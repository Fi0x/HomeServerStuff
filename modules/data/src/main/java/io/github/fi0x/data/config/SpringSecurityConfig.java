package io.github.fi0x.data.config;

import io.github.fi0x.data.components.DatabaseCleanup;
import io.github.fi0x.data.db.DataRepo;
import io.github.fi0x.data.db.SensorRepo;
import io.github.fi0x.data.db.StatDataRepo;
import io.github.fi0x.util.config.HomeServerUtilConfig;
import lombok.extern.slf4j.Slf4j;
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
	private static final String[] PUBLIC_URLS = new String[]{"/", "/api/*", "/api/data/*"};
	private static final String[] ANONYMOUS_URLS = new String[]{};
	private static final String[] PRIVATE_URLS = new String[]{"/sensor/*/*/edit", "/sensor/*/*/update"};

	@Bean
	@Order(SecurityProperties.BASIC_AUTH_ORDER)
	public SecurityFilterChain securityFilterChain2(HttpSecurity http) throws Exception
	{
		log.debug("securityFilterChain() bean called");

		return HomeServerUtilConfig.securityFilterChainSetup(http, PUBLIC_URLS, ANONYMOUS_URLS, PRIVATE_URLS);
	}

	@Bean
	public DatabaseCleanup databaseCleanup(DataRepo dataRepo, StatDataRepo statRepo, SensorRepo sensorRepo)
	{
		return new DatabaseCleanup(dataRepo, statRepo, sensorRepo);
	}
}
