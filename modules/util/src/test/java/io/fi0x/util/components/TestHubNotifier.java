package io.fi0x.util.components;

import io.github.fi0x.util.components.HubNotifier;
import io.github.fi0x.util.components.ServiceInformation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TestHubNotifier
{
	@Mock
	private ServiceInformation serviceInformation;

	@InjectMocks
	private HubNotifier component;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);

		Mockito.when(serviceInformation.getHubIp()).thenReturn("localhost");
	}

	@Test
	void test_registerInHub_success()
	{
		Assertions.assertDoesNotThrow(() -> component.registerInHub());
	}
}
