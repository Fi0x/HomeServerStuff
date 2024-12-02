package io.github.fi0x.data.service;

import io.github.fi0x.data.components.Sensor;
import io.github.fi0x.data.db.DataRepo;
import io.github.fi0x.data.db.entities.DataEntity;
import io.github.fi0x.data.logic.dto.DataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

		sensor.updateSensorTimestamp(address, data.getSensorName());

		DataEntity dataEntity =
				DataEntity.builder().address(address).sensor(data.getSensorName()).timestamp(System.currentTimeMillis())
						  .value(data.getValue()).build();
		dataRepo.save(dataEntity);
	}
}
