package io.github.fi0x.util.service;

import io.github.fi0x.util.dto.ServiceInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
	@Value("${homeserver.service.name}")
	private String serviceName;
	@Value("${homeserver.service.is-hub}")
	private Boolean isHub;

	/**
	 * This method returns all the information, that can be gathered, about this service.
	 *
	 * @return The {@link ServiceInfoDto} of this service.
	 */
	public ServiceInfoDto getServiceInformation()
	{
		return ServiceInfoDto.builder().name(serviceName).loginDisabled(!loginEnabled).isHub(isHub).build();
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
			if (input == null)
			{
				log.warn("Could not return the logo of this service, because the InputStream is null");
				return null;
			}

			return input.readAllBytes();
		} catch (IOException e)
		{
			log.warn("Could not return the logo of this service", e);
		}
		return null;
	}
}
