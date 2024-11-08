package io.fi0x.util.components;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockStatic;

@RunWith(SpringRunner.class)
public class TestAuthenticator
{
	private static final String USERNAME = "Authenticated Username";

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
	}

	@Test
	void test_getAuthenticatedUsername_success()
	{
		MockedStatic<SecurityContextHolder> staticMock = mockStatic(SecurityContextHolder.class);
		staticMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);
		doReturn(authentication).when(securityContext).getAuthentication();
		doReturn(USERNAME).when(authentication).getName();

		Assertions.assertEquals(USERNAME, component.getAuthenticatedUsername());

		staticMock.close();
	}
}
