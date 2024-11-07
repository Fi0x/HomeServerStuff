package io.fi0x.util.components;

import io.fi0x.util.dto.ServiceDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubNotifier
{
	@Value("${homeserver.hub.ip}")
	private String hubIp = "localhost";
	@Value("${homeserver.hub.port}")
	private Integer hubPort = 2345;
	@Value("${homeserver.service.name}")
	private String serviceName = "Util";
	@Value("${server.port}")
	private Integer currentPort = 0;

	private final RestTemplate restTemplate = new RestTemplate();

	@Scheduled(fixedRate = 60000)
	public void registerInHub()
	{
		log.debug("Sending service-info to hub");

		String url = "http://" + hubIp + ":" + hubPort + "/api/service/register";
		ServiceDataDto requestDto = ServiceDataDto.builder().name(serviceName).port(currentPort).build();

		try
		{
			restTemplate.postForObject(url, requestDto, Void.class);
		} catch (RestClientException e)
		{
			log.warn("Could not reach hub because of exception: {}", e.getLocalizedMessage());
		}
	}
}
