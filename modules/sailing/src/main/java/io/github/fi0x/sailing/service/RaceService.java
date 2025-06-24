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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


	private final RaceRepo raceRepo;
	private final RaceResultRepo resultRepo;

	public List<RaceEntity> getAllOrcRaces(String group)
	{
		List<RaceEntity> resultEntities = raceRepo.findAllByOrcRaceOrderByStartDateAsc(true);

		if(group != null)
			return resultEntities.stream().filter(result -> result.getRaceGroup().equals(group)).toList();

		return resultEntities;
	}

	public List<RaceEntity> getAllRaces()
	{
		List<RaceEntity> entities = raceRepo.findAll();
		entities.sort(
				(a, b) -> a.getStartDate() - b.getStartDate() > 0 ? 1 : a.getStartDate() - b.getStartDate() == 0 ? 0 : -1);
		return entities;
	}

	public List<String> getAllOrcRaceGroups()
	{
		return raceRepo.findAll().stream().map(RaceEntity::getRaceGroup).distinct().toList();
	}

	public List<ShipRaceResults> getAllResults(String group)
	{
		List<ShipRaceResults> results = new ArrayList<>();

		List<RaceResultEntity> entities = resultRepo.findAll(Sort.by("shipName", "skipper"));
		if(group != null)
			entities = entities.stream().filter(result -> result.getRaceGroup().equals(group)).toList();

		ShipRaceResults current = null;
		for(RaceResultEntity entity : entities)
		{
			if(current == null || !current.getShipName().equals(entity.getShipName()) || !current.getSkipper()
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
		if(!existingEntities.isEmpty())
		{
			if(existingEntities.size() > 1)
				throw new IllegalArgumentException(
						"Multiple races with that url are already loaded. Url: " + raceResultUrl);
			//TODO: Re-calculate scores if possible
			return getResults(existingEntities.get(0).getId());
		}

		String m2sRaceId = getM2sRaceId(raceResultUrl);
		String m2sClassId = getM2sClassId(raceResultUrl);
		String url = M2S_BASE_URL + "/" + m2sRaceId + "/regattaresult/" + m2sClassId;
		RestTemplate restTemplate = new RestTemplate();
		M2sRaceResultJsonDto result = restTemplate.getForObject(url, M2sRaceResultJsonDto.class);

		if(result == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find any results for url: " + url);

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
			switch(dates.length)
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
			if(ORC_RACES.containsKey(raceName))
			{
				scoreModifier = ORC_RACES.get(raceName);
				isOrcRace = true;
			}
		} catch(IOException | NullPointerException | ParseException e)
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not fetch race-details from manage2sail");
		}

		RaceEntity raceEntity =
				RaceEntity.builder().name(raceName).startDate(startDate).raceGroup(result.getRaceGroup())
						  .endDate(endDate).scoreModifier(scoreModifier).url(raceResultUrl).orcRace(isOrcRace)
						  .bufferRace(!raceName.contains(FINAL_RACE_NAME)).participants(result.getShipResults().size())
						  .build();
		raceRepo.save(raceEntity);

		List<RaceResultEntity> raceResultEntities = new ArrayList<>();
		for(M2sSingleResultJsonDto singleResult : result.getShipResults())
		{
			RaceResultEntity resultEntity =
					RaceResultEntity.builder().name(raceName).startDate(startDate).raceGroup(result.getRaceGroup())
									.skipper(singleResult.getSkipper()).shipName(singleResult.getShipName())
									.position(singleResult.getPosition()).shipClass(singleResult.getShipClass())
									.build();
			raceResultEntities.add(resultEntity);
			resultRepo.save(resultEntity);
		}

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
