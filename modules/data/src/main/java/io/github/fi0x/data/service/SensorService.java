package io.github.fi0x.data.service;

import io.github.fi0x.data.db.DataRepo;
import io.github.fi0x.data.db.SensorRepo;
import io.github.fi0x.data.db.TagRepo;
import io.github.fi0x.data.db.entities.DataEntity;
import io.github.fi0x.data.db.entities.SensorEntity;
import io.github.fi0x.data.db.entities.SensorId;
import io.github.fi0x.data.db.entities.TagEntity;
import io.github.fi0x.data.logic.converter.SensorConverter;
import io.github.fi0x.data.logic.dto.ExpandedSensorDto;
import io.github.fi0x.data.logic.dto.SensorDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorService
{
	private final SensorRepo sensorRepo;
	private final TagRepo tagRepo;
	private final DataRepo dataRepo;

	public void saveSensor(String address, SensorDto sensor)
	{
		SensorId id = new SensorId(address, sensor.getName());
		SensorEntity sensorEntity = sensorRepo.findById(id)
											  .orElse(SensorEntity.builder().address(address).name(sensor.getName())
																  .build());

		sensorEntity.setLastUpdate(System.currentTimeMillis());
		sensorEntity.setDescription(sensor.getDescription());
		sensorEntity.setUnit(sensor.getUnit());
		sensorEntity.setType(sensor.getType());
		sensorRepo.save(sensorEntity);

		List<TagEntity> existingTags = tagRepo.findAllBySensorName(sensor.getName());
		existingTags.forEach(tag -> {
			if (!sensor.getTags().contains(tag.getTag()))
				tagRepo.delete(tag);
		});
		sensor.getTags().forEach(tag -> {
			TagEntity tagEntity = new TagEntity(sensor.getName(), tag);
			if (!existingTags.contains(tagEntity))
				tagRepo.save(tagEntity);
		});
	}

	public List<ExpandedSensorDto> getAllDetailedSensors()
	{
		List<ExpandedSensorDto> sensorDtos =
				sensorRepo.findAll().stream().map(SensorConverter::toExpandedDto).toList();

		sensorDtos.forEach(dto -> {
			List<TagEntity> tags = tagRepo.findAllBySensorName(dto.getName());
			dto.setTags(tags.stream().map(TagEntity::getTag).toList());
		});
		sensorDtos.forEach(dto -> {
			Optional<DataEntity> mostRecentValue = dataRepo.findFirstByAddressAndSensorOrderByTimestampDesc(
					dto.getAddress(), dto.getName());
			if (mostRecentValue.isPresent())
				dto.setValue(mostRecentValue.get().getValue());
			else
				dto.setValue(0D);
		});
		return sensorDtos;
	}

	public List<String> getAllSensorTypes()
	{
		return sensorRepo.findAll().stream().map(SensorEntity::getType).toList();
	}
}
