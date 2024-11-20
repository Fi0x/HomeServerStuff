package io.github.fi0x.util.config;

import io.github.fi0x.util.components.Authenticator;
import io.github.fi0x.util.rest.ErrorController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

/**
 * This configuration creates a {@link SecurityFilterChain} to allow access to some endpoints. It also creates beans
 * for the {@link ErrorController} and {@link Authenticator}. Additionally, it sets up everything for user management
 * by spring.
 */

@Slf4j
@Configuration
@EnableWebSecurity
public class HomeServerUtilConfig
{
	private static final String[] PUBLIC_URLS = new String[]{"/error", "/WEB-INF/jsp/error.jsp",
			"/webjars/bootstrap" + "/*/css/*", "/webjars/bootstrap/*/js/*", "/webjars/jquery/*/*", "/css/design.css",
			"/images/*"};
	private static final String[] ANONYMOUS_URLS = new String[]{"/custom-login", "/WEB-INF/jsp/login.jsp", "/register"
			, "/WEB-INF/jsp/signup.jsp"};
	@Value("${spring.datasource.url}")
	private String database;
	@Value("${spring.datasource.username}")
	private String dbUsername;
	@Value("${spring.datasource.password}")
	private String dbPassword;
	@Value("${spring.datasource.driver-class-name}")
	private String dbDriver;
	@Value("${homeserver.username:fi0x}")
	private String webUser;
	@Value("${homeserver.password:123}")
	private String webPassword;

	/**
	 * This method creates a bean for the security-filter-chain to allow certain endpoints to be accessible.
	 *
	 * @param http {@link HttpSecurity}
	 * @return The {@link SecurityFilterChain} created from the http parameter
	 * @throws Exception If anything goes wrong in the creation process of the security settings.
	 */
	//TODO: Ensure this is working correctly and not blocking other modules
	@Bean
	@Order(SecurityProperties.BASIC_AUTH_ORDER)
	public SecurityFilterChain utilitySecurityFilterChain(HttpSecurity http) throws Exception
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

	/**
	 * This method allows the creation of a bean for the {@link ErrorController}, so that it can be accessed from
	 * other modules.
	 *
	 * @return The {@link ErrorController}
	 */
	@Bean
	public ErrorController errorController()
	{
		return new ErrorController();
	}

	/**
	 * This method allows the creation of a bean for the {@link Authenticator}, so that it can be accessed from
	 * other modules.
	 *
	 * @return The {@link Authenticator}
	 */
	@Bean
	public Authenticator authenticator()
	{
		return new Authenticator();
	}

	/**
	 * This method creates a dataSource-bean, to enable database-access.
	 *
	 * @return The {@link DataSource}
	 */
	@Bean
	public DataSource dataSource()
	{
		log.debug("dataSource() bean called");

		DataSourceBuilder<?> builder = DataSourceBuilder.create();

		builder.driverClassName(dbDriver);
		builder.url(database);
		builder.username(dbUsername);
		builder.password(dbPassword);

		return builder.build();
	}

	/**
	 * This method creates a userDetailsManager-bean, so that users can be handled by spring.
	 *
	 * @param dataSource The {@link DataSource}, where users are stored.
	 * @return The {@link UserDetailsManager}
	 */
	@Bean
	public UserDetailsManager userDetailsManager(DataSource dataSource)
	{
		log.debug("userDetailsManager() bean called");

		JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

		if (!manager.userExists(webUser))
		{
			createUser(manager, webUser, webPassword, "USER", "ADMIN");
			log.info("User '{}' created as default admin", webUser);
		}

		return manager;
	}

	/**
	 * This method creates a passwordEncoder-bean to encrypt user-passwords.
	 *
	 * @return The {@link PasswordEncoder}
	 */
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}

	private void createUser(JdbcUserDetailsManager userManager, String username, String password, String... roles)
	{
		userManager.createUser(
				User.builder().passwordEncoder(input -> passwordEncoder().encode(input)).username(username)
					.password(password).roles(roles).build());
	}
}
