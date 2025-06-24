package io.github.fi0x.sailing.rest;

import io.github.fi0x.sailing.service.OrcService;
import io.github.fi0x.sailing.service.RaceService;
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
	private final RaceService raceService;

	@GetMapping("/orc")
	public String showOrcCertificates(ModelMap model)
	{
		log.info("showOrcCertificates() called");

		model.put("certificates", orcService.getAllCertificates());

		return "certificate-list";
	}

	@GetMapping("/")
	public String showMainPage()
	{
		log.info("showMainPage() called");

		return "main";
	}

	@GetMapping("/orc-race-results")
	public String showRaceResults(ModelMap model)
	{
		log.info("showRaceResults() called");

		model.put("races", raceService.getAllOrcRaces());
		model.put("raceResults", raceService.getAllResults());

		return "orc-race-results";
	}
}
