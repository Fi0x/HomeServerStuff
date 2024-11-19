package io.github.fi0x.util.service;

import io.github.fi0x.util.dto.ServiceDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class provides methods, that perform REST-calls to other services.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RequestService
{
	@Value("${homeserver.hub.ip:localhost}")
	private String hubIp;
	@Value("${homeserver.hub.port:2345}")
	private String hubPort;

	private final RestTemplate restTemplate = new RestTemplate();

	/**
	 * This method calls the hub endpoint, to receive information, about the services, that are registered there.
	 *
	 * @return A list of all registered services from the hub.
	 */
	public List<ServiceDataDto> getRegisteredServicesFromHub()
	{
		log.trace("getRegisteredServicesFromHub() called");

		String url = "http://" + hubIp + ":" + hubPort + "/api/service/list";
		try
		{
			ServiceDataDto[] result = restTemplate.getForObject(url, ServiceDataDto[].class);

			if (result != null)
				return Arrays.asList(result);

			log.warn("Could not fetch any service from hub.");
			return Collections.emptyList();
		} catch (RestClientException e)
		{
			log.warn("Could not reach hub because of exception: {}", e.getLocalizedMessage());
		}
		return Collections.emptyList();
	}
}
