package io.fi0x.util.config;

import io.fi0x.util.rest.LoggingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilLoggingConfig
{
	@Bean
	public LoggingFilter loggingFilter()
	{
		return new LoggingFilter();
	}
}
