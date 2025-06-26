package io.github.fi0x.wordle.db;

import io.github.fi0x.wordle.db.entities.WordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepo extends JpaRepository<WordEntity, String>
{
}
