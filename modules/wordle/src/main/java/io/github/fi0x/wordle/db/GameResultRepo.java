package io.github.fi0x.wordle.db;

import io.github.fi0x.wordle.db.entities.GameResultEntity;
import io.github.fi0x.wordle.db.entities.GameResultId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameResultRepo extends JpaRepository<GameResultEntity, GameResultId>
{
	List<GameResultEntity> findAllByTimestamp(Long timestamp);
}
