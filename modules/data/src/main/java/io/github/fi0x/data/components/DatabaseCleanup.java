package io.github.fi0x.data.components;

import io.github.fi0x.data.db.DataRepo;
import io.github.fi0x.data.db.entities.DataEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseCleanup
{
	@Value("${homeserver.data.max-value-age}")
	private Long maxValueTime;

	private final DataRepo dataRepo;

	@Scheduled(fixedRate = 3600000)
	public void cleanDatabase()
	{
		log.debug("Database cleanup running");

		Long oldestAllowedTime = System.currentTimeMillis() - (maxValueTime * 1000);
		List<DataEntity> entities = dataRepo.findAllValuesOlderThan(oldestAllowedTime);
		dataRepo.deleteAll(entities);
	}
}
