package io.github.fi0x.util.rest;

import io.github.fi0x.util.dto.ServiceDataDto;
import io.github.fi0x.util.service.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
