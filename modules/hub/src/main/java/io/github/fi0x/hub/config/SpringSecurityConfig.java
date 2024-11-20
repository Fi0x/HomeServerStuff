package io.github.fi0x.hub.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig
{
	private static final String[] PUBLIC_URLS = new String[]{"/", "/*", "/WEB-INF/jsp/main-page.jsp"};

	@Bean
	@Order(SecurityProperties.BASIC_AUTH_ORDER)
	public SecurityFilterChain hubSecurityFilterChain(HttpSecurity http) throws Exception
	{
		log.info("hubSecurityFilterChain() bean called");

		http.authorizeHttpRequests(auth -> {
			auth.requestMatchers(PUBLIC_URLS).permitAll();
			auth.anyRequest().permitAll();
		});

		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

		http.formLogin(form -> {
			form.loginPage("/custom-login");
			form.loginProcessingUrl("/login");
			form.defaultSuccessUrl("/", true);
			form.permitAll();
		});

		http.csrf(AbstractHttpConfigurer::disable);
		http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

		return http.build();
	}
}
