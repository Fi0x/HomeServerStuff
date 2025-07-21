package io.github.fi0x.util.config;

import io.github.fi0x.util.components.Authenticator;
import io.github.fi0x.util.components.ServiceInformation;
import io.github.fi0x.util.dto.UserRoles;
import io.github.fi0x.util.rest.ErrorController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
	 * This method creates a bean for the {@link ServiceInformation} component.
	 *
	 * @return The {@link ServiceInformation} bean.
	 */
	@Bean
	public ServiceInformation serviceInformation()
	{
		return new ServiceInformation();
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
			createUser(manager, webUser, webPassword, UserRoles.USER.name(), UserRoles.ADMIN.name());
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
