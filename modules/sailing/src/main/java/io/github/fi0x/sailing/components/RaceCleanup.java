package io.github.fi0x.sailing.components;

import io.github.fi0x.sailing.db.RaceRepo;
import io.github.fi0x.sailing.db.RaceResultRepo;
import io.github.fi0x.sailing.db.entities.RaceEntity;
import io.github.fi0x.sailing.db.entities.RaceId;
import io.github.fi0x.sailing.db.entities.RaceResultEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RaceCleanup
{
	private final RaceRepo raceRepo;
	private final RaceResultRepo resultRepo;

	@Scheduled(fixedRate = 3600000)
	public void cleanDatabase()
	{
		log.debug("Race-Result cleanup running");

		List<RaceId> existingRaceIds = raceRepo.findAll().stream().map(RaceEntity::getId).toList();
		List<RaceResultEntity> resultsWithoutRace = resultRepo.findAll().stream().filter(result -> {
			RaceId resultId = new RaceId(result.getName(), result.getStartDate(), result.getRaceGroup());
			return !existingRaceIds.contains(resultId);
		}).toList();
		resultRepo.deleteAll(resultsWithoutRace);

		log.debug("Cleanup complete");
	}
}
