package io.github.fi0x.wordle.components;


import io.github.fi0x.wordle.db.GameRepo;
import io.github.fi0x.wordle.db.GameResultRepo;
import io.github.fi0x.wordle.db.entities.GameEntity;
import io.github.fi0x.wordle.db.entities.GameResultEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseCleanup
{
	private final GameRepo gameRepo;
	private final GameResultRepo resultRepo;

	@Scheduled(fixedRate = 3600000)
	public void cleanDatabase()
	{
		log.debug("Database cleanup running");

		Long latestPossibleTimestamp = System.currentTimeMillis() - 3600000;

		List<GameEntity> allGames = gameRepo.findAll();
		List<GameEntity> oldGames = allGames.stream()
											.filter(gameEntity -> gameEntity.getTimestamp() < latestPossibleTimestamp)
											.toList();
		List<GameResultEntity> allResults = resultRepo.findAll();
		List<GameResultEntity> oldResults = allResults.stream()
													  .filter(gameResultEntity -> gameResultEntity.getTimestamp() < latestPossibleTimestamp)
													  .toList();

		List<Long> gameTimestamps = allGames.stream().map(GameEntity::getTimestamp).toList();
		List<Long> resultTimestamps = allResults.stream().map(GameResultEntity::getTimestamp).toList();

		for (GameEntity game : oldGames)
		{
			if (!resultTimestamps.contains(game.getTimestamp()))
				gameRepo.delete(game);
		}

		for (GameResultEntity result : oldResults)
		{
			if (!gameTimestamps.contains(result.getTimestamp()))
				resultRepo.delete(result);
		}

		log.debug("Database cleanup complete");
	}
}
