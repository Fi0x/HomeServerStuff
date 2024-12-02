package io.github.fi0x.data.db;

import io.github.fi0x.data.db.entities.DataEntity;
import io.github.fi0x.data.db.entities.DataId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DataRepo extends JpaRepository<DataEntity, DataId>
{
	@Query(value = "SELECT * FROM dtdata WHERE TIMESTAMP < ?1", nativeQuery = true)
	List<DataEntity> findAllValuesOlderThan(Long time);
}
