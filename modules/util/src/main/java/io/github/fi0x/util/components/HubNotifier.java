package io.github.fi0x.util.components;

import io.github.fi0x.util.dto.ServiceDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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
@Component
@RequiredArgsConstructor
public class HubNotifier
{
	private static final String URL_PREFIX = "http://";
	private static final String HUB_REGISTER_ENDPOINT = "/api/service/register";

	@Value("${server.port}")
	private Integer currentPort;

	private final ServiceInformation serviceInformation;

	private final RestTemplate restTemplate = new RestTemplate();

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

		//		String urlEnding = ":" + serviceInformation.getHubPort() + HUB_REGISTER_ENDPOINT;
		String urlEnding = HUB_REGISTER_ENDPOINT;

		ServiceDataDto requestDto = ServiceDataDto.builder().name(serviceInformation.getServiceName()).port(currentPort)
				.build();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<ServiceDataDto> request = new HttpEntity<>(requestDto, headers);

		for (String ip : getBroadcastIps())
		{
			try
			{
				log.trace("Sending service-info to " + ip);
				String url = URL_PREFIX + ip + urlEnding;
				restTemplate.postForObject(url, request, Void.class);
			} catch (RestClientException e)
			{
				log.warn("Could not reach hub because of exception: {}", e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}

	private static List<String> getBroadcastIps()
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
