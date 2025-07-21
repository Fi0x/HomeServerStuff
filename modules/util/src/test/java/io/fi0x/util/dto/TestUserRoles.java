package io.fi0x.util.dto;

import io.github.fi0x.util.dto.UserRoles;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestUserRoles
{
	private static final String ANONYMOUS = "ANONYMOUS";
	private static final String USER = "USER";
	private static final String ADMIN = "ADMIN";
	private static final String UNKNOWN = "UNKNOWN";
	private static final String BLA = "BLA";

	@Test
	public void test_fromString()
	{
		Assertions.assertEquals(UserRoles.ANONYMOUS, UserRoles.fromString(ANONYMOUS));
		Assertions.assertEquals(UserRoles.USER, UserRoles.fromString(USER));
		Assertions.assertEquals(UserRoles.ADMIN, UserRoles.fromString(ADMIN));
		Assertions.assertEquals(UserRoles.UNKNOWN, UserRoles.fromString(UNKNOWN));
		Assertions.assertEquals(UserRoles.UNKNOWN, UserRoles.fromString(BLA));
	}
}
