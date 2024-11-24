package io.github.fi0x.util.service;

import io.github.fi0x.util.components.ServiceInformation;
import io.github.fi0x.util.dto.ServiceDataDto;
import io.github.fi0x.util.dto.ServiceInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

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
	@Value("${homeserver.service.login-enabled}")
	private Boolean loginEnabled;

	private final ServiceInformation serviceInformation;

	private final RestTemplate restTemplate = new RestTemplate();

	/**
	 * This method returns all the information, that can be gathered, about this service.
	 *
	 * @return The {@link ServiceInfoDto} of this service.
	 */
	public ServiceInfoDto getServiceInformation()
	{
		log.trace("getServiceInformation() called");

		String url =
				"http://" + serviceInformation.getHubIp() + ":" + serviceInformation.getHubPort() + "/api/service/info?serviceName=" + serviceInformation.getServiceName();
		ServiceInfoDto.ServiceInfoDtoBuilder builder =
				ServiceInfoDto.builder().name(serviceInformation.getServiceName()).loginDisabled(!loginEnabled)
							  .isHub(serviceInformation.getIsHub());

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
	public byte[] getServiceLogo() throws ResponseStatusException
	{
		try
		{
			InputStream input = getClass().getResourceAsStream("images/logo.png");
			if(input == null)
			{
				log.warn("Could not return the logo of this service, because the InputStream is null");
				throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Could not find image");
			}

			return input.readAllBytes();
		} catch(IOException e)
		{
			log.warn("Could not return the logo of this service", e);
		}
		throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Could not find image");
	}
}
