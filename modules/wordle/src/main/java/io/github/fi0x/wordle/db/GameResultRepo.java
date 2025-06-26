package io.github.fi0x.wordle.db;

import io.github.fi0x.wordle.db.entities.GameResultEntity;
import io.github.fi0x.wordle.db.entities.GameResultId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameResultRepo extends JpaRepository<GameResultEntity, GameResultId>
{
}
