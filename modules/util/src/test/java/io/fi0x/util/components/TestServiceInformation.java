package io.fi0x.util.components;

import io.github.fi0x.util.components.ServiceInformation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TestServiceInformation
{
	private static final String LOCAL_IP = null;

	@InjectMocks
	private ServiceInformation component;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void test_getHubIp_success()
	{
		Assertions.assertEquals(null, component.getHubIp());
	}
}
