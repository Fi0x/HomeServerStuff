package io.github.fi0x.util.service;

import io.github.fi0x.util.dto.ServiceDataDto;
import io.github.fi0x.util.dto.ServiceInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class provides methods, that return information about this service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InformationService
{
	private final RestTemplate restTemplate = new RestTemplate();
	@Value("${homeserver.hub.ip}")
	private String hubIp;
	@Value("${homeserver.service.login-enabled}")
	private Boolean loginEnabled;
	@Value("${homeserver.service.name}")
	private String serviceName;
	@Value("${homeserver.service.is-hub}")
	private Boolean isHub;
	@Value("${homeserver.hub.port}")
	private String hubPort;

	/**
	 * This method returns all the information, that can be gathered, about this service.
	 *
	 * @return The {@link ServiceInfoDto} of this service.
	 */
	public ServiceInfoDto getServiceInformation()
	{
		log.trace("getServiceInformation() called");

		String url = "http://" + hubIp + ":" + hubPort + "/api/service/info?serviceName=" + serviceName;
		ServiceInfoDto.ServiceInfoDtoBuilder builder =
				ServiceInfoDto.builder().name(serviceName).loginDisabled(!loginEnabled).isHub(isHub);

		try
		{
			ServiceDataDto result = restTemplate.getForObject(url, ServiceDataDto.class);

			if(result == null)
				log.warn("Could not fetch any service from hub.");
			else
				builder.protocol(result.getProtocol()).ip(result.getIp()).port(result.getPort());
		} catch(RestClientException e)
		{
			log.warn("Could not reach hub because of exception: {}", e.getLocalizedMessage());
		}

		return builder.build();
	}

	/**
	 * Returns the logo of this service.
	 *
	 * @return The {@link Byte}[] of the logo.
	 */
	public byte[] getServiceLogo()
	{
		try
		{
			InputStream input = getClass().getResourceAsStream("images/logo.png");
			if(input == null)
			{
				log.warn("Could not return the logo of this service, because the InputStream is null");
				return null;
			}

			return input.readAllBytes();
		} catch(IOException e)
		{
			log.warn("Could not return the logo of this service", e);
		}
		return null;
	}
}
