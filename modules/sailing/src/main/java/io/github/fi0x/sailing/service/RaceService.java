package io.github.fi0x.sailing.service;

import io.github.fi0x.sailing.db.RaceRepo;
import io.github.fi0x.sailing.db.RaceResultRepo;
import io.github.fi0x.sailing.db.entities.RaceEntity;
import io.github.fi0x.sailing.db.entities.RaceId;
import io.github.fi0x.sailing.db.entities.RaceResultEntity;
import io.github.fi0x.sailing.logic.dto.M2sRaceResultJsonDto;
import io.github.fi0x.sailing.logic.dto.M2sSingleResultJsonDto;
import io.github.fi0x.sailing.logic.dto.ShipRaceResults;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RaceService
{
	private static final String M2S_BASE_URL = "https://www.manage2sail.com/api/event";

	private final RaceRepo raceRepo;
	private final RaceResultRepo resultRepo;

	public List<RaceEntity> getAllOrcRaces()
	{
		return raceRepo.findAllByOrcRaceOrderByStartDateAsc(true);
	}

	public List<ShipRaceResults> getAllResults()
	{
		List<ShipRaceResults> results = new ArrayList<>();

		List<RaceResultEntity> entities = resultRepo.findAll(Sort.by("shipName", "skipper"));
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

			current.getRaceResults().add(entity);
		}

		return results;
	}

	public List<RaceResultEntity> saveRace(String raceResultUrl)
	{
		List<RaceEntity> existingEntities = getRace(raceResultUrl);
		if (!existingEntities.isEmpty())
		{
			if (existingEntities.size() > 1)
				throw new IllegalArgumentException(
						"Multiple races with that url are already loaded. Url: " + raceResultUrl);
			return getResults(existingEntities.get(0).getId());
		}

		String m2sRaceId = getM2sRaceId(raceResultUrl);
		String m2sClassId = getM2sClassId(raceResultUrl);
		String url = M2S_BASE_URL + "/" + m2sRaceId + "/regattaresult/" + m2sClassId;
		RestTemplate restTemplate = new RestTemplate();
		M2sRaceResultJsonDto result = restTemplate.getForObject(url, M2sRaceResultJsonDto.class);

		if (result == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find any results for url: " + url);

		//TODO: Fill missing values with additional calls
		//M2s information:
		String raceName = "";
		Long startDate = 0L;
		Long endDate = 0L;
		//Orc Rangliste information
		Double scoreModifier = 0.0;
		Boolean isOrcRace = false;
		//Ensure, race-name is not 'Final Race'
		Boolean isBufferRace = true;

		RaceEntity raceEntity = RaceEntity.builder().name(raceName).startDate(startDate)
										  .raceGroup(result.getRaceGroup()).endDate(endDate)
										  .scoreModifier(scoreModifier).url(raceResultUrl).orcRace(isOrcRace)
										  .bufferRace(isBufferRace).build();

		List<RaceResultEntity> raceResultEntities = new ArrayList<>();
		for (M2sSingleResultJsonDto singleResult : result.getShipResults())
		{
			raceResultEntities.add(
					RaceResultEntity.builder().name(raceName).startDate(startDate).raceGroup(result.getRaceGroup())
									.skipper(singleResult.getSkipper()).shipName(singleResult.getShipName())
									.position(singleResult.getPosition()).shipClass(singleResult.getShipClass())
									.build());
		}

		//TODO: Save results and race details in database

		return raceResultEntities;
	}

	private List<RaceEntity> getRace(String url)
	{
		return raceRepo.findAllByUrl(url);
	}

	private List<RaceResultEntity> getResults(RaceId raceId)
	{
		return resultRepo.findAllByNameAndStartDate(raceId.getName(), raceId.getStartDate());
	}

	private String getM2sRaceId(String url)
	{
		return url.split("event/")[1].split("#!/")[0];
	}

	private String getM2sClassId(String url)
	{
		return url.split("/?classId=")[1];
	}
}
