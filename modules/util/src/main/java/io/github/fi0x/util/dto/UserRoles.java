package io.github.fi0x.util.dto;

/**
 * This enum is used to represent authorization-levels for Users.
 */
public enum UserRoles
{
	/**
	 * The role for not logged-in users.
	 */
	ANONYMOUS,
	/**
	 * The role for default users.
	 */
	USER,
	/**
	 * The role for admin users.
	 */
	ADMIN,
	/**
	 * The role that is returned, if the provided role is unknown to this enum.
	 */
	UNKNOWN;

	/**
	 * Converts a {@link String} into a {@link UserRoles}. If the {@link String} does not match any of the enum's
	 * elements, {@link UserRoles}.UNKNOWN will be returned.
	 *
	 * @param roleString The {@link String} that should be matched to a {@link UserRoles}.
	 * @return The {@link UserRoles} that matches the {@link String} input.
	 */
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
