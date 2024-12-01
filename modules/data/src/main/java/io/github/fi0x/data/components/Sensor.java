package io.github.fi0x.data.components;

import io.github.fi0x.data.db.SensorRepo;
import io.github.fi0x.data.db.TagRepo;
import io.github.fi0x.data.db.entities.SensorEntity;
import io.github.fi0x.data.db.entities.TagEntity;
import io.github.fi0x.data.logic.converter.SensorConverter;
import io.github.fi0x.data.logic.dto.SensorDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class Sensor
{
	private final SensorRepo sensorRepo;
	private final TagRepo tagRepo;

	public SensorDto getSensor(String address) throws ResponseStatusException
	{
		SensorEntity entity = sensorRepo.findByAddress(address).orElseThrow(() -> {
			log.warn("Sensor at address '{}' is not registered", address);
			return new ResponseStatusException(HttpStatusCode.valueOf(404),
											   "The address from which data was received, could not be mapped to a registered sensor");
		});

		SensorDto sensorDto = SensorConverter.toDto(entity);
		List<TagEntity> tagEntities = tagRepo.findAllBySensorName(entity.getName());
		sensorDto.setTags(tagEntities.stream().map(TagEntity::getTag).toList());
		return sensorDto;
	}

	public void updateSensorTimestamp(String address)
	{
		SensorEntity entity = sensorRepo.findByAddress(address).orElseThrow(() -> {
			log.warn("No sensor registered under ip '{}'", address);
			return new ResponseStatusException(HttpStatusCode.valueOf(404),
											   "The sensor at ip " + address + " is not registered and can therefore not be updated with a new timestamp");
		});
		entity.setLastUpdate(System.currentTimeMillis());
		sensorRepo.save(entity);
	}
}
