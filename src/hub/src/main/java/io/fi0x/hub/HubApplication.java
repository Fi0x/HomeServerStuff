package io.fi0x.hub;

import io.fi0x.util.config.HomeServerUtilConfig;
import io.fi0x.util.config.UtilLoggingConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@SpringBootApplication
@Import({HomeServerUtilConfig.class, UtilLoggingConfig.class})
public class HubApplication extends SpringBootServletInitializer
{
	public static void main(String[] args)
	{
		log.info("Initial loading complete, starting spring");

		SpringApplication.run(HubApplication.class, args);
	}
}
