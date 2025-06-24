package io.github.fi0x.sailing.service;

import io.github.fi0x.sailing.db.RaceRepo;
import io.github.fi0x.sailing.db.RaceResultRepo;
import io.github.fi0x.sailing.db.entities.RaceEntity;
import io.github.fi0x.sailing.db.entities.RaceResultEntity;
import io.github.fi0x.sailing.logic.dto.ShipRaceResults;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RaceService
{
	private final RaceRepo raceRepo;
	private final RaceResultRepo resultRepo;

	public List<RaceEntity> getAllOrcRaces()
	{
		return raceRepo.findAllByOrcRaceOOrderByStartDateAsc(true);
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
}
