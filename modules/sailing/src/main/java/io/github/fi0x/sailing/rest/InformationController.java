package io.github.fi0x.sailing.rest;

import io.github.fi0x.sailing.db.entities.CertificateEntity;
import io.github.fi0x.sailing.service.OrcService;
import io.github.fi0x.sailing.service.RaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

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

		List<CertificateEntity> certificates = orcService.getAllCertificates();
		model.put("certificates", certificates);
		model.put("certificateTypes",
				  certificates.stream().map(CertificateEntity::getCertificateType).distinct().toList());
		model.put("countries", certificates.stream().map(CertificateEntity::getCountry).distinct().toList());

		return "certificate-list";
	}

	@GetMapping("/")
	public String showMainPage(ModelMap model)
	{
		log.info("showMainPage() called");

		model.put("raceGroups", raceService.getAllRaceGroupsWithYear());

		return "main";
	}

	@GetMapping("/race-results/orc")
	public String showOrcRaceResults(ModelMap model, @RequestParam(required = false) String group,
									 @RequestParam(required = false) Integer year)
	{
		log.info("showOrcRaceResults() called");

		model.put("races", raceService.getAllOrcRaces(group, year));
		model.put("raceResults", raceService.getAllResults(group, year));

		return "race-results";
	}

	@GetMapping("/race-results")
	public String showRaceResults(ModelMap model, @RequestParam(required = false) String group,
								  @RequestParam(required = false) Integer year)
	{
		log.info("showRaceResults() called");

		model.put("races", raceService.getAllRaces(group, year));
		model.put("raceResults", raceService.getAllResults(group, year));

		return "race-results";
	}

	@GetMapping("/race-list")
	public String showRaceList(ModelMap model)
	{
		log.info("showRaceList() called");

		model.put("races", raceService.getAllRaces());

		return "race-list";
	}

	@GetMapping("/race/new")
	public String getClassListForRace(ModelMap map, @RequestParam String raceOverviewUrl)
	{
		log.info("getClassListForRace() called for URL={}", raceOverviewUrl);

		String decodedUrl = URLDecoder.decode(raceOverviewUrl, StandardCharsets.UTF_8);
		map.put("raceClasses", raceService.getRaceClasses(decodedUrl));

		return "new-race-class-list";
	}

	@GetMapping("/race/manual")
	public String addNewRaceManually()
	{
		log.info("addNewRaceManually() called");

		return "add-race-manually";
	}

	@GetMapping("/race/{raceName}/{startDate}/{raceGroup}/edit")
	public String editRaceResultsManually(ModelMap map, @PathVariable String raceName, @PathVariable Long startDate,
										  @PathVariable String raceGroup)
	{
		log.info("editRaceResultsManually() called");

		map.put("raceInfo", raceService.getRace(raceName, startDate, raceGroup));
		map.put("raceResults", raceService.getAllResults(raceName, startDate, raceGroup));

		return "modify-race-results";
	}
}
