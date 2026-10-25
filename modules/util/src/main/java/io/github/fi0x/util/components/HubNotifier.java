package io.github.fi0x.util.components;

import io.github.fi0x.util.dto.ServiceDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;

/**
 * This component is used to notify the hub about the existence of a new service.
 */
@Slf4j
@RequiredArgsConstructor
public class HubNotifier
{
	private static final String URL_PREFIX = "http://";
	private static final String HUB_REGISTER_ENDPOINT = "/api/service/register";

	@Value("${server.port}")
	private Integer currentPort;

	private final ServiceInformation serviceInformation;
	private final RestClient restClient;

	/**
	 * This scheduled method runs every minute to let the hub know, that this service exists.
	 * The hub is located through the {@code homeserver.hub.ip} and {@code homeserver.hub.port} properties.
	 * The service, that runs this scheduled method, sends its name (set through the {@code homeserver.service.name}
	 * property) to the hub. It also lets the hub know, on which port of the current machine, this service is
	 * reachable.
	 */
	@Scheduled(fixedRate = 60000)
	public void registerInHub()
	{
		log.debug("Sending service-info to hub");

		ServiceDataDto requestDto =
				ServiceDataDto.builder().name(serviceInformation.getServiceName()).port(currentPort)
				.build();

		for (String ip : getBroadcastIps())
		{
			try
			{
				log.trace("Sending service-info to {}", ip);
				String url = URL_PREFIX + ip + HUB_REGISTER_ENDPOINT;
				restClient.post().uri(url).contentType(MediaType.APPLICATION_JSON).body(requestDto).retrieve()
						.toBodilessEntity();
			} catch (RestClientException e)
			{
				log.warn("Could not reach hub because of exception: {}", e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * Provides a list of all broadcast addresses of the local network.
	 *
	 * @return A list of all broadcast addresses of the local network.
	 */
	public List<String> getBroadcastIps()
	{
		HashSet<String> broadcastAddresses = new HashSet<>();
		try
		{
			Enumeration<NetworkInterface> list = NetworkInterface.getNetworkInterfaces();
			while (list.hasMoreElements())
			{
				NetworkInterface iface = list.nextElement();
				if (iface == null)
					continue;
				if (!iface.isLoopback() && iface.isUp())
				{
					for (InterfaceAddress address : iface.getInterfaceAddresses())
					{
						if (address == null)
							continue;

						InetAddress broadcast = address.getBroadcast();
						if (broadcast != null)
						{
							broadcastAddresses.add(broadcast.getHostAddress());
						}
					}
				}
			}
		} catch (SocketException e)
		{
			throw new RuntimeException(e);
		}

		return broadcastAddresses.stream().toList();
	}
}
