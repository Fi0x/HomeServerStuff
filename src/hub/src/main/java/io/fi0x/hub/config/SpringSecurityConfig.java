package io.fi0x.hub.config;

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
	private static final String[] PUBLIC_URLS = new String[]{"/", "/*", "/WEB-INF/jsp/main-page.jsp", "/recipe/*",
			"/WEB-INF/jsp/show-recipe.jsp", "/recipe/*/*", "WEB-INF/jsp/edit-recipe", "/recipes", "/WEB-INF/jsp" +
			"/recipes.jsp", "/error", "/WEB-INF/jsp/error.jsp", "/webjars/bootstrap/*/css/*", "/webjars/bootstrap/*/js"
			+ "/*", "/webjars/jquery/*/*", "/css/design.css", "/images/*", "/api/service/register"};
	private static final String[] ANONYMOUS_URLS = new String[]{"/register", "/WEB-INF/jsp/signup.jsp", "/custom-login"
			, "/WEB-INF/jsp/login.jsp"};

	@Bean
	@Order(SecurityProperties.BASIC_AUTH_ORDER)
	public SecurityFilterChain securityFilterChain2(HttpSecurity http) throws Exception
	{
		log.debug("securityFilterChain() bean called");

		http.authorizeHttpRequests(auth -> {
			auth.requestMatchers(PUBLIC_URLS).permitAll();
			auth.requestMatchers(ANONYMOUS_URLS).anonymous();
			auth.anyRequest().authenticated();
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
