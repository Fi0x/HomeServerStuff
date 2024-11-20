package io.github.fi0x.util.config;

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
	 */
	@Bean
	public ApiController apiController(RequestService requestService, InformationService infoService)
	{
		return new ApiController(requestService, infoService);
	}

	/**
	 * This method creates a RequestService-bean, that is required by the apiController-bean
	 *
	 * @return The {@link RequestService} bean
	 */
	@Bean
	public RequestService requestService()
	{
		return new RequestService();
	}

	/**
	 * This method creates an InfoService-bean, that is required by the apiController-bean
	 *
	 * @return The {@link InformationService} bean
	 */
	@Bean
	public InformationService infoService()
	{
		return new InformationService();
	}
}
