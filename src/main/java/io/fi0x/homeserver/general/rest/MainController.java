package io.fi0x.homeserver.general.rest;

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
    private String languageGeneratorPort;

    @GetMapping("/")
    public String showHomePage(ModelMap model)
    {
        //TODO: Check if the language generator is online and can be reached (use provided ip and port)
        //TODO: If language generator is reachable, add a button to the main-page to open the language-generator
        return "main-page";
    }
}
