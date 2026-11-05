package io.github.fi0x.data.service;

import io.github.fi0x.data.db.SensorRepo;
import io.github.fi0x.data.db.entities.SensorEntity;
import io.github.fi0x.data.db.entities.SensorId;
import io.github.fi0x.data.logic.dto.SensorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersistenceSensorService
{
	private final SensorRepo sensorRepo;

	public SensorEntity saveIfNew(String address, String name, String description, String unit, String type,
								  Long dataDelay, Double valueAdjustment, Double minValue, Double maxValue)
	{
		return findSensorEntity(address, name).orElse(
				saveNewSensor(address, name, description, unit, type, dataDelay, valueAdjustment, minValue, maxValue));
	}

	public SensorEntity saveNewSensor(String address, String name, String type, Long dataDelay)
	{
		String unit = SensorType.VPD.is(type) ? "kPa" : "";
		return saveNewSensor(address, name, name, unit, type, dataDelay, 0.0, null, null);
	}

	public void addVPDSensor(String address, String name, String vpdSensorName)
	{
		SensorEntity sensorEntity = getSensorEntity(address, name);

		long timestamp = System.currentTimeMillis();
		sensorEntity.setLastUpdate(timestamp);
		sensorEntity.setVPDSensorName(vpdSensorName);

		sensorRepo.save(sensorEntity);
	}

	public Optional<SensorEntity> findSensorEntity(String address, String name)
	{
		SensorId id = new SensorId(address, name);
		return sensorRepo.findById(id);
	}

	public List<SensorEntity> getAllSensors(String address)
	{
		return sensorRepo.findAllByAddress(address);
	}

	private SensorEntity getSensorEntity(String address, String name)
	{
		return findSensorEntity(address, name).orElse(SensorEntity.builder().address(address).name(name).build());
	}

	private SensorEntity saveNewSensor(String address, String name, String displayName, String unit, String type,
									   Long dataDelay, Double valueAdjustment, Double minValue, Double maxValue)
	{
		long timestamp = System.currentTimeMillis();

		SensorEntity sensorEntity = SensorEntity.builder().address(address).name(name).displayName(displayName)
				.lastUpdate(timestamp).unit(unit).type(type).dataDelay(dataDelay).valueAdjustment(valueAdjustment)
				.minValue(minValue).maxValue(maxValue).build();

		sensorRepo.save(sensorEntity);
		return sensorEntity;
	}
}
