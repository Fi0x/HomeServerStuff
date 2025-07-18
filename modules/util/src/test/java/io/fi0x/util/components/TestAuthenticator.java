package io.fi0x.util.components;

import io.github.fi0x.util.components.Authenticator;
import io.github.fi0x.util.dto.UserRoles;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockStatic;

@RunWith(SpringRunner.class)
public class TestAuthenticator
{
	private static final String USERNAME = "Authenticated Username";
	private MockedStatic<SecurityContextHolder> staticMock;

	@Mock
	private Authentication authentication;
	@Mock
	private SecurityContext securityContext;

	@InjectMocks
	private Authenticator component;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);

		staticMock = mockStatic(SecurityContextHolder.class);
		staticMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);
		doReturn(authentication).when(securityContext).getAuthentication();
	}

	@AfterEach
	void cleanup()
	{
		staticMock.close();
	}

	@Test
	void test_getAuthenticatedUsername_success()
	{
		doReturn(USERNAME).when(authentication).getName();

		Assertions.assertEquals(USERNAME, component.getAuthenticatedUsername());
	}

	@Test
	void test_getActiveRoles_success()
	{
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
		doReturn(List.of(authority)).when(authentication).getAuthorities();

		Assertions.assertEquals(List.of(UserRoles.ADMIN), component.getActiveRoles());
	}

	@Test
	void test_authenticate_success()
	{
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
		doReturn(List.of(authority)).when(authentication).getAuthorities();

		Assertions.assertDoesNotThrow(() -> component.authenticate(UserRoles.ADMIN));
	}

	@Test
	void test_authenticate_missing()
	{
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
		doReturn(List.of(authority)).when(authentication).getAuthorities();

		Assertions.assertThrows(AuthorizationDeniedException.class, () -> component.authenticate(UserRoles.USER));
		Assertions.assertThrows(AuthorizationDeniedException.class,
								() -> component.authenticate(UserRoles.ADMIN, UserRoles.USER));
	}

	@Test
	void test_restAuthenticate_success()
	{
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
		doReturn(List.of(authority)).when(authentication).getAuthorities();

		Assertions.assertDoesNotThrow(() -> component.restAuthenticate(UserRoles.ADMIN));
	}

	@Test
	void test_restAuthenticate_missing()
	{
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
		doReturn(List.of(authority)).when(authentication).getAuthorities();

		Assertions.assertThrows(ResponseStatusException.class, () -> component.restAuthenticate(UserRoles.USER));
		Assertions.assertThrows(ResponseStatusException.class,
								() -> component.restAuthenticate(UserRoles.ADMIN, UserRoles.USER));
	}
}
