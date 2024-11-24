package io.github.fi0x.util.config;

import io.github.fi0x.util.components.Authenticator;
import io.github.fi0x.util.components.ServiceInformation;
import io.github.fi0x.util.rest.ApiController;
import io.github.fi0x.util.service.InformationService;
import io.github.fi0x.util.service.RequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This configuration can be imported, to enable basic api-endpoints, that are needed by most services
 */
@Slf4j
@Configuration
public class ApiConfig
{
	/**
	 * This method creates an apiController-bean, that will provide api-endpoints.
	 *
	 * @param requestService The {@link RequestService} that handles api-requests to other services.
	 * @param infoService    The {@link InformationService} that provides information about this service.
	 * @return The {@link ApiController} bean with its endpoints enabled
	 * @param authenticator The {@link Authenticator} that provides the username to the service
	 */
	@Bean
	public ApiController apiController(RequestService requestService, InformationService infoService,
									   Authenticator authenticator)
	{
		return new ApiController(requestService, infoService, authenticator);
	}

	/**
	 * This method creates a RequestService-bean, that is required by the apiController-bean
	 *
	 * @param serviceInformation The serviceInformation-bean of this service
	 * @return The {@link RequestService} bean
	 */
	@Bean
	public RequestService requestService(ServiceInformation serviceInformation)
	{
		return new RequestService(serviceInformation);
	}

	/**
	 * This method creates an InfoService-bean, that is required by the apiController-bean
	 *
	 * @param serviceInformation The serviceInformation-bean of this service
	 * @return The {@link InformationService} bean
	 */
	@Bean
	public InformationService infoService(ServiceInformation serviceInformation)
	{
		return new InformationService(serviceInformation);
	}
}
