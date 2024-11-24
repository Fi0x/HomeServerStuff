package io.github.fi0x.util.components;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * This component provides methods, to retrieve basic information about this service.
 */
@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class ServiceInformation
{
	private static final List<String> LOCAL_IPS = new ArrayList<>();
	private static long lastTimeFetchedIps = 0;
	@Value("${homeserver.hub.ip}")
	private String hubIp;
	@Value("${homeserver.hub.port}")
	private Integer hubPort;
	@Value("${homeserver.service.name}")
	private String serviceName;
	@Value("${homeserver.service.is-hub}")
	private Boolean isHub;

	/**
	 * This is the getter for the {@code hubIp} field. It is modified to translate {@code localhost} to the current address.
	 *
	 * @return The ip-address of the hub.
	 */
	public String getHubIp()
	{
		if("localhost".equals(hubIp))
		{
			List<String> ips = getLocalIpAddresses();
			for(String ip : ips)
			{
				if(ip.startsWith("192.168."))
					return ip;
			}

			log.warn("Could not find local host address, falling back to localhost");
		}

		return hubIp;
	}

	private List<String> getLocalIpAddresses()
	{
		if(LOCAL_IPS.isEmpty() || lastTimeFetchedIps < System.currentTimeMillis() - 60000)
		{
			lastTimeFetchedIps = System.currentTimeMillis();
			List<String> ips = new ArrayList<>();

			Enumeration<NetworkInterface> interfaces;
			try
			{
				interfaces = NetworkInterface.getNetworkInterfaces();

				while(interfaces.hasMoreElements())
				{
					List<InterfaceAddress> addresses = interfaces.nextElement().getInterfaceAddresses();
					for(InterfaceAddress address : addresses)
						ips.add(address.getAddress().getHostAddress());
				}

				LOCAL_IPS.clear();
				LOCAL_IPS.addAll(ips);

			} catch(SocketException e)
			{
				log.warn(
						"Could not get network interfaces to retrieve local ip addresses. Existing list will be returned.",
						e);
			}
		}

		return LOCAL_IPS;
	}
}
