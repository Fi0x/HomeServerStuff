package io.github.fi0x.hub.rest;


import io.github.fi0x.hub.service.ConnectionService;
import io.github.fi0x.util.components.Authenticator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
	private final ConnectionService connectionService;

	private final Authenticator authenticator;

	@GetMapping({"/"})
	public String showHomePage(ModelMap model)
	{
		log.info("showHomePage() called");

		model.put("username", authenticator.getAuthenticatedUsername());
		model.put("services", connectionService.getConnectedServices());

		return "main-page";
	}
}
