package io.github.fi0x.sailing.rest;

import io.github.fi0x.sailing.service.OrcService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class InformationController
{
	private final OrcService orcService;

	@GetMapping("/orc")
	public String showOrcCertificates(ModelMap model)
	{
		log.info("showOrcCertificates() called");

		model.put("certificates", orcService.getAllCertificates());

		return "certificate-list";
	}

	@GetMapping("/")
	public String showMainPage(ModelMap model)
	{
		return "main";
	}
}
