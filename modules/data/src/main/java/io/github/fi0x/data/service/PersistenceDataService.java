package io.github.fi0x.data.service;

import io.github.fi0x.data.db.DataRepo;
import io.github.fi0x.data.db.entities.DataEntity;
import io.github.fi0x.data.db.entities.SensorEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersistenceDataService
{
	private final DataRepo dataRepo;

	public void saveData(String address, String sensorName, Double value, Long timestamp)
	{
		DataEntity dataEntity = DataEntity.builder().address(address).sensor(sensorName).timestamp(timestamp)
				.value(value).build();
		dataRepo.save(dataEntity);
	}

	public DataEntity getMostRecentEntry(SensorEntity sensor)
	{
		return dataRepo.findFirstByAddressAndSensorOrderByTimestampDesc(sensor.getAddress(), sensor.getName())
				.orElse(null);
	}
}
