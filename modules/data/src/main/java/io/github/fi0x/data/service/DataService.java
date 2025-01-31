package io.github.fi0x.data.service;

import io.github.fi0x.data.components.Sensor;
import io.github.fi0x.data.db.DataRepo;
import io.github.fi0x.data.db.entities.DataEntity;
import io.github.fi0x.data.logic.dto.DataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

		if(sensor.wasRecentlyUpdated(address, data.getSensorName()))
		{
			log.debug("Ignored sensor update, since last update was too recently");
			return;
		}

		sensor.updateSensorTimestamp(address, data.getSensorName());

		DataEntity dataEntity =
				DataEntity.builder().address(address).sensor(data.getSensorName()).timestamp(System.currentTimeMillis())
						  .value(data.getValue()).build();
		dataRepo.save(dataEntity);
	}

	public SortedMap<Date, Double> getAllData(String address, String sensorName, Double valueAdjustment)
	{
		List<DataEntity> entities = dataRepo.findAllByAddressAndSensorOrderByTimestampAsc(address, sensorName);
		return getDateValueTreeMap(entities, valueAdjustment).descendingMap();
	}

	public SortedMap<Date, Double> getData(String address, String sensorName, Double valueAdjustment, Integer amount)
	{
		List<DataEntity> entities = dataRepo.findLastXFromSensor(address, sensorName, amount);
		return getDateValueTreeMap(entities, valueAdjustment);
	}

	public SortedMap<Date, Double> getData(String address, String sensorName, Double valueAdjustment, Long oldest)
	{
		List<DataEntity> entities = dataRepo.findFromSensorYoungerThan(address, sensorName, oldest);
		return getDateValueTreeMap(entities, valueAdjustment);
	}

	@Transactional
	public void deleteForSensor(String address, String sensorName, Long timestamp, Double value)
	{
		if (value == null && timestamp == null)
			dataRepo.deleteAllByAddressAndSensor(address, sensorName);
		else if (value == null)
			dataRepo.deleteAllByAddressAndSensorAndTimestamp(address, sensorName, timestamp);
		else if (timestamp == null)
			dataRepo.deleteAllByAddressAndSensorAndValue(address, sensorName, value);
		else
			dataRepo.deleteAllByAddressAndSensorAndTimestampAndValue(address, sensorName, timestamp, value);
	}

	private TreeMap<Date, Double> getDateValueTreeMap(List<DataEntity> entities, Double valueAdjustment)
	{
		if(valueAdjustment != null)
			entities.forEach(entity -> entity.setValue(entity.getValue() + valueAdjustment));

		return new TreeMap<>(entities.stream().collect(
				Collectors.toMap(entity -> new Date(entity.getTimestamp()), DataEntity::getValue)));

	}
}
