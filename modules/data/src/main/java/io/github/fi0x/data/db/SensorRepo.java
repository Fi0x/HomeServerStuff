package io.github.fi0x.data.db;

import io.github.fi0x.data.db.entities.SensorEntity;
import io.github.fi0x.data.db.entities.SensorId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SensorRepo extends JpaRepository<SensorEntity, SensorId>
{
	Optional<SensorEntity> findByAddressAndName(String address, String name);

	List<SensorEntity> findAllByName(String name);
}
