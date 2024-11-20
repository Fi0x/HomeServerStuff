package io.github.fi0x.util.config;

import io.github.fi0x.util.components.HubNotifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This configuration can be imported by other modules to enable the scheduled task, that notifies a hub-module about
 * the existence of this service.
 */
@Configuration
public class HubRegisterConfig
{
	/**
	 * This method creates a {@link HubNotifier} bean that will start a scheduled task, if
	 * {@link org.springframework.scheduling.annotation.EnableScheduling} is active.
	 *
	 * @return The {@link HubNotifier}
	 */
	@Bean
	public HubNotifier notifier()
	{
		return new HubNotifier();
	}
}
