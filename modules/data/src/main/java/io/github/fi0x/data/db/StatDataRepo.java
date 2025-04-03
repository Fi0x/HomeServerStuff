package io.github.fi0x.data.db;

import io.github.fi0x.data.db.entities.DataEntity;
import io.github.fi0x.data.db.entities.DataId;
import io.github.fi0x.data.db.entities.StatDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StatDataRepo extends JpaRepository<StatDataEntity, DataId>
{
	List<DataEntity> findAllByAddressAndSensorOrderByTimestampAsc(String address, String sensor);

	@Query(value = "SELECT * FROM dtdata WHERE ADDRESS = ?1 AND SENSOR = ?2 AND TIMESTAMP >= ?3", nativeQuery = true)
	List<DataEntity> findFromSensorYoungerThan(String address, String sensor, Long timestamp);

	@Query(value = "SELECT * FROM dtdata WHERE ADDRESS = ?1 AND SENSOR = ?2 ORDER BY TIMESTAMP DESC LIMIT ?3",
			nativeQuery = true)
	List<DataEntity> findLastXFromSensor(String address, String sensor, Integer amount);
}
