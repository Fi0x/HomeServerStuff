package io.github.fi0x.data.components;

import io.github.fi0x.data.db.DataRepo;
import io.github.fi0x.data.db.SensorRepo;
import io.github.fi0x.data.db.StatDataRepo;
import io.github.fi0x.data.db.entities.DataEntity;
import io.github.fi0x.data.db.entities.SensorEntity;
import io.github.fi0x.data.db.entities.StatDataEntity;
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

		sensorEntities.forEach(
				sensorEntity -> cleanSensor(sensorEntity.getAddress(), sensorEntity.getName(), oldestAllowedTime));

		cleanSensorlessData(sensorEntities);
	}

	private void cleanSensor(String address, String name, long oldestAllowedTime)
	{
		Optional<DataEntity> oldestDataEntity = dataRepo.findFirstByAddressAndSensorOrderByTimestampAsc(address, name);
		oldestDataEntity.ifPresent(entity -> {
			List<DataEntity> possibleEntities = dataRepo.findFromSensorOlderThan(address, name, oldestAllowedTime);
			//TODO: Average all days, except current one, but only delete data from outside of allowed times
			averageDays(possibleEntities);
		});
	}

	private void averageDays(List<DataEntity> entities)
	{
		if (entities.isEmpty())
			return;

		entities.sort((e1, e2) -> {
			long compare = e2.getTimestamp() - e1.getTimestamp();
			return compare < 0 ? -1 : compare > 0 ? 1 : 0;
		});

		final Date mostRecentDate = new Date(entities.get(0).getTimestamp());
		List<DataEntity> workingEntities = new java.util.ArrayList<>(entities.stream()
																			 .filter(entity -> !DateUtils.isSameDay(
																					 mostRecentDate,
																					 new Date(entity.getTimestamp())))
																			 .toList());

		while (!workingEntities.isEmpty())
		{
			final Date youngestEntryDate = new Date(workingEntities.get(0).getTimestamp());
			List<DataEntity> currentEntities = workingEntities.stream()
															  .filter(entity -> DateUtils.isSameDay(youngestEntryDate,
																									new Date(
																											entity.getTimestamp())))
															  .toList();

			if (!currentEntities.isEmpty())
			{
				StatDataEntity statDataEntity = getAverage(currentEntities, youngestEntryDate);

				if (statDataEntity != null)
				{
					addMinAndMaxValues(statDataEntity, currentEntities);
					statRepo.save(statDataEntity);
				}
				dataRepo.deleteAll(entities);
			}

			workingEntities.removeAll(currentEntities);
		}
	}

	private StatDataEntity getAverage(List<DataEntity> entities, Date desiredDate)
	{
		if (entities.isEmpty())
			return null;

		int count = 0;
		double total = 0;
		for (DataEntity entity : entities)
		{
			count++;
			total += entity.getValue();
		}

		DataEntity averageEntity = new DataEntity(entities.get(0).getAddress(), entities.get(0).getSensor(),
												  desiredDate.getTime(), total / count);

		return StatDataEntity.builder().address(averageEntity.getAddress()).sensor(averageEntity.getSensor())
							 .timestamp(averageEntity.getTimestamp()).average(averageEntity.getValue()).build();
	}

	private void cleanSensorlessData(List<SensorEntity> sensorEntities)
	{
		List<DataEntity> dataEntities = dataRepo.findAll();

		for (SensorEntity sensorEntity : sensorEntities)
			dataEntities =
					dataEntities.stream().filter(dataEntity -> !isSameSensor(sensorEntity, dataEntity)).toList();

		dataRepo.deleteAll(dataEntities);
	}

	private boolean isSameSensor(SensorEntity sensor, DataEntity data)
	{
		return sensor.getAddress().equals(data.getAddress()) && sensor.getName().equals(data.getSensor());
	}

	private void addMinAndMaxValues(StatDataEntity statEntity, List<DataEntity> entities)
	{
		if (entities.isEmpty())
			return;

		Double min = entities.get(0).getValue();
		Double max = entities.get(0).getValue();
		for (DataEntity e : entities)
		{
			if (min > e.getValue())
				min = e.getValue();
			if (max < e.getValue())
				max = e.getValue();
		}

		statEntity.setMin(min);
		statEntity.setMax(max);
	}
}
