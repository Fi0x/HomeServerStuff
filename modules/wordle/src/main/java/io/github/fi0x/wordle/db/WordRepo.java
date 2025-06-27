package io.github.fi0x.wordle.db;

import io.github.fi0x.wordle.db.entities.WordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WordRepo extends JpaRepository<WordEntity, String>
{
	Optional<WordEntity> findFirstByVerifiedGreaterThanOrderByVerified(Integer verified);
}
