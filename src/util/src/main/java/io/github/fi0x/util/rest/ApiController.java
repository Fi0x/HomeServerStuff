package io.github.fi0x.util.rest;

import io.github.fi0x.util.dto.ServiceDataDto;
import io.github.fi0x.util.service.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${homeserver.github.url:https://github.com/Fi0x/HomeServerStuff}")
	private String githubUrl;

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
	public ModelAndView getGithubUrl()
	{
		log.debug("getGithubUrl() called");

		return new ModelAndView("redirect:" + githubUrl);
	}
}
