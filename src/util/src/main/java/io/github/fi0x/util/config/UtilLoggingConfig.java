package io.github.fi0x.util.config;

import io.github.fi0x.util.rest.LoggingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This configuration enables the logging of requests in your service.
 */
@Configuration
public class UtilLoggingConfig
{
	/**
	 * This method creates a logginFilter-bean, which trace-logs every request that is made to the server.
	 *
	 * @return The {@link LoggingFilter} bean
	 */
	@Bean
	public LoggingFilter loggingFilter()
	{
		return new LoggingFilter();
	}
}
