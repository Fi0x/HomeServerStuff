package io.fi0x.homeserver.general.rest;

import io.fi0x.homeserver.general.components.Authenticator;
import io.fi0x.homeserver.general.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
@SessionAttributes({"username"})
public class MainController
{
    @Value("${homeserver.languagegenerator.ip}")
    private String languageGeneratorIp;
    private final ConnectionService connectionService;
    @Value("${homeserver.languagegenerator.port}")
    private Integer languageGeneratorPort;

    private final Authenticator authenticator;

    @GetMapping("/")
    public String showHomePage(ModelMap model)
    {
        model.put("username", authenticator.getAuthenticatedUsername());

        boolean languageGeneratorReachable = connectionService.isReachable(languageGeneratorIp, languageGeneratorPort);
        if (languageGeneratorReachable)
            model.put("languageGeneratorAddress", languageGeneratorIp + ":" + languageGeneratorPort);

        return "main-page";
    }

    @GetMapping("/*")
    public String redirectHomePage()
    {
        return "redirect:/";
    }
}
