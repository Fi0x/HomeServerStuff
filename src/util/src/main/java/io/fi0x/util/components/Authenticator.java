package io.fi0x.util.components;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class Authenticator
{
	//TODO: Maybe this needs to be a service or created as a bean in a config to work in other modules
	public String getAuthenticatedUsername()
	{
		log.trace("getAuthenticatedUsername() called");
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
}
