package io.github.fi0x.wordle.db;

import io.github.fi0x.wordle.db.entities.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepo extends JpaRepository<GameEntity, Long>
{
}
