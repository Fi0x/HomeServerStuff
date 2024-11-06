package io.fi0x.hub.rest;


import io.fi0x.hub.service.ConnectionService;
import io.fi0x.util.components.Authenticator;
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
    @Value("${homeserver.languagegenerator.port}")
    private Integer languageGeneratorPort;

    private final ConnectionService connectionService;

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
