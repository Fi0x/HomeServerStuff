package io.github.fi0x.hub.rest;

import io.github.fi0x.hub.service.ConnectionService;
import io.github.fi0x.util.components.ServiceInformation;
import io.github.fi0x.util.dto.ServiceDataDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/service")
@SessionAttributes({"username"})
public class ConnectionController
{
	private final ConnectionService connectionService;
	private final ServiceInformation serviceInformation;

	@PostMapping("/register")
	public void registerService(HttpServletRequest request, @RequestBody ServiceDataDto requestDto)
	{
		log.debug("registerService() called");

		connectionService.registerService(request.getRemoteAddr(), requestDto);
	}

	@GetMapping("/list")
	public List<ServiceDataDto> getRegisteredServices()
	{
		log.debug("getRegisteredServices() called");

		return connectionService.getConnectedServices();
	}

	@GetMapping("/info")
	public ServiceDataDto getRegisteredServiceInfo(@RequestParam String serviceName)
	{
		log.debug("getRegisteredServiceInfo() called");

		if(serviceInformation.getIsHub())
			return ServiceDataDto.builder().name(serviceInformation.getServiceName()).protocol("http")
								 .ip(serviceInformation.getHubIp()).port(serviceInformation.getHubPort()).build();

		List<ServiceDataDto> possibleServices =
				connectionService.getConnectedServices().stream().filter(dto -> serviceName.equals(dto.getName()))
								 .toList();

		if(possibleServices.size() != 1)
			throw new ResponseStatusException(HttpStatusCode.valueOf(404),
											  "Found none or multiple services with name '" + serviceName + "'");

		return possibleServices.get(0);
	}
}
