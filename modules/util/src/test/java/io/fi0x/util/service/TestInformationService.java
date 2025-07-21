package io.fi0x.util.service;

import io.github.fi0x.util.components.ServiceInformation;
import io.github.fi0x.util.dto.ServiceDataDto;
import io.github.fi0x.util.dto.ServiceInfoDto;
import io.github.fi0x.util.service.InformationService;
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
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class TestInformationService
{
	private static final String HUB_IP = "123";
	private static final Integer HUB_PORT = 2345;
	private static final String SERVICE_NAME = "Test Service";
	private static final Boolean HAS_LOGIN = true;
	private static final Boolean IS_HUB = false;
	private static final String PROTOCOL = "Http";
	private static final String SERVICE_IP = "192.837";
	private static final Integer SERVICE_PORT = 4938;

	@Mock
	private ServiceInformation serviceInformation;
	@Mock
	private RestTemplate restMock;

	@InjectMocks
	private InformationService service;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);

		ReflectionTestUtils.setField(service, "loginEnabled", HAS_LOGIN);
		ReflectionTestUtils.setField(service, "restTemplate", restMock);
	}

	@Test
	void test_getServiceInformation_success()
	{
		when(serviceInformation.getHubIp()).thenReturn(HUB_IP);
		when(serviceInformation.getHubPort()).thenReturn(HUB_PORT);
		when(serviceInformation.getServiceName()).thenReturn(SERVICE_NAME);
		when(serviceInformation.getIsHub()).thenReturn(IS_HUB);
		when(restMock.getForObject(anyString(), any())).thenReturn(
				ServiceDataDto.builder().protocol(PROTOCOL).ip(SERVICE_IP).port(SERVICE_PORT).build());

		ServiceInfoDto result = service.getServiceInformation();
		Assertions.assertEquals(SERVICE_NAME, result.getName());
		Assertions.assertEquals(HAS_LOGIN, !result.getLoginDisabled());
		Assertions.assertEquals(IS_HUB, result.getIsHub());
		Assertions.assertEquals(PROTOCOL, result.getProtocol());
		Assertions.assertEquals(SERVICE_IP, result.getIp());
		Assertions.assertEquals(SERVICE_PORT, result.getPort());
	}

	@Test
	void test_getServiceInformation_noResponse()
	{
		when(serviceInformation.getHubIp()).thenReturn(HUB_IP);
		when(serviceInformation.getHubPort()).thenReturn(HUB_PORT);
		when(serviceInformation.getServiceName()).thenReturn(SERVICE_NAME);
		when(serviceInformation.getIsHub()).thenReturn(IS_HUB);
		when(restMock.getForObject(anyString(), any())).thenReturn(null);

		ServiceInfoDto result = service.getServiceInformation();
		Assertions.assertEquals(SERVICE_NAME, result.getName());
		Assertions.assertEquals(HAS_LOGIN, !result.getLoginDisabled());
		Assertions.assertEquals(IS_HUB, result.getIsHub());
		Assertions.assertNull(result.getProtocol());
		Assertions.assertNull(result.getIp());
		Assertions.assertNull(result.getPort());
	}

	@Test
	void test_getServiceInformation_responseException()
	{
		when(serviceInformation.getHubIp()).thenReturn(HUB_IP);
		when(serviceInformation.getHubPort()).thenReturn(HUB_PORT);
		when(serviceInformation.getServiceName()).thenReturn(SERVICE_NAME);
		when(serviceInformation.getIsHub()).thenReturn(IS_HUB);
		when(restMock.getForObject(anyString(), any())).thenThrow(new RestClientException("Error"));

		ServiceInfoDto result = service.getServiceInformation();
		Assertions.assertEquals(SERVICE_NAME, result.getName());
		Assertions.assertEquals(HAS_LOGIN, !result.getLoginDisabled());
		Assertions.assertEquals(IS_HUB, result.getIsHub());
		Assertions.assertNull(result.getProtocol());
		Assertions.assertNull(result.getIp());
		Assertions.assertNull(result.getPort());
	}

	@Test
	void test_getServiceLogo_exception()
	{
		Assertions.assertThrows(ResponseStatusException.class, () -> service.getServiceLogo());
	}
}
