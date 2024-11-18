package io.github.fi0x.data;

import io.github.fi0x.util.config.HomeServerUtilConfig;
import io.github.fi0x.util.config.HubRegisterConfig;
import io.github.fi0x.util.config.UtilLoggingConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@SpringBootApplication
@Import({HomeServerUtilConfig.class, HubRegisterConfig.class, UtilLoggingConfig.class})
public class DataApplication extends SpringBootServletInitializer
{
	public static void main(String[] args)
	{
		log.info("Initial loading complete, starting spring");

		SpringApplication.run(DataApplication.class, args);
	}
}
