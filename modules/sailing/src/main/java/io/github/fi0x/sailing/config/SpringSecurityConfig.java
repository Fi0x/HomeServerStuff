package io.github.fi0x.sailing.config;

import io.github.fi0x.util.config.HomeServerUtilSecurityConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig
{
	private static final String[] PUBLIC_URLS = new String[]{"/", "/orc", "/race"};
	private static final String[] ANONYMOUS_URLS = new String[]{};
	private static final String[] PRIVATE_URLS = new String[]{"/race/manual"};

	@Bean
	@Order(SecurityProperties.BASIC_AUTH_ORDER)
	public SecurityFilterChain securityFilterChain2(HttpSecurity http) throws Exception
	{
		log.debug("securityFilterChain() bean called");

		return HomeServerUtilSecurityConfig.securityFilterChainSetup(http, ArrayUtils.addAll(PUBLIC_URLS,
																							 HomeServerUtilSecurityConfig.PUBLIC_URLS),
																	 ArrayUtils.addAll(ANONYMOUS_URLS,
																					   HomeServerUtilSecurityConfig.ANONYMOUS_URLS),
																	 ArrayUtils.addAll(PRIVATE_URLS,
																					   HomeServerUtilSecurityConfig.PRIVATE_URLS));
	}
}
