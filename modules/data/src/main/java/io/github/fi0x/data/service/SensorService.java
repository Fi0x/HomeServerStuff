package io.github.fi0x.data.service;

import io.github.fi0x.data.db.SensorRepo;
import io.github.fi0x.data.db.TagRepo;
import io.github.fi0x.data.db.entities.SensorEntity;
import io.github.fi0x.data.db.entities.SensorId;
import io.github.fi0x.data.db.entities.TagEntity;
import io.github.fi0x.data.logic.dto.ExpandedSensorDto;
import io.github.fi0x.data.logic.dto.SensorDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorService
{
	private final SensorRepo sensorRepo;
	private final TagRepo tagRepo;

	public void saveSensor(String address, SensorDto sensor)
	{
		SensorId id = new SensorId(address, sensor.getName());
		SensorEntity sensorEntity =
				sensorRepo.findById(id).orElse(SensorEntity.builder().address(address).name(sensor.getName()).build());

		sensorEntity.setLastUpdate(System.currentTimeMillis());
		sensorEntity.setDescription(sensor.getDescription());
		sensorEntity.setUnit(sensor.getUnit());
		sensorEntity.setType(sensor.getType());
		sensorRepo.save(sensorEntity);

		List<TagEntity> existingTags = tagRepo.findAllBySensorName(sensor.getName());
		existingTags.forEach(tag -> {
			if(!sensor.getTags().contains(tag.getTag()))
				tagRepo.delete(tag);
		});
		sensor.getTags().forEach(tag -> {
			TagEntity tagEntity = new TagEntity(sensor.getName(), tag);
			if(!existingTags.contains(tagEntity))
				tagRepo.save(tagEntity);
		});
	}

	public List<ExpandedSensorDto> getAllDetailedSensors()
	{
		//TODO: Retrieve a list of all sensors and enrich it with the most recent measuring-value and address
		throw new RuntimeException("Not yet implemented");
	}

	public List<String> getAllSensorTypes()
	{
		//TODO: Retrieve a list of all sensors and return the types as a set
		throw new RuntimeException("Not yet implemented");
	}
}
