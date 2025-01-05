package io.github.fi0x.data.components;

import io.github.fi0x.data.db.SensorRepo;
import io.github.fi0x.data.db.entities.SensorEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Component
@RequiredArgsConstructor
public class Sensor
{
	private final SensorRepo sensorRepo;

	public void updateSensorTimestamp(String address, String name)
	{
		SensorEntity entity = sensorRepo.findByAddressAndName(address, name).orElseThrow(() -> {
			log.warn("No sensor registered under ip '{}'", address);
			return new ResponseStatusException(HttpStatusCode.valueOf(404),
											   "The sensor at ip " + address + " is not registered and can therefore " + "not be updated with a new timestamp");
		});
		entity.setLastUpdate(System.currentTimeMillis());
		sensorRepo.save(entity);
	}

	public boolean wasRecentlyUpdated(String address, String name)
	{
		SensorEntity existingSensor = sensorRepo.findByAddressAndName(address, name).orElseGet(
				() -> SensorEntity.builder().lastUpdate(0L).dataDelay(0L).build());
		long timeSinceLastUpdate = System.currentTimeMillis() - existingSensor.getLastUpdate();
		return timeSinceLastUpdate < existingSensor.getDataDelay() / 10;
	}
}
