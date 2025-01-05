package io.github.fi0x.data.service;

import io.github.fi0x.data.components.Sensor;
import io.github.fi0x.data.db.DataRepo;
import io.github.fi0x.data.db.entities.DataEntity;
import io.github.fi0x.data.logic.dto.DataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataService
{
	private final DataRepo dataRepo;

	private final Sensor sensor;

	public void addData(String address, DataDto data) throws ResponseStatusException
	{
		log.trace("addData() called from address {}", address);

		if (sensor.wasRecentlyUpdated(address, data.getSensorName()))
		{
			log.debug("Ignored sensor update, since last update was too recently");
			return;
		}

		sensor.updateSensorTimestamp(address, data.getSensorName());

		DataEntity dataEntity = DataEntity.builder().address(address).sensor(data.getSensorName())
										  .timestamp(System.currentTimeMillis()).value(data.getValue()).build();
		dataRepo.save(dataEntity);
	}

	public SortedMap<Date, Double> getAllData(String address, String sensorName)
	{
		List<DataEntity> entities = dataRepo.findAllByAddressAndSensorOrderByTimestampAsc(address, sensorName);

		TreeMap<Date, Double> map = new TreeMap<>(entities.stream().collect(
				Collectors.toMap(entity -> new Date(entity.getTimestamp()), DataEntity::getValue)));

		return map.descendingMap();
	}
}
