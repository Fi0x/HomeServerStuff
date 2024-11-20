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
	private static final String[] PUBLIC_URLS =
			new String[]{"/error", "/WEB-INF/jsp/error.jsp", "/webjars/bootstrap/*/css/*", "/webjars/bootstrap/*/js/*",
					"/webjars/jquery/*/*", "/css/design.css", "/images/*"};
	private static final String[] ANONYMOUS_URLS =
			new String[]{"/custom-login", "/WEB-INF/jsp/login.jsp", "/register", "/WEB-INF/jsp/signup.jsp"};
	private static final String[] PRIVATE_URLS = new String[]{};

	@Value("${spring.datasource.url}")
	private String database;
	@Value("${spring.datasource.username}")
	private String dbUsername;
	@Value("${spring.datasource.password}")
	private String dbPassword;
	@Value("${spring.datasource.driver-class-name}")
	private String dbDriver;
	@Value("${homeserver.username}")
	private String webUser;
	@Value("${homeserver.password}")
	private String webPassword;

	/**
	 * This method allows a user to adjust the security-filter-chain by adding private, public and anonymous urls
	 *
	 * @param http          The {@link HttpSecurity} object
	 * @param publicUrls    A list of urls that should always be accessible
	 * @param anonymousUrls A list of urls that should only be accessible to users, that are not logged in
	 * @param privateUrls   A list of urls that should only be accessible for logged-in users
	 * @return The {@link SecurityFilterChain} object
	 * @throws Exception If anything goes wrong during setup
	 */
	public static SecurityFilterChain securityFilterChainSetup(HttpSecurity http, String[] publicUrls,
															   String[] anonymousUrls,
															   String[] privateUrls) throws Exception
	{
		http.authorizeHttpRequests(auth -> {
			auth.requestMatchers(publicUrls).permitAll();
			auth.requestMatchers(anonymousUrls).anonymous();
			auth.requestMatchers(privateUrls).authenticated();
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

	/**
	 * This method creates a bean for the security-filter-chain to allow certain endpoints to be accessible.
	 *
	 * @param http {@link HttpSecurity}
	 * @return The {@link SecurityFilterChain} created from the http parameter
	 * @throws Exception If anything goes wrong in the creation process of the security settings.
	 */
	@Bean
	@Order(SecurityProperties.BASIC_AUTH_ORDER)
	public SecurityFilterChain utilitySecurityFilterChain(HttpSecurity http) throws Exception
	{
		log.info("utilitySecurityFilterChain() bean called");

		return securityFilterChainSetup(http, PUBLIC_URLS, ANONYMOUS_URLS, PRIVATE_URLS);
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

		if(!manager.userExists(webUser))
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
