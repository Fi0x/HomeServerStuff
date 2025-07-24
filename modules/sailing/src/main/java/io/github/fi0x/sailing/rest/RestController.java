package io.github.fi0x.sailing.rest;


import io.github.fi0x.sailing.db.entities.CertificateEntity;
import io.github.fi0x.sailing.logic.dto.RaceInfoDto;
import io.github.fi0x.sailing.logic.dto.RaceResultDto;
import io.github.fi0x.sailing.logic.dto.m2s.M2sClass;
import io.github.fi0x.sailing.service.OrcService;
import io.github.fi0x.sailing.service.RaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

	@PostMapping("/race/load")
	public List<RaceResultDto> getRaceResultsForClass(@RequestBody M2sClass raceClass)
	{
		log.debug("getRaceResultsForClass() called with class={}", raceClass);

		return raceService.loadSpecificRaceClassResults(raceClass);
	}

	@PostMapping("/race/save/info")
	public void saveRaceInfo(@RequestBody RaceInfoDto raceDto)
	{
		log.debug("saveRaceInfo() called with dto {}", raceDto);

		//TODO: verify and save race info (inputs might be invalid, so throw an error then)
		// startDate gets replaced by longDate
		// url will be empty and should stay empty
		throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "WIP, not done yet");
	}

	@PostMapping("/race/save")
	public void saveRaceResults(@RequestBody List<RaceResultDto> raceResultDtos)
	{
		log.debug("saveRaceResults() called with {} dtos", raceResultDtos.size());

		raceService.saveRaceResults(raceResultDtos);
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
