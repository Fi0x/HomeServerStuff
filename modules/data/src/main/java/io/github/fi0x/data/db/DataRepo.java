package io.github.fi0x.data.db;

import io.github.fi0x.data.db.entities.DataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DataRepo extends JpaRepository<DataEntity, Long>
{
	@Query(value = "SELECT MAX(ID) FROM data", nativeQuery = true)
	Optional<Long> getHighestId();
}
