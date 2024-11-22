package io.github.fi0x.util.config;

import io.github.fi0x.util.components.HubNotifier;
import io.github.fi0x.util.components.ServiceInformation;
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
	 * @param serviceInformation The serviceInformation-bean of this service
	 * @return The {@link HubNotifier}
	 */
	@Bean
	public HubNotifier notifier(ServiceInformation serviceInformation)
	{
		return new HubNotifier(serviceInformation);
	}
}
