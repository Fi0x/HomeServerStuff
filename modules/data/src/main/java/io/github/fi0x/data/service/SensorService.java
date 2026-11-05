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
import io.github.fi0x.data.logic.dto.SensorType;
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

	private final CalculatedSensorService calculatedSensorService;
	private final PersistenceSensorService persistenceSensorService;
	private final PersistenceDataService persistenceDataService;

	private final Sensor sensor;

	public long saveSensorAndData(String address, SensorDataDto sensorWithData)
	{
		if (sensor.wasRecentlyUpdated(address, sensorWithData.getName()))
		{
			log.debug("Ignored sensor update, since last update was too recently");
			return -1;
		}

		SensorEntity sensorEntity = persistenceSensorService.saveIfNew(address, sensorWithData.getName(),
				sensorWithData.getDescription(), sensorWithData.getUnit(), sensorWithData.getType(),
				sensorWithData.getDataDelay(), sensorWithData.getValueAdjustment(), sensorWithData.getMinValue(),
				sensorWithData.getMaxValue());
		saveTags(sensorWithData.getName(), sensorWithData.getTags());

		persistenceDataService.saveData(address, sensorWithData.getName(), sensorWithData.getValue(),
				sensorEntity.getLastUpdate());

		if (sensorEntity.getVPDSensorName() != null)
		{
			calculatedSensorService.calculateAndSaveSensorDataFor(sensorEntity.getAddress(),
					sensorEntity.getVPDSensorName(), SensorType.VPD);
		}

		return sensorEntity.getLastUpdate();
	}

	public void saveSensorValueAdjustment(String address, String name, String displayName, Double valueAdjustment,
										  Double minValue, Double maxValue)
	{
		SensorEntity entity = sensorRepo.findByAddressAndName(address, name).orElseThrow(
				() -> new ResponseStatusException(HttpStatusCode.valueOf(404),
						"Requested sensor to update does not exist in the database"));

		if (displayName != null)
			entity.setDisplayName(displayName);
		if (valueAdjustment != null)
			entity.setValueAdjustment(valueAdjustment);
		if (minValue != null)
			entity.setMinValue(minValue);
		if (maxValue != null)
			entity.setMaxValue(maxValue);

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

	public ExpandedSensorDto createSensor(String address, String name, SensorType type)
	{
		if (persistenceSensorService.findSensorEntity(address, name).isPresent())
			throw new ResponseStatusException(HttpStatusCode.valueOf(400),
					"The sensor could not be created, because it already exists");

		Long dataDelay = null;
		if (SensorType.VPD.equals(type))
		{
			List<SensorEntity> inputSensors = persistenceSensorService.getAllSensors(address).stream()
					.filter(e -> SensorType.HUMIDITY.is(e.getType()) || SensorType.TEMPERATURE.is(e.getType()))
					.toList();
			if (inputSensors.size() != 2)
				throw new ResponseStatusException(HttpStatusCode.valueOf(404),
						"Could not find the 2 related humidity and temperature sensors");
			if (!inputSensors.get(0).getDataDelay().equals(inputSensors.get(1).getDataDelay()))
				throw new ResponseStatusException(HttpStatusCode.valueOf(400),
						"Could not create VPD sensor, because the 2 input sensors have different data-delays");
			dataDelay = inputSensors.get(0).getDataDelay();

			persistenceSensorService.addVPDSensor(address, inputSensors.get(0).getName(), name);
			persistenceSensorService.addVPDSensor(address, inputSensors.get(1).getName(), name);
		}
		return SensorConverter.toExpandedDto(
				persistenceSensorService.saveNewSensor(address, name, type.toString(), dataDelay));
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
}
