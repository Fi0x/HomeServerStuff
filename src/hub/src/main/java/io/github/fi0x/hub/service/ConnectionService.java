package io.github.fi0x.hub.service;

import io.fi0x.javadatastructures.Tuple;
import io.fi0x.javadatastructures.TupleList;
import io.github.fi0x.util.dto.ServiceDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectionService
{
	private static final TupleList<Long, ServiceDataDto> SERVICES = new TupleList<>();

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

		List<Tuple<Long, ServiceDataDto>> alreadySaved = SERVICES.stream()
																 .filter(serviceDataDto -> serviceDataDto.getObject2()
																										 .getName()
																										 .equals(service.getName()))
																 .toList();

		if (ObjectUtils.isEmpty(service.getProtocol()))
			service.setProtocol("http");
		if (alreadySaved.isEmpty())
		{
			SERVICES.add(new Tuple<>(System.currentTimeMillis(), service));
			return;
		}

		if (alreadySaved.size() > 1)
		{
			log.warn("Multiple services with the name '{}' were registered. The outdated ones will get removed",
					 service.getName());
			SERVICES.removeAll(alreadySaved);

			SERVICES.add(new Tuple<>(System.currentTimeMillis(), service));
			return;
		}

		Tuple<Long, ServiceDataDto> tuple = alreadySaved.get(0);
		boolean sameIp = tuple.getObject2().getIp().equals(service.getIp());
		boolean samePort = tuple.getObject2().getPort().equals(service.getPort());
		if (sameIp && samePort)
		{
			tuple.setObject1(System.currentTimeMillis());
			log.debug("Service '{}' updated", service.getName());
		} else
		{
			log.info("Service '{}' changed from {}:{} to {}:{}", service.getName(), tuple.getObject2().getIp(),
					 tuple.getObject2().getPort(), service.getIp(), service.getPort());
			tuple.setObject2(service);
		}
	}

	public List<ServiceDataDto> getConnectedServices()
	{
		log.trace("getConnectedServices() called");

		return SERVICES.stream().map(Tuple::getObject2).toList();
	}

	@Scheduled(fixedRate = 60000)
	public void cleanup()
	{
		log.debug("Scheduled cleanup running");

		TupleList<Long, ServiceDataDto> removalList = new TupleList<>();
		SERVICES.forEach(tuple -> {
			if (System.currentTimeMillis() - tuple.getObject1() > 120000)
				removalList.add(tuple);
		});

		SERVICES.removeAll(removalList);

		log.trace("Updated services-list: {}", SERVICES);
	}
}
