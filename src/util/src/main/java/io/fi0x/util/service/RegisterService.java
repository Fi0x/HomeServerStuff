package io.fi0x.util.service;

import io.fi0x.util.logic.dto.ServiceDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterService
{
    @Value("${homeserver.hub.ip}")
    private String hubIp;
    @Value("${homeserver.hub.port}")
    private Integer hubPort;
    @Value("${homeserver.service.name}")
    private String serviceName;
    @Value("${server.port}")
    private Integer currentPort;

    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRate = 60000)
    public void registerInHub()
    {
        String url = "http://" + hubIp + ":" + hubPort + "/api/service/register";
        ServiceDataDto requestDto = ServiceDataDto.builder().name(serviceName).port(currentPort).build();

        restTemplate.postForObject(url, requestDto, Void.class);
    }
}
