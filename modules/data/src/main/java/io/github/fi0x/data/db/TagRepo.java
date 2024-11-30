package io.github.fi0x.data.db;

import io.github.fi0x.data.db.entities.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TagRepo extends JpaRepository<TagEntity, Long>
{
	Optional<TagEntity> findByTag(String tag);

	@Query(value = "SELECT MAX(ID) FROM tags", nativeQuery = true)
	Optional<Long> getHighestId();
}
