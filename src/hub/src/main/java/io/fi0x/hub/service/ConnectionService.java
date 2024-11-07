package io.fi0x.hub.service;

import io.fi0x.util.dto.ServiceDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectionService
{
	private static final List<ServiceDataDto> SERVICES = new ArrayList<>();

	public void registerService(String address, ServiceDataDto service)
	{
		log.trace("registerService() called");

		if (ObjectUtils.isEmpty(service.getIp()))
			service.setIp(address);
		if (ObjectUtils.isEmpty(service.getIp()))
		{
			log.warn("Cannot register service with an unknown address. Dto was: {}", service);
			throw new ResponseStatusException(HttpStatusCode.valueOf(400), "Unknown service-address");
		}
		if (ObjectUtils.isEmpty(service.getName()))
		{
			log.warn("Cannot register service without a service-name. Address: {}", service.getIp());
			throw new ResponseStatusException(HttpStatusCode.valueOf(400), "Unknown service-name");
		}
		if (service.getPort() == null)
		{
			log.warn("Cannot register service without a port. Address: {}", service.getIp());
			throw new ResponseStatusException(HttpStatusCode.valueOf(400), "Unknown service-port");
		}

		SERVICES.add(service);
	}

	public List<ServiceDataDto> getConnectedServices()
	{
		log.trace("getConnectedServices() called");

		return SERVICES;
	}
}
