package io.fi0x.hub.rest;

import io.fi0x.util.logic.dto.ServiceDataDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Controller
@RequiredArgsConstructor
@SessionAttributes({"username"})
public class ConnectionController
{
	private static final Map<String, ServiceDataDto> SERVICES = new HashMap<>();

	@PostMapping("/api/service/register")
	public void registerService(HttpServletRequest request, @RequestBody ServiceDataDto requestDto)
	{
		log.debug("registerService() called");

		String address = requestDto.getIp().isBlank() ? request.getRemoteAddr() : requestDto.getIp();
		if (address.isBlank())
		{
			log.warn("Received service register-request from an unknown address. Dto was: {}", requestDto);
			return;
		}
		if (requestDto.getName().isBlank())
		{
			log.warn("Received service register-request without a service-name from address: {}", address);
			return;
		}
		if (requestDto.getPort() == null)
		{
			log.warn("Received service register-request without a port from address: {}", address);
			return;
		}

		SERVICES.put(convert(address, requestDto.getPort()), requestDto);
	}

	public static ServiceDataDto getServiceName(String ip, Integer port)
	{
		return SERVICES.get(convert(ip, port));
	}

	public static String getServiceAddress(String serviceName)
	{
		for (Map.Entry<String, ServiceDataDto> entry : SERVICES.entrySet())
		{
			if (entry.getValue().getName().equals(serviceName))
				return entry.getKey();
		}
		return null;
	}

	private static String convert(String address, Integer port)
	{
		return address + ":" + port;
	}
}
