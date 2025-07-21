package io.fi0x.util.rest.validation;

import io.github.fi0x.util.dto.UserDto;
import io.github.fi0x.util.rest.validation.PasswordMatchValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;


public class TestPasswordMatchValidator
{
	private static final String USERNAME = "Obelix";
	private static final String PASSWORD = "Idefix";
	private static final String WRONG_PW = "Asterix";

	@InjectMocks
	private PasswordMatchValidator testClass = new PasswordMatchValidator();

	@Test
	void test_initialize()
	{
		Assertions.assertDoesNotThrow(() -> testClass.initialize(null));
	}

	@Test
	void test_isValid_success()
	{
		Assertions.assertTrue(testClass.isValid(getValidUserDto(), null));
	}

	@Test
	void test_isValid_wrongObject()
	{
		Assertions.assertThrows(ClassCastException.class, () -> testClass.isValid(new Object(), null));
	}

	@Test
	void test_isValid_noMatch()
	{
		UserDto dto = getValidUserDto();
		dto.setMatchingPassword(WRONG_PW);
		Assertions.assertFalse(testClass.isValid(dto, null));
	}

	private UserDto getValidUserDto()
	{
		UserDto dto = new UserDto();

		dto.setUsername(USERNAME);
		dto.setPassword(PASSWORD);
		dto.setMatchingPassword(PASSWORD);

		return dto;
	}
}
