package io.github.fi0x.sailing.service;

import io.github.fi0x.sailing.db.RaceRepo;
import io.github.fi0x.sailing.db.RaceResultRepo;
import io.github.fi0x.sailing.db.entities.RaceEntity;
import io.github.fi0x.sailing.db.entities.RaceId;
import io.github.fi0x.sailing.db.entities.RaceResultEntity;
import io.github.fi0x.sailing.db.entities.RaceResultId;
import io.github.fi0x.sailing.logic.converter.RaceConverter;
import io.github.fi0x.sailing.logic.converter.RaceResultToDtoConverter;
import io.github.fi0x.sailing.logic.dto.*;
import io.github.fi0x.util.components.Authenticator;
import io.github.fi0x.util.dto.UserRoles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RaceService
{
	private static final String M2S_BASE_URL = "https://www.manage2sail.com/api/event";
	private static final String M2S_UI_URL = "https://www.manage2sail.com/de-DE/event";
	private static final String FINAL_RACE_NAME = "RVB Final Race";
	private static final Map<String, Double> ORC_RACES = new HashMap<>()
	{{
		put("39. Internationaler Drei-Länder-Cup 2025", 3.0);
		put("Graf-Zeppelin-Regatta", 3.0);
		put("74. RUND UM 2025", 3.3);
		put("West - Ost 2025", 3.3);
		put("53. Altnauer TagNacht Regatta", 3.0);
		put("Blue Planet Flug Trophy", 3.0);
		put(FINAL_RACE_NAME, 3.0);
	}};
	private static final List<String> INVALID_RACE_STATUS = List.of("DNS", "DNC");

	private final Authenticator authenticator;
	private final RaceRepo raceRepo;
	private final RaceResultRepo resultRepo;
	private final RaceResultToDtoConverter raceResultConverter;
	private final RaceConverter raceConverter;

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

	@Transactional
	public List<RaceResultEntity> saveRace(String raceResultUrl)
	{
		authenticator.restAuthenticate(UserRoles.ADMIN);

		//TODO: Retrieve html page for URL and get div with id='classes'
		// Get table from that div, then all tr elements that are not in the table head
		// Get race-result IDs from href links and the names of those result groups
		// Show the user the possible races to select (multi-select possible)
		// Load all results the user had selected and show them after each other to the user
		// Retrieved json should use the race-names and let the user decide which results to keep (multi-select)

		//Example races:
		// https://www.manage2sail.com/de-DE/event/7da1f04b-bd3a-4068-8d31-4ecf17bdc1bb#!/
		// https://www.manage2sail.com/de-DE/event/6695807a-a06f-49d5-863d-39c96c82d6cf#!/

		List<RaceEntity> existingEntities = getRace(raceResultUrl);
		if (!existingEntities.isEmpty() && existingEntities.size() > 1)
		{
			throw new IllegalArgumentException(
					"Multiple races with that url are already loaded. Url: " + raceResultUrl);
		}

		String m2sRaceId = getM2sRaceId(raceResultUrl);
		String m2sClassId = getM2sClassId(raceResultUrl);
		String url = M2S_BASE_URL + "/" + m2sRaceId + "/regattaresult/" + m2sClassId;
		RestTemplate restTemplate = new RestTemplate();
		M2sRaceResultJsonDto result = restTemplate.getForObject(url, M2sRaceResultJsonDto.class);

		Document page;
		String raceName;
		long startDate = 0;
		long endDate = 0;
		double scoreModifier = 1.0;
		boolean isOrcRace = false;
		try
		{
			page = Jsoup.connect(M2S_UI_URL + "/" + getM2sRaceId(raceResultUrl)).get();
			Element eventName = page.getElementsByClass("eventName").first();
			raceName = eventName.getElementsByTag("h1").first().text();
			String[] dates = eventName.getElementsByClass("eventDates").first().text().split(" - ");
			SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
			switch (dates.length)
			{
				case 1:
					startDate = dateFormatter.parse(dates[0]).getTime();
					endDate = dateFormatter.parse(dates[0]).getTime();
					break;
				case 2:
					startDate = dateFormatter.parse(dates[0]).getTime();
					endDate = dateFormatter.parse(dates[1]).getTime();
					break;
				default:
					log.warn("Could not add a date to race with url: {}", url);
			}
			if (ORC_RACES.containsKey(raceName))
			{
				scoreModifier = ORC_RACES.get(raceName);
				isOrcRace = true;
			}
		} catch (IOException | NullPointerException | ParseException e)
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not fetch race-details from manage2sail");
		}

		RaceEntity raceEntity = RaceEntity.builder().name(raceName).startDate(startDate)
										  .raceGroup(getCleanedGroupName(result.getRaceGroup())).endDate(endDate)
										  .scoreModifier(scoreModifier).url(raceResultUrl).orcRace(isOrcRace)
										  .bufferRace(!raceName.contains(FINAL_RACE_NAME))
										  .participants(getActiveParticipants(result)).build();
		raceRepo.save(raceEntity);

		List<RaceResultEntity> raceResultEntities = new ArrayList<>();
		for (M2sSingleResultJsonDto singleResult : result.getShipResults())
		{
			RaceResultEntity resultEntity = RaceResultEntity.builder().name(raceName).startDate(startDate)
															.raceGroup(raceEntity.getRaceGroup())
															.skipper(singleResult.getSkipper())
															.shipName(singleResult.getShipName())
															.position(singleResult.getPosition())
															.score(calculateScore(singleResult.getPosition(),
																				  raceEntity))
															.shipClass(singleResult.getShipClass()).build();
			raceResultEntities.add(resultEntity);
			resultRepo.save(resultEntity);
		}

		return raceResultEntities;
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

	private List<RaceEntity> getRace(String url)
	{
		return raceRepo.findAllByUrl(url);
	}

	private String getM2sRaceId(String url)
	{
		return url.split("event/")[1].split("#!/")[0];
	}

	private String getM2sClassId(String url)
	{
		return url.split("/?classId=")[1];
	}

	private String getCleanedGroupName(String raceGroup)
	{
		String cleaned = raceGroup.toUpperCase().replace("-", " ").replaceAll("\\(.*?\\)", "");
		cleaned = cleaned.trim();
		if (cleaned.endsWith("E"))
			return cleaned.substring(0, cleaned.length() - 1);

		return cleaned;
	}

	private Integer getActiveParticipants(M2sRaceResultJsonDto results)
	{
		return results.getShipResults().stream().filter(participant -> participant.getRaceEntries().stream().anyMatch(
				singleRace -> singleRace.getRaceStatus() == null || !INVALID_RACE_STATUS.contains(
						singleRace.getRaceStatus()))).toList().size();
	}

	private Double calculateScore(Integer position, RaceEntity race)
	{
		return ((double) (race.getParticipants() - position + 1)) / ((double) (race.getParticipants() + 1)) * 100.0 * race.getScoreModifier();
	}
}
