package io.github.fi0x.util.components;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * A component to handle authenticated users.
 */
@Slf4j
@Component
@AllArgsConstructor
public class Authenticator
{
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
	 * Gets the roles of the currently authenticated user within the current context. Returns ROLE_ANONYMOUS if user
	 * is not logged in.
	 *
	 * @return The list of roles for the currently authenticated user
	 */
	public List<String> getActiveRoles()
	{
		log.trace("getActiveRoles() called");

		return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
									.map(GrantedAuthority::getAuthority).toList();
	}
}
