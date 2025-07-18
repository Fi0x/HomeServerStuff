package io.github.fi0x.util.service;

import io.github.fi0x.util.dto.UserDto;
import io.github.fi0x.util.dto.UserRoles;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

/**
 * This service allows a module to register new users
 */
@Slf4j
@Service
@AllArgsConstructor
public class AuthenticationService
{
	private final UserDetailsManager userDetailsManager;
	private final PasswordEncoder passwordEncoder;

	/**
	 * This method registers a new user in the database. The user can be accessed by spring-security.
	 *
	 * @param userDto The dto of the new user.
	 * @throws DuplicateKeyException If the user already exists.
	 */
	public void registerUser(UserDto userDto) throws DuplicateKeyException
	{
		log.trace("registerUser() called with userDto={}", userDto);

		if (userDetailsManager.userExists(userDto.getUsername()))
			throw new DuplicateKeyException("A user with that username already exists");

		userDetailsManager.createUser(
				User.builder().passwordEncoder(passwordEncoder::encode).username(userDto.getUsername())
					.password(userDto.getPassword()).roles(UserRoles.USER.name()).build());
	}
}
