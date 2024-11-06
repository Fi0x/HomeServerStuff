package io.fi0x.hub.rest;

import io.fi0x.util.logic.dto.ServiceDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.SessionAttributes;


@Slf4j
@Controller
@RequiredArgsConstructor
@SessionAttributes({"username"})
public class ConnectionController
{
    @PostMapping("/api/service/register")
    public void registerService(@RequestBody ServiceDataDto requestDto)
    {
        log.info("registerService() called");
        //TODO: Register new service in a list to show on main-page
    }
}
