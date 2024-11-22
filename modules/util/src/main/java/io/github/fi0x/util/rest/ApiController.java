package io.github.fi0x.util.rest;

import io.github.fi0x.util.components.Authenticator;
import io.github.fi0x.util.dto.LoginDto;
import io.github.fi0x.util.dto.ServiceDataDto;
import io.github.fi0x.util.dto.ServiceInfoDto;
import io.github.fi0x.util.service.InformationService;
import io.github.fi0x.util.service.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * This controller provides commonly used REST-endpoints
 */
@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ApiController
{
	private final RequestService requestService;
	private final InformationService infoService;
	private final Authenticator authenticator;

	/**
	 * This endpoint retrieves information about all the services, that are registered in the hub.
	 *
	 * @return A list of all services, registered in the hub
	 */
	@GetMapping("/service/list")
	public List<ServiceDataDto> getRegisteredServices()
	{
		log.debug("getRegisteredServices() called");

		return requestService.getRegisteredServicesFromHub();
	}

	/**
	 * This endpoint provides the url for this project's github-page
	 *
	 * @return The github-url of this project
	 */
	@GetMapping("/github")
	public ModelAndView redirectToGithubUrl()
	{
		log.debug("redirectToGithubUrl() called");

		return new ModelAndView("redirect:" + requestService.getGithubUrl());
	}

	/**
	 * This endpoint provides the url for the hub, this service is connected to
	 *
	 * @return The url of the service's hub
	 */
	@GetMapping("/hub")
	public ModelAndView redirectToHubUrl()
	{
		log.debug("redirectToHubUrl() called");

		return new ModelAndView("redirect:" + requestService.getHubUrl());
	}

	/**
	 * This endpoint provides information about the current service.
	 *
	 * @return A {@link ServiceInfoDto} with the information about this service.
	 */
	@GetMapping("/service/info")
	public ServiceInfoDto getServiceInfo()
	{
		log.debug("getServiceInfo() called");

		return infoService.getServiceInformation();
	}

	/**
	 * This endpoint is used to provide an image, that represents this service's logo
	 *
	 * @return The logo.png of this service
	 */
	@GetMapping(path = "/service/logo", produces = MediaType.IMAGE_PNG_VALUE)
	public byte[] getServiceLogo()
	{
		log.debug("getServiceLogo() called");

		return infoService.getServiceLogo();
	}

	/**
	 * This endpoint is used to provide the username of the currently logged-in user.
	 *
	 * @return The username.
	 */
	@GetMapping(path = "/username")
	public LoginDto getCurrentUsername()
	{
		log.debug("getCurrentUsername() called");

		return LoginDto.builder().username(authenticator.getAuthenticatedUsername()).build();
	}
}
