package io.fi0x.homeserver.general.components;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class Authenticator
{
	public String getAuthenticatedUsername()
	{
		log.trace("getAuthenticatedUsername() called");
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
}
