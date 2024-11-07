package io.fi0x.util.config;

import io.fi0x.util.components.HubNotifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HubRegisterConfig
{
	@Bean
	public HubNotifier notifier()
	{
		return new HubNotifier();
	}
}
