package io.github.fi0x.util.dto;

public enum UserRoles
{
	ANONYMOUS, USER, ADMIN, UNKNOWN;

	public static UserRoles fromString(String roleString)
	{
		try
		{
			return UserRoles.valueOf(roleString);
		} catch (Exception e)
		{
			return UNKNOWN;
		}
	}
}
