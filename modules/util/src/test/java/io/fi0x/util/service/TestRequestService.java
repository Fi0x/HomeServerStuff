package io.fi0x.util.service;


import io.github.fi0x.util.components.ServiceInformation;
import io.github.fi0x.util.dto.ServiceDataDto;
import io.github.fi0x.util.service.RequestService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class TestRequestService
{
	private static final String GITHUB_URL = "github.com";
	private static final String HUB_IP = "123.456.7.8";
	private static final Integer HUB_PORT = 2345;
	private static final String SERVICE_NAME = "Test Service";
	private static final String OTHER_SERVICE = "Second Service";

	@Mock
	private ServiceInformation serviceInformation;
	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private RequestService service;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);

		ReflectionTestUtils.setField(service, "githubUrl", GITHUB_URL);
		ReflectionTestUtils.setField(service, "restTemplate", restTemplate);
	}

	@Test
	void test_getRegisteredServicesFromHub_success()
	{
		when(serviceInformation.getServiceName()).thenReturn(SERVICE_NAME);
		ServiceDataDto dto = ServiceDataDto.builder().name(SERVICE_NAME).build();
		ServiceDataDto dto2 = ServiceDataDto.builder().name(OTHER_SERVICE).build();
		when(restTemplate.getForObject(anyString(), any())).thenReturn(new ServiceDataDto[]{dto, dto2});

		List<ServiceDataDto> result = service.getRegisteredServicesFromHub();
		Assertions.assertEquals(List.of(dto2), result);
	}

	@Test
	void test_getRegisteredServicesFromHub_noOtherServices()
	{
		when(serviceInformation.getServiceName()).thenReturn(SERVICE_NAME);
		ServiceDataDto dto = ServiceDataDto.builder().name(SERVICE_NAME).build();
		ServiceDataDto dto2 = ServiceDataDto.builder().name(OTHER_SERVICE).build();
		when(restTemplate.getForObject(anyString(), any())).thenReturn(new ServiceDataDto[]{dto});

		List<ServiceDataDto> result = service.getRegisteredServicesFromHub();
		Assertions.assertEquals(List.of(), result);
	}

	@Test
	void test_getRegisteredServicesFromHub_nullResult()
	{
		when(restTemplate.getForObject(anyString(), any())).thenReturn(null);

		List<ServiceDataDto> result = service.getRegisteredServicesFromHub();
		Assertions.assertEquals(List.of(), result);
	}

	@Test
	void test_getRegisteredServicesFromHub_exception()
	{
		when(restTemplate.getForObject(anyString(), any())).thenThrow(new RestClientException("Error"));

		List<ServiceDataDto> result = service.getRegisteredServicesFromHub();
		Assertions.assertEquals(List.of(), result);
	}

	@Test
	void test_getGithubUrl()
	{
		Assertions.assertEquals(GITHUB_URL, service.getGithubUrl());
	}

	@Test
	void test_getHubUrl()
	{
		when(serviceInformation.getHubIp()).thenReturn(HUB_IP);
		when(serviceInformation.getHubPort()).thenReturn(HUB_PORT);

		String url = "http://" + HUB_IP + ":" + HUB_PORT;

		Assertions.assertEquals(url, service.getHubUrl());
	}
}
