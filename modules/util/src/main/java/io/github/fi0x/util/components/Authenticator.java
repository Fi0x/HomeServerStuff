package io.github.fi0x.util.components;

import io.github.fi0x.util.dto.UserRoles;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * A component to handle authenticated users.
 */
@Slf4j
@Component
@AllArgsConstructor
public class Authenticator
{
	private static final String ROLE_PREFIX = "ROLE_";

	/**
	 * Gets the username of the authenticated user within the current context. Returns anonymousUser if no user is
	 * currently authenticated.
	 *
	 * @return The username of the currently authenticated user
	 */
	public String getAuthenticatedUsername()
	{
		log.trace("getAuthenticatedUsername() called");

		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	/**
	 * Gets the roles of the currently authenticated user within the current context. Returns ANONYMOUS if user
	 * is not logged in.
	 *
	 * @return The list of roles for the currently authenticated user
	 */
	public List<UserRoles> getActiveRoles()
	{
		log.trace("getActiveRoles() called");

		return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
									.map(authority -> UserRoles.fromString(
											authority.getAuthority().replaceFirst(ROLE_PREFIX, ""))).toList();
	}

	/**
	 * Checks, if the current user has all requested roles. Throws an AuthorizationDeniedException if at least 1 role
	 * is missing.
	 *
	 * @param requiredRoles The roles the user should have.
	 */
	public void authenticate(UserRoles... requiredRoles)
	{
		List<UserRoles> activeRoles = getActiveRoles();
		for (UserRoles role : requiredRoles)
		{
			if (!activeRoles.contains(role))
				throw new AuthorizationDeniedException("Role missing '" + role + "'");
		}
	}

	/**
	 * Checks, if the current user has all requested roles. Throws an ResponseStatusException if at least 1 role is
	 * missing.
	 *
	 * @param requiredRoles The roles the user should have.
	 */
	public void restAuthenticate(UserRoles... requiredRoles)
	{
		try
		{
			authenticate(requiredRoles);
		} catch (AuthorizationDeniedException e)
		{
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have the required authorization",
											  e);
		}
	}
}
