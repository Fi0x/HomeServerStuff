package io.github.fi0x.sailing.rest;


import io.github.fi0x.sailing.db.entities.CertificateEntity;
import io.github.fi0x.sailing.db.entities.RaceResultEntity;
import io.github.fi0x.sailing.logic.dto.M2sClass;
import io.github.fi0x.sailing.logic.dto.RaceInfoDto;
import io.github.fi0x.sailing.service.OrcService;
import io.github.fi0x.sailing.service.RaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RestController
{
	private final OrcService orcService;
	private final RaceService raceService;

	@GetMapping("/orc/add/{certificateId}")
	public CertificateEntity addOrcCertificate(@PathVariable String certificateId)
	{
		log.debug("addOrcCertificate() called with id {}", certificateId);

		return orcService.saveCertificate(certificateId);
	}

	@PostMapping("/orc/remove/{certificateId}")
	public void removeOrcCertificate(@PathVariable String certificateId)
	{
		log.debug("removeOrcCertificate() called with id {}", certificateId);

		orcService.removeCertificate(certificateId);
	}

	@PostMapping("/race/add")
	public List<RaceResultEntity> addRace(@RequestBody String raceUrl)
	{
		log.debug("Adding results for race " + raceUrl);

		String decodedUrl = URLDecoder.decode(raceUrl, StandardCharsets.UTF_8);
		return raceService.saveRace(decodedUrl);
	}

	@PostMapping("/race/load")
	public List<Object> getRaceResultsForClass(@RequestBody M2sClass raceClass)
	{
		log.debug("getRaceResultsForClass() called with class={}", raceClass);

		//TODO: Return list of correct object instead
		// Use this endpoint to get results for a specific raceClass
		// Do this for each raceClass the user has selected
		// Show the results to the user to select, which ones to keep and how to name the raceClass

		return raceService.loadSpecificRaceClassResults(raceClass);
	}

	@DeleteMapping("/race/remove/{raceName}/{startDate}/{raceGroup}")
	public void removeRaceResult(@PathVariable String raceName, @PathVariable Long startDate,
								 @PathVariable String raceGroup, @RequestParam(required = false) String skipper)
	{
		log.debug("removeRaceResult() called for race '{}', date '{}', group '{}', skipper '{}'", raceName, startDate,
				  raceGroup, skipper);

		raceService.deleteResult(raceName, startDate, raceGroup, skipper);
	}

	@PostMapping("/race/update/{raceName}/{startDate}/{raceGroup}")
	public void updateRace(@PathVariable String raceName, @PathVariable Long startDate, @PathVariable String raceGroup,
						   @RequestBody RaceInfoDto raceInfo)
	{
		log.debug("updateRace() called for race '{}', date '{}', group '{}', dto '{}'", raceName, startDate, raceGroup,
				  raceInfo);

		raceService.updateRace(raceName, startDate, raceGroup, raceInfo);
	}
}
