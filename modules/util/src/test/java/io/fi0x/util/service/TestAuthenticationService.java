package io.fi0x.util.service;

import io.github.fi0x.util.dto.UserDto;
import io.github.fi0x.util.service.AuthenticationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
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

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);
	}

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
