package io.fi0x.util.rest;

import io.github.fi0x.util.components.Authenticator;
import io.github.fi0x.util.dto.LoginDto;
import io.github.fi0x.util.dto.ServiceDataDto;
import io.github.fi0x.util.dto.ServiceInfoDto;
import io.github.fi0x.util.dto.UserDto;
import io.github.fi0x.util.rest.ApiController;
import io.github.fi0x.util.rest.ErrorController;
import io.github.fi0x.util.rest.LoggingFilter;
import io.github.fi0x.util.rest.UserController;
import io.github.fi0x.util.service.AuthenticationService;
import io.github.fi0x.util.service.InformationService;
import io.github.fi0x.util.service.RequestService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class TestControllers
{
	private static final ServiceDataDto SERVICE_DATA_DTO_RESULT = new ServiceDataDto("Name", "Protocol", "IP", 42);
	private static final String GITHUB_URL = "fi0x.github";
	private static final String HUB_URL = "hub.1234";
	private static final ServiceInfoDto SERVICE_INFO_DTO_RESULT =
			new ServiceInfoDto("Name", "Protocol", "IP", 42, false, false);
	private static final byte[] BYTE_RESULT = new byte[]{3, 2, 5, 34, 10, 6};
	private static final String USERNAME = "Miraculix";
	private static final String ERROR = "error";
	private static final Integer ERROR_CODE = 444;
	private static final String ERROR_MSG = "Mayday";
	private static final String PASSWORD = "Safe";

	@Mock
	private RequestService requestService;
	@Mock
	private InformationService informationService;
	@Mock
	private Authenticator authenticator;
	@Mock
	private HttpServletRequest httpServletRequest;
	@Mock
	private FilterChain filterChain;
	@Mock
	private ServletRequest servletRequest;
	@Mock
	private HttpSession session;
	@Mock
	private AuthenticationService authenticationService;

	@InjectMocks
	private ApiController api;
	@InjectMocks
	private ErrorController error;
	@InjectMocks
	private LoggingFilter filter;
	@InjectMocks
	private UserController user;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void test_api_getRegisteredServices()
	{
		when(requestService.getRegisteredServicesFromHub()).thenReturn(List.of(SERVICE_DATA_DTO_RESULT));

		Assertions.assertEquals(List.of(SERVICE_DATA_DTO_RESULT), api.getRegisteredServices());
	}

	@Test
	void test_api_redirectToGithubUrl()
	{
		when(requestService.getGithubUrl()).thenReturn(GITHUB_URL);
		ModelAndView expected = new ModelAndView("redirect:" + GITHUB_URL);

		Assertions.assertEquals(expected.getView(), api.redirectToGithubUrl().getView());
	}

	@Test
	void test_api_redirectToHubUrl()
	{
		when(requestService.getHubUrl()).thenReturn(HUB_URL);
		ModelAndView expected = new ModelAndView("redirect:" + HUB_URL);

		Assertions.assertEquals(expected.getView(), api.redirectToHubUrl().getView());
	}

	@Test
	void test_api_getServiceInfo()
	{
		when(informationService.getServiceInformation()).thenReturn(SERVICE_INFO_DTO_RESULT);

		Assertions.assertEquals(SERVICE_INFO_DTO_RESULT, api.getServiceInfo());
	}

	@Test
	void test_api_getServiceLogo()
	{
		when(informationService.getServiceLogo()).thenReturn(BYTE_RESULT);

		Assertions.assertEquals(BYTE_RESULT, api.getServiceLogo());
	}

	@Test
	void test_api_getCurrentUsername()
	{
		when(authenticator.getAuthenticatedUsername()).thenReturn(USERNAME);

		Assertions.assertEquals(LoginDto.builder().username(USERNAME).build(), api.getCurrentUsername());

	}

	@Test
	void test_error_showError()
	{
		when(httpServletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(ERROR_CODE);
		when(httpServletRequest.getAttribute(RequestDispatcher.ERROR_MESSAGE)).thenReturn(ERROR_MSG);
		ModelMap map = new ModelMap();

		Assertions.assertEquals(ERROR, error.showError(map, httpServletRequest));
		Assertions.assertEquals(ERROR_CODE, map.get("errorCode"));
		Assertions.assertEquals(ERROR_MSG, map.get("errorMessage"));
	}

	@Test
	void test_error_showPostError()
	{
		when(httpServletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(ERROR_CODE);
		when(httpServletRequest.getAttribute(RequestDispatcher.ERROR_MESSAGE)).thenReturn(ERROR_MSG);
		ModelMap map = new ModelMap();

		Assertions.assertEquals(ERROR, error.showPostError(map, httpServletRequest));
		Assertions.assertEquals(ERROR_CODE, map.get("errorCode"));
		Assertions.assertEquals(ERROR_MSG, map.get("errorMessage"));
	}

	@Test
	void test_error_showDeleteError()
	{
		when(httpServletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(ERROR_CODE);
		when(httpServletRequest.getAttribute(RequestDispatcher.ERROR_MESSAGE)).thenReturn(ERROR_MSG);
		ModelMap map = new ModelMap();

		Assertions.assertEquals(ERROR, error.showDeleteError(map, httpServletRequest));
		Assertions.assertEquals(ERROR_CODE, map.get("errorCode"));
		Assertions.assertEquals(ERROR_MSG, map.get("errorMessage"));
	}

	@Test
	void test_filter_doFilter()
	{
		Assertions.assertDoesNotThrow(() -> filter.doFilter(httpServletRequest, null, filterChain));

	}

	@Test
	void test_filter_doFilter_npe()
	{
		Assertions.assertThrows(NullPointerException.class, () -> filter.doFilter(null, null, filterChain));
	}

	@Test
	void test_filter_doFilter_cce()
	{
		Assertions.assertThrows(ClassCastException.class, () -> filter.doFilter(servletRequest, null, filterChain));
	}

	@Test
	void test_user_showLogin()
	{
		when(authenticator.getAuthenticatedUsername()).thenReturn(USERNAME);
		ModelMap map = new ModelMap();

		Assertions.assertEquals("login", user.showLogin(map));
		Assertions.assertEquals(USERNAME, map.get("username"));
		Assertions.assertEquals(new LoginDto(), map.get("loginDto"));
	}

	@Test
	void test_user_showRegister()
	{
		when(httpServletRequest.getSession()).thenReturn(session);
		ModelMap map = new ModelMap();

		Assertions.assertEquals("signup", user.showRegister(map, httpServletRequest));
		Assertions.assertEquals(new UserDto(), map.get("userDto"));
	}

	@Test
	void test_user_registerUser()
	{
		when(httpServletRequest.getSession()).thenReturn(session);
		ModelMap map = new ModelMap();
		map.put("registerError", ERROR_MSG);

		UserDto userDto = new UserDto();
		userDto.setUsername(USERNAME);
		userDto.setPassword(PASSWORD);
		userDto.setMatchingPassword(PASSWORD);

		Assertions.assertEquals("redirect:/", user.registerUser(map, httpServletRequest, userDto));
		Assertions.assertFalse(map.containsAttribute("registerError"));

		verify(authenticationService, times(1)).registerUser(userDto);
		verify(session, times(1)).removeAttribute("redirect");
	}

	@Test
	void test_user_registerUser_duplicate()
	{
		UserDto userDto = new UserDto();
		userDto.setUsername(USERNAME);
		userDto.setPassword(PASSWORD);
		userDto.setMatchingPassword(PASSWORD);

		doThrow(new DuplicateKeyException("")).when(authenticationService).registerUser(userDto);
		ModelMap map = new ModelMap();

		Assertions.assertEquals("redirect:register", user.registerUser(map, httpServletRequest, userDto));
		Assertions.assertEquals(userDto, map.get("userDto"));
		Assertions.assertEquals("A user with this name already exists.", map.get("registerError"));

		verify(authenticationService, times(1)).registerUser(userDto);
	}
}
