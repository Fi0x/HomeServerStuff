package io.github.fi0x.data.components;

import io.github.fi0x.data.db.DataRepo;
import io.github.fi0x.data.db.SensorRepo;
import io.github.fi0x.data.db.StatDataRepo;
import io.github.fi0x.data.db.entities.DataEntity;
import io.github.fi0x.data.db.entities.SensorEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseCleanup
{
	@Value("${homeserver.data.max-value-age}")
	private Long maxValueTime;

	private final DataRepo dataRepo;
	private final StatDataRepo statRepo;
	private final SensorRepo sensorRepo;

	@Scheduled(fixedRate = 3600000)
	public void cleanDatabase()
	{
		log.debug("Database cleanup running");

		List<SensorEntity> sensorEntities = sensorRepo.findAll();
		long oldestAllowedTime = System.currentTimeMillis() - (maxValueTime * 1000);

		//TODO: Get min, max and avg and write them into statRepo
		//TODO: Remove the calculated entries from dataRepo
		//TODO: Copy older entries from dataRepo to statRepo

		sensorEntities.forEach(
				sensorEntity -> cleanSensor(sensorEntity.getAddress(), sensorEntity.getName(), oldestAllowedTime));

		cleanSensorlessData(sensorEntities);
	}

	private void cleanSensor(String address, String name, long oldestAllowedTime)
	{
		Optional<DataEntity> oldestDataEntity = dataRepo.findFirstByAddressAndSensorOrderByTimestampAsc(address, name);
		oldestDataEntity.ifPresent(entity -> {
			List<DataEntity> possibleEntities = dataRepo.findFromSensorOlderThan(address, name, oldestAllowedTime);

			averageDays(possibleEntities);
		});
	}

	private void averageDays(List<DataEntity> entities)
	{
		if(entities.isEmpty())
			return;

		entities.sort((e1, e2) -> Math.toIntExact((e2.getTimestamp() - e1.getTimestamp())));

		final Date mostRecentDate = new Date(entities.get(0).getTimestamp());
		List<DataEntity> workingEntities = new java.util.ArrayList<>(entities.stream()
																			 .filter(entity -> !DateUtils.isSameDay(
																					 mostRecentDate,
																					 new Date(entity.getTimestamp())))
																			 .toList());

		while(!workingEntities.isEmpty())
		{
			final Date youngestEntryDate = new Date(workingEntities.get(0).getTimestamp());
			List<DataEntity> currentEntities = workingEntities.stream()
															  .filter(entity -> DateUtils.isSameDay(youngestEntryDate,
																									new Date(
																											entity.getTimestamp())))
															  .toList();

			if(currentEntities.size() > 1)
				saveAverage(currentEntities, youngestEntryDate);

			workingEntities.removeAll(currentEntities);
		}
	}

	private void saveAverage(List<DataEntity> entities, Date desiredDate)
	{
		if(entities.isEmpty())
			return;

		int count = 0;
		double total = 0;
		for(DataEntity entity : entities)
		{
			count++;
			total += entity.getValue();
		}

		DataEntity averageEntity =
				new DataEntity(entities.get(0).getAddress(), entities.get(0).getSensor(), desiredDate.getTime(),
							   total / count);

		dataRepo.deleteAll(entities);
		dataRepo.save(averageEntity);
	}

	private void cleanSensorlessData(List<SensorEntity> sensorEntities)
	{
		List<DataEntity> dataEntities = dataRepo.findAll();

		for(SensorEntity sensorEntity : sensorEntities)
			dataEntities = dataEntities.stream().filter(dataEntity -> !isSameSensor(sensorEntity, dataEntity)).toList();

		dataRepo.deleteAll(dataEntities);
	}

	private boolean isSameSensor(SensorEntity sensor, DataEntity data)
	{
		return sensor.getAddress().equals(data.getAddress()) && sensor.getName().equals(data.getSensor());
	}
}
