package io.github.fi0x.data.service;

import io.github.fi0x.data.components.Sensor;
import io.github.fi0x.data.db.DataRepo;
import io.github.fi0x.data.db.SensorRepo;
import io.github.fi0x.data.db.TagRepo;
import io.github.fi0x.data.db.entities.DataEntity;
import io.github.fi0x.data.db.entities.SensorEntity;
import io.github.fi0x.data.db.entities.SensorId;
import io.github.fi0x.data.db.entities.TagEntity;
import io.github.fi0x.data.logic.converter.SensorConverter;
import io.github.fi0x.data.logic.dto.ExpandedSensorDto;
import io.github.fi0x.data.logic.dto.SensorDataDto;
import io.github.fi0x.data.logic.dto.SensorDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorService
{
	private final SensorRepo sensorRepo;
	private final TagRepo tagRepo;
	private final DataRepo dataRepo;

	private final Sensor sensor;

	public long saveSensorAndData(String address, SensorDataDto sensorWithData)
	{
		if (sensor.wasRecentlyUpdated(address, sensorWithData.getName()))
		{
			log.debug("Ignored sensor update, since last update was too recently");
			return -1;
		}

		long timestamp = saveSensorEntity(address, sensorWithData.getName(), sensorWithData.getDescription(),
										  sensorWithData.getUnit(),
						 sensorWithData.getType(), sensorWithData.getDataDelay(), sensorWithData.getValueAdjustment(),
						 sensorWithData.getMinValue(), sensorWithData.getMaxValue());
		saveTags(sensorWithData.getName(), sensorWithData.getTags());

		saveData(address, sensorWithData.getName(), sensorWithData.getValue(), timestamp);

		return timestamp;
	}

	public void saveSensorValueAdjustment(String address, String name, double valueAdjustment)
	{
		SensorEntity entity = sensorRepo.findByAddressAndName(address, name).orElseThrow(
				() -> new ResponseStatusException(HttpStatusCode.valueOf(404),
												  "Requested sensor to update does not exist in the database"));

		entity.setValueAdjustment(valueAdjustment);

		sensorRepo.save(entity);
	}

	public List<SensorDto> getAllSensors()
	{
		List<SensorDto> sensorDtos = sensorRepo.findAll().stream().map(SensorConverter::toDto).toList();

		sensorDtos.forEach(dto -> {
			List<TagEntity> tags = tagRepo.findAllBySensorName(dto.getName());
			dto.setTags(tags.stream().map(TagEntity::getTag).toList());
		});

		return sensorDtos;
	}

	public List<ExpandedSensorDto> getAllDetailedSensors()
	{
		List<ExpandedSensorDto> sensorDtos =
				sensorRepo.findAll().stream().map(SensorConverter::toExpandedDto).toList();

		sensorDtos.forEach(dto -> {
			List<TagEntity> tags = tagRepo.findAllBySensorName(dto.getName());
			dto.setTags(tags.stream().map(TagEntity::getTag).toList());
			if (dto.getValueAdjustment() == null)
				dto.setValueAdjustment(0.0);
		});
		sensorDtos.forEach(dto -> {
			Optional<DataEntity> mostRecentValue = dataRepo.findFirstByAddressAndSensorOrderByTimestampDesc(
					dto.getAddress(), dto.getName());
			if (mostRecentValue.isPresent())
				dto.setValue(mostRecentValue.get().getValue() + dto.getValueAdjustment());
			else
				dto.setValue(0D);
		});
		return sensorDtos;
	}

	public ExpandedSensorDto getDetailedSensor(String address, String name) throws ResponseStatusException
	{
		Optional<ExpandedSensorDto> optionalSensorDto = sensorRepo.findById(new SensorId(address, name))
																  .map(SensorConverter::toExpandedDto);
		ExpandedSensorDto sensorDto = optionalSensorDto.orElseThrow(
				() -> new ResponseStatusException(HttpStatusCode.valueOf(404),
												  "The requested sensor is not registered in the database"));

		sensorDto.setTags(tagRepo.findAllBySensorName(name).stream().map(TagEntity::getTag).toList());
		if (sensorDto.getValueAdjustment() == null)
			sensorDto.setValueAdjustment(0.0);

		Optional<DataEntity> mostRecentValue = dataRepo.findFirstByAddressAndSensorOrderByTimestampDesc(address, name);
		mostRecentValue.ifPresent(
				dataEntity -> sensorDto.setValue(dataEntity.getValue() + sensorDto.getValueAdjustment()));

		return sensorDto;
	}

	public Set<String> getAllSensorTags()
	{
		Set<String> results = sensorRepo.findAll().stream().map(SensorEntity::getType).collect(Collectors.toSet());
		results.addAll(tagRepo.findAll().stream().map(TagEntity::getTag).collect(Collectors.toSet()));
		return results;
	}

	public void deleteSensor(String address, String name)
	{
		SensorEntity entity = sensorRepo.findByAddressAndName(address, name).orElseThrow(
				() -> new ResponseStatusException(HttpStatusCode.valueOf(404),
												  "Could not find requested sensor to delete"));

		sensorRepo.delete(entity);
	}

	private SensorEntity getSensorEntity(String address, String name)
	{
		SensorId id = new SensorId(address, name);
		return sensorRepo.findById(id).orElse(SensorEntity.builder().address(address).name(name).build());
	}

	private long saveSensorEntity(String address, String name, String description, String unit, String type,
								  Long dataDelay, Double valueAdjustment, Double minValue, Double maxValue)
	{
		SensorEntity sensorEntity = getSensorEntity(address, name);

		long timestamp = System.currentTimeMillis();
		sensorEntity.setLastUpdate(timestamp);

		if (description != null)
			sensorEntity.setDescription(description);
		if (unit != null)
			sensorEntity.setUnit(unit);
		if (type != null)
			sensorEntity.setType(type);
		if (dataDelay != null)
			sensorEntity.setDataDelay(dataDelay);
		if (valueAdjustment != null)
			sensorEntity.setValueAdjustment(valueAdjustment);
		if (minValue != null)
			sensorEntity.setMinValue(minValue);
		if (maxValue != null)
			sensorEntity.setMaxValue(maxValue);
		sensorRepo.save(sensorEntity);

		return timestamp;
	}

	private void saveTags(String sensorName, List<String> tags)
	{
		List<TagEntity> existingTags = tagRepo.findAllBySensorName(sensorName);
		existingTags.forEach(tag -> {
			if (!tags.contains(tag.getTag()))
				tagRepo.delete(tag);
		});
		tags.forEach(tag -> {
			TagEntity tagEntity = new TagEntity(sensorName, tag);
			if (!existingTags.contains(tagEntity))
				tagRepo.save(tagEntity);
		});
	}

	private void saveData(String address, String sensorName, Double value, Long timestamp)
	{
		DataEntity dataEntity = DataEntity.builder().address(address).sensor(sensorName).timestamp(timestamp)
										  .value(value).build();
		dataRepo.save(dataEntity);
	}
}
