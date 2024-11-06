package io.fi0x.hub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectionService
{
    private final Map<InetSocketAddress, Boolean> reachableUrls = new HashMap<>();
    @Value("${homeserver.network.delay}")
    private Integer networkDelay;

    public boolean isReachable(String url, Integer port)
    {
        InetSocketAddress address = new InetSocketAddress(url, port);
        if (!reachableUrls.containsKey(address))
            reachableUrls.put(address, false);

        return reachableUrls.get(address);
    }

    @Scheduled(fixedRate = 10000)
    public void checkUrlReachability()
    {
        log.info("Scheduled task running");
        for (InetSocketAddress address : reachableUrls.keySet())
        {
            try (Socket socket = new Socket())
            {
                socket.connect(address, networkDelay);
                reachableUrls.put(address, true);
            } catch (IOException e)
            {
                reachableUrls.put(address, false);
            }
        }
        log.info("Scheduled task finished");
    }
}
