package io.fi0x.util.service;

import io.github.fi0x.util.dto.UserDto;
import io.github.fi0x.util.service.AuthenticationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TestAuthenticationService
{
	private static final String USERNAME = "Fridolin";
	private static final String PASSWORD = "password123";

	@Mock
	private UserDetailsManager userDetailsManager;
	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private AuthenticationService service;


	@Test
	void test_registerUser_success()
	{
		when(passwordEncoder.encode(anyString())).thenReturn("");

		UserDto dto = new UserDto();
		dto.setUsername(USERNAME);
		dto.setPassword(PASSWORD);

		Assertions.assertDoesNotThrow(() -> service.registerUser(dto));
	}

	@Test
	void test_registerUser_duplicate()
	{
		when(userDetailsManager.userExists(anyString())).thenReturn(true);

		UserDto dto = new UserDto();
		dto.setUsername(USERNAME);
		dto.setPassword(PASSWORD);

		Assertions.assertThrows(DuplicateKeyException.class, () -> service.registerUser(dto));
	}
}
