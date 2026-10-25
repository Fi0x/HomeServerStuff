package io.fi0x.util.components;

import io.github.fi0x.util.components.HubNotifier;
import io.github.fi0x.util.components.ServiceInformation;
import io.github.fi0x.util.dto.ServiceDataDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class TestHubNotifier
{
	@Mock
	private ServiceInformation serviceInformation;

	@Mock
	private RestClient restClient;

	@Mock
	private RestClient.RequestBodyUriSpec requestBodyUriSpec;

	@Mock
	private RestClient.ResponseSpec responseSpec;

	private HubNotifier component;

	@BeforeEach
	void setup()
	{
		component = Mockito.spy(new HubNotifier(serviceInformation, restClient));
		ReflectionTestUtils.setField(component, "currentPort", 8080);
	}

	@Test
	void test_registerInHub_success()
	{
		Mockito.when(serviceInformation.getServiceName()).thenReturn("test-service");
		Mockito.doReturn(List.of("192.168.0.255")).when(component).getBroadcastIps();

		Mockito.when(restClient.post()).thenReturn(requestBodyUriSpec);
		Mockito.when(requestBodyUriSpec.uri("http://192.168.0.255/api/service/register"))
				.thenReturn(requestBodyUriSpec);
		Mockito.when(requestBodyUriSpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodyUriSpec);
		Mockito.when(requestBodyUriSpec.body(any(ServiceDataDto.class))).thenReturn(requestBodyUriSpec);
		Mockito.when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
		Mockito.when(responseSpec.toBodilessEntity()).thenReturn(ResponseEntity.ok().build());

		Assertions.assertDoesNotThrow(() -> component.registerInHub());

		Mockito.verify(restClient).post();
		Mockito.verify(requestBodyUriSpec).uri("http://192.168.0.255/api/service/register");
		Mockito.verify(requestBodyUriSpec).contentType(MediaType.APPLICATION_JSON);
		Mockito.verify(requestBodyUriSpec).body(any(ServiceDataDto.class));
		Mockito.verify(responseSpec).toBodilessEntity();
	}
}
