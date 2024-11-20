package io.github.fi0x.util.service;

import io.github.fi0x.util.dto.ServiceInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

	/**
	 * This method returns all the information, that can be gathered, about this service.
	 *
	 * @return The {@link ServiceInfoDto} of this service.
	 */
	public ServiceInfoDto getServiceInformation()
	{
		return ServiceInfoDto.builder().loginDisabled(!loginEnabled).build();
	}
}
