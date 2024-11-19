package io.github.fi0x.hub.rest;

import io.github.fi0x.hub.service.ConnectionService;
import io.github.fi0x.util.dto.ServiceDataDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@SessionAttributes({"username"})
public class ConnectionController
{
	private final ConnectionService connectionService;

	@PostMapping("/api/service/register")
	public void registerService(HttpServletRequest request, @RequestBody ServiceDataDto requestDto)
	{
		log.debug("registerService() called");

		connectionService.registerService(request.getRemoteAddr(), requestDto);
	}

	@GetMapping("/api/service/list")
	public List<ServiceDataDto> getRegisteredServices()
	{
		log.debug("getRegisteredServices() called");

		return connectionService.getConnectedServices();
	}
}
