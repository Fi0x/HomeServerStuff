package io.fi0x.util.components;

import io.github.fi0x.util.components.ServiceInformation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class TestServiceInformation
{
	private static final String LOCALHOST = "localhost";
	private static final Integer HUB_PORT = 2345;
	private static final String SERVICE_NAME = "TestService";
	private static final Boolean IS_HUB = false;
	private static final String LOCAL_IP = "192.168.1.2";
	private static final String LOCAL_IP2 = "192.168.1.3";
	private static final String EXTERNAL_IP = "1.2.3.4";


	private MockedStatic<NetworkInterface> staticMock;
	@Mock
	private Enumeration<NetworkInterface> interfaces;
	@Mock
	private NetworkInterface networkInterface;
	@Mock
	private InterfaceAddress interfaceAddress;
	@Mock
	private InetAddress address;

	@InjectMocks
	private ServiceInformation component;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);

		staticMock = mockStatic(NetworkInterface.class);
		staticMock.when(NetworkInterface::getNetworkInterfaces).thenReturn(interfaces);
		when(interfaces.hasMoreElements()).thenReturn(true, false, true, false);
		when(interfaces.nextElement()).thenReturn(networkInterface);
		when(networkInterface.getInterfaceAddresses()).thenReturn(List.of(interfaceAddress));
		when(interfaceAddress.getAddress()).thenReturn(address);
		when(address.getHostAddress()).thenReturn(LOCAL_IP);

		ReflectionTestUtils.setField(component, "hubIp", LOCALHOST);
		ReflectionTestUtils.setField(component, "hubPort", HUB_PORT);
		ReflectionTestUtils.setField(component, "serviceName", SERVICE_NAME);
		ReflectionTestUtils.setField(component, "isHub", IS_HUB);
	}

	@AfterEach
	void cleanup()
	{
		staticMock.close();

		Object ipObject = ReflectionTestUtils.getField(component, "LOCAL_IPS");
		if(ipObject instanceof List<?> ipList)
			ipList.clear();
	}

	@Test
	void test_getHubIp_success()
	{
		Assertions.assertEquals(LOCAL_IP, component.getHubIp());
		Assertions.assertEquals(LOCAL_IP, component.getHubIp());

		ReflectionTestUtils.setField(component, "lastTimeFetchedIps", 0);
		Assertions.assertEquals(LOCAL_IP, component.getHubIp());

		verify(interfaces, times(2)).nextElement();
	}

	@Test
	void test_getHubIp_success_otherIp()
	{
		ReflectionTestUtils.setField(component, "hubIp", LOCAL_IP2);

		Assertions.assertEquals(LOCAL_IP2, component.getHubIp());
	}

	@Test
	void test_getHubIp_fallback()
	{
		when(address.getHostAddress()).thenReturn(EXTERNAL_IP);

		Assertions.assertEquals(LOCALHOST, component.getHubIp());
	}

	@Test
	void text_getHubIp_exception()
	{
		staticMock.when(NetworkInterface::getNetworkInterfaces).thenThrow(new SocketException());

		Assertions.assertEquals(LOCALHOST, component.getHubIp());
	}
}
