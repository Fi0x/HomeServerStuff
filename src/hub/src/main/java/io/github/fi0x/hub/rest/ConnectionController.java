package io.github.fi0x.hub.rest;

import io.github.fi0x.hub.service.ConnectionService;
import io.github.fi0x.util.dto.ServiceDataDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.SessionAttributes;


@Slf4j
@Controller
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
}
