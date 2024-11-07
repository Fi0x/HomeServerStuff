package io.fi0x.util.config;

import io.fi0x.util.components.Authenticator;
import io.fi0x.util.rest.UserController;
import io.fi0x.util.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

@Slf4j
@Configuration
public class LoginConfig
{
	@Bean
	public UserController userController(AuthenticationService authenticationService, Authenticator authenticator)
	{
		return new UserController(authenticationService, authenticator);
	}

	@Bean
	public AuthenticationService authenticationService(UserDetailsManager userDetailsManager,
													   PasswordEncoder passwordEncoder)
	{
		return new AuthenticationService(userDetailsManager, passwordEncoder);
	}
}
