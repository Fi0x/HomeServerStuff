package io.github.fi0x.data.components;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseCleanup
{
	@Scheduled(fixedRate = 600000)
	public void cleanDatabase()
	{
		log.debug("Database cleanup running");
		//TODO: Remove data from the database, that is too old
	}
}
