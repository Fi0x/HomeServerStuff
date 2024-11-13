package io.fi0x.util.config;

import io.fi0x.util.components.Authenticator;
import io.fi0x.util.rest.UserController;
import io.fi0x.util.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

/**
 * This configuration can be imported to enable the endpoints of the {@link UserController}, which inclued login and
 * register in spring-security.
 */
@Slf4j
@Configuration
public class LoginConfig
{
	/**
	 * This method creates a userController-bean, that will provide endpoints for login and registration.
	 *
	 * @param authenticationService The {@link AuthenticationService} that handles all authentication requests.
	 * @param authenticator         The authenticator that is used to access usernames.
	 * @return The {@link UserController} bean with the enabled endpoints.
	 */
	@Bean
	public UserController userController(AuthenticationService authenticationService, Authenticator authenticator)
	{
		return new UserController(authenticationService, authenticator);
	}

	/**
	 * This method provides an authenticationService-bean that is required by the userController-bean to register and
	 * login users.
	 *
	 * @param userDetailsManager The userDetailsManager-bean for this service.
	 * @param passwordEncoder    The passwordEncoder-bean for this service.
	 * @return The {@link AuthenticationService} bean
	 */
	@Bean
	public AuthenticationService authenticationService(UserDetailsManager userDetailsManager,
													   PasswordEncoder passwordEncoder)
	{
		return new AuthenticationService(userDetailsManager, passwordEncoder);
	}
}
