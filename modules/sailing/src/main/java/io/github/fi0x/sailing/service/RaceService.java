package io.github.fi0x.sailing.service;

import io.github.fi0x.sailing.components.Manage2SailRetriever;
import io.github.fi0x.sailing.db.RaceRepo;
import io.github.fi0x.sailing.db.RaceResultRepo;
import io.github.fi0x.sailing.db.entities.RaceEntity;
import io.github.fi0x.sailing.db.entities.RaceId;
import io.github.fi0x.sailing.db.entities.RaceResultEntity;
import io.github.fi0x.sailing.db.entities.RaceResultId;
import io.github.fi0x.sailing.logic.converter.RaceConverter;
import io.github.fi0x.sailing.logic.converter.RaceResultToDtoConverter;
import io.github.fi0x.sailing.logic.dto.RaceInfoDto;
import io.github.fi0x.sailing.logic.dto.RaceInformation;
import io.github.fi0x.sailing.logic.dto.RaceResultDto;
import io.github.fi0x.sailing.logic.dto.ShipRaceResults;
import io.github.fi0x.sailing.logic.dto.m2s.M2sClass;
import io.github.fi0x.util.components.Authenticator;
import io.github.fi0x.util.dto.UserRoles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RaceService
{
	private static final String M2S_BASE_URL = "https://www.manage2sail.com/api/event";

	private final Authenticator authenticator;
	private final RaceRepo raceRepo;
	private final RaceResultRepo resultRepo;
	private final RaceResultToDtoConverter raceResultConverter;
	private final RaceConverter raceConverter;
	private final Manage2SailRetriever m2sRetriever;

	public List<RaceEntity> getAllOrcRaces(String group, Integer year)
	{
		List<RaceEntity> resultEntities = raceRepo.findAllByOrcRaceOrderByStartDateAsc(true);
		return filterYearAndGroup(resultEntities, group, year, RaceEntity.class);
	}

	public List<RaceInfoDto> getAllRaces()
	{
		return getAllRaces(null, null).stream().map(raceConverter::convert).toList();
	}

	public List<RaceEntity> getAllRaces(String group, Integer year)
	{
		List<RaceEntity> entities = raceRepo.findAll();
		entities.sort(
				(a, b) -> a.getStartDate() - b.getStartDate() > 0 ? 1 : a.getStartDate() - b.getStartDate() == 0 ? 0 :
						-1);

		return filterYearAndGroup(entities, group, year, RaceEntity.class);
	}

	public List<String> getAllRaceGroupsWithYear()
	{
		return raceRepo.findAll().stream().map(RaceEntity::getGroupAndYear).distinct().toList();
	}

	public List<RaceResultDto> getAllResults(String raceName, Long startDate, String raceGroup)
	{
		return resultRepo.findAllByNameAndStartDateAndRaceGroup(raceName, startDate, raceGroup).stream()
						 .map(raceResultConverter::convert).toList();
	}

	public List<ShipRaceResults> getAllResults(String group, Integer year)
	{
		List<ShipRaceResults> results = new ArrayList<>();

		List<RaceResultEntity> entities = resultRepo.findAll(Sort.by("shipName", "skipper"));
		entities = filterYearAndGroup(entities, group, year, RaceResultEntity.class);

		List<RaceEntity> races = raceRepo.findAll();
		races = filterYearAndGroup(races, group, year, RaceEntity.class);

		ShipRaceResults current = null;
		for (RaceResultEntity entity : entities)
		{
			if (current == null || !current.getShipName().equals(entity.getShipName()) || !current.getSkipper()
																								  .equals(entity.getSkipper()))
			{
				current = new ShipRaceResults(entity.getShipName(), entity.getSkipper(), entity.getShipClass(),
											  new ArrayList<>());
				results.add(current);
			}

			RaceResultDto raceResultDto = raceResultConverter.convert(entity);
			raceResultDto.setUrl(
					races.stream().filter(race -> race.getId().equals(raceResultDto.getRaceId())).findFirst()
						 .orElse(RaceEntity.builder().url("").build()).getUrl());
			current.getRaceResults().add(raceResultDto);
		}

		Map<RaceId, Boolean> raceMap = raceRepo.findAll().stream()
											   .collect(Collectors.toMap(RaceEntity::getId,
																		 RaceEntity::getBufferRace));
		for (ShipRaceResults shipDetails : results)
		{
			List<RaceResultDto> crossableResults = shipDetails.getRaceResults().stream()
															  .filter(res -> raceMap.get(res.getRaceId()))
															  .sorted((a, b) -> a.getScore() - b.getScore() == 0 ? 0 :
																	  a.getScore() - b.getScore() > 0 ? 1 : -1)
															  .toList();
			if (crossableResults.size() <= 4)
				continue;

			crossableResults.get(0).setCrossed(true);
			if (crossableResults.size() > 5)
				crossableResults.get(1).setCrossed(true);
		}

		return results;
	}

	public List<M2sClass> getRaceClasses(String raceOverviewUrl)
	{
		authenticator.restAuthenticate(UserRoles.ADMIN);
		try
		{
			List<M2sClass> classes = m2sRetriever.getRaceClasses(raceOverviewUrl);
			if (classes.isEmpty())
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not retrieve results for race");
			return classes;
		} catch (IOException e)
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
											  "Could not retireve manage2sail details for the race", e);
		}
	}

	public List<RaceResultDto> loadSpecificRaceClassResults(M2sClass selectedRaceClass)
	{
		String m2sEventId = getM2sRaceId(selectedRaceClass.getClassUrl()).split("/#!")[0];
		String m2sClassId = getM2sClassId(selectedRaceClass.getClassUrl());
		String classResultUrl = M2S_BASE_URL + "/" + m2sEventId + "/regattaresult/" + m2sClassId;
		return m2sRetriever.getClassResults(classResultUrl, selectedRaceClass.getRaceEventName(),
											selectedRaceClass.getStartDate(), selectedRaceClass.getEndDate(),
											selectedRaceClass.getEventUrl());
	}

	@Transactional
	public void saveRaceResults(List<RaceResultDto> raceResults)
	{
		authenticator.restAuthenticate(UserRoles.ADMIN);

		List<RaceResultEntity> resultEntities = new ArrayList<>();

		raceResults.sort(Comparator.comparing(RaceResultDto::getName).thenComparing(RaceResultDto::getStartDate)
								   .thenComparing(RaceResultDto::getRaceGroup));

		RaceEntity currentRace = new RaceEntity();
		for (RaceResultDto result : raceResults)
		{
			if (!result.getRaceId().equals(currentRace.getId()))
			{
				long participants = raceResults.stream().filter(r -> r.getRaceId().equals(result.getRaceId())).count();
				currentRace = RaceEntity.builder().name(result.getName()).startDate(result.getStartDate())
										.raceGroup(result.getRaceGroup()).endDate(result.getEndDate())
										.scoreModifier(1.0).url(result.getUrl()).orcRace(false).bufferRace(true)
										.participants((int) participants).build();
				raceRepo.save(currentRace);
			}

			Double score = calculateScore(result.getPosition(), currentRace);
			RaceResultEntity resultEntity = RaceResultEntity.builder().name(result.getName())
															.startDate(result.getStartDate())
															.raceGroup(result.getRaceGroup())
															.skipper(result.getSkipper()).shipName(result.getShipName())
															.position(result.getPosition()).score(score)
															.shipClass(result.getShipClass()).build();
			resultEntities.add(resultEntity);
		}
		resultRepo.saveAll(resultEntities);
	}

	@Transactional
	public void deleteResult(String raceName, Long startDate, String raceGroup, String skipper)
	{
		authenticator.restAuthenticate(UserRoles.ADMIN);

		if (skipper == null)
			deleteRace(raceName, startDate, raceGroup);
		else
			deleteSingleResult(raceName, startDate, raceGroup, skipper);
	}

	@Transactional
	public void updateRace(String raceName, Long startDate, String raceGroup, RaceInfoDto updateDto)
	{
		RaceEntity originalEntity = raceRepo.findById(new RaceId(raceName, startDate, raceGroup)).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
												  "Could not update race, because no entry exists for it."));

		RaceEntity newEntity = raceConverter.convert(updateDto);
		if (newEntity.getName() == null)
			newEntity.setName(originalEntity.getName());
		if (newEntity.getStartDate() == null)
			newEntity.setStartDate(originalEntity.getStartDate());
		if (newEntity.getRaceGroup() == null)
			newEntity.setRaceGroup(originalEntity.getRaceGroup());
		if (newEntity.getScoreModifier() == null)
			newEntity.setScoreModifier(originalEntity.getScoreModifier());
		if (newEntity.getOrcRace() == null)
			newEntity.setOrcRace(originalEntity.getOrcRace());
		if (newEntity.getBufferRace() == null)
			newEntity.setBufferRace(originalEntity.getBufferRace());
		if (newEntity.getParticipants() == null)
			newEntity.setParticipants(originalEntity.getParticipants());
		if (newEntity.getUrl() == null)
			newEntity.setUrl(originalEntity.getUrl());

		raceRepo.save(newEntity);

		List<RaceResultEntity> raceResults = resultRepo.findAllByNameAndStartDateAndRaceGroup(raceName, startDate,
																							  raceGroup);
		List<RaceResultEntity> updatedRaceResults = new ArrayList<>();
		raceResults.forEach(e -> {
			RaceResultEntity e2 = e.clone();
			e2.setName(newEntity.getName());
			e2.setStartDate(newEntity.getStartDate());
			e2.setRaceGroup(newEntity.getRaceGroup());
			e2.setScore(calculateScore(e2.getPosition(), newEntity));
			updatedRaceResults.add(e2);
		});
		resultRepo.saveAll(updatedRaceResults);

		if (!newEntity.getName().equals(raceName) || !newEntity.getStartDate()
															   .equals(startDate) || !newEntity.getRaceGroup()
																							   .equals(raceGroup))
		{
			raceRepo.delete(originalEntity);
			resultRepo.deleteAll(raceResults);
		}
	}

	private void deleteRace(String raceName, Long startDate, String raceGroup)
	{
		RaceId id = new RaceId(raceName, startDate, raceGroup);

		RaceEntity raceEntity = raceRepo.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Race does not exist"));

		resultRepo.deleteAllByNameAndStartDateAndRaceGroup(raceName, startDate, raceGroup);
		raceRepo.delete(raceEntity);

		log.trace("Deleted race and all of its entries for race '{}' date '{}' group '{}'", raceName, startDate,
				  raceGroup);
	}

	private void deleteSingleResult(String raceName, Long startDate, String raceGroup, String skipper)
	{
		RaceResultId id = new RaceResultId(raceName, startDate, raceGroup, skipper);

		log.trace("Removing result for {}", id);

		resultRepo.deleteById(id);
	}

	private <X extends RaceInformation> List<X> filterYearAndGroup(List<X> raceEntities, String group, Integer year,
																   Class<X> resultClass)
	{
		if (group != null)
			raceEntities = raceEntities.stream().filter(result -> result.getRaceGroup().equals(group)).toList();
		if (year != null)
			raceEntities = raceEntities.stream().filter(result -> {
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(new Date(result.getStartDate()));
				return calendar.get(Calendar.YEAR) == year;
			}).toList();

		return raceEntities;
	}

	private String getM2sRaceId(String url)
	{
		return url.split("event/")[1].split("#!/")[0];
	}

	private String getM2sClassId(String url)
	{
		return url.split("/?classId=")[1];
	}


	private Double calculateScore(Integer position, RaceEntity race)
	{
		return ((double) (race.getParticipants() - position + 1)) / ((double) (race.getParticipants() + 1)) * 100.0 * race.getScoreModifier();
	}
}
