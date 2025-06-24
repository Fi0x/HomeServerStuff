package io.github.fi0x.sailing.rest;

import io.github.fi0x.sailing.service.OrcService;
import io.github.fi0x.sailing.service.RaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String showMainPage(ModelMap model)
	{
		log.info("showMainPage() called");

		model.put("raceGroups", raceService.getAllOrcRaceGroups());

		return "main";
	}

	@GetMapping("/race-results/orc")
	public String showOrcRaceResults(ModelMap model, @RequestParam(required = false) String group)
	{
		log.info("showOrcRaceResults() called");

		model.put("races", raceService.getAllOrcRaces(group));
		model.put("raceResults", raceService.getAllResults(group));

		return "race-results";
	}

	@GetMapping("/race-results")
	public String showRaceResults(ModelMap model)
	{
		log.info("showRaceResults() called");

		model.put("races", raceService.getAllRaces());
		model.put("raceResults", raceService.getAllResults(null));

		return "race-results";
	}
}
