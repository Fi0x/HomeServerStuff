package io.github.fi0x.sailing.rest;


import io.github.fi0x.sailing.db.entities.CertificateEntity;
import io.github.fi0x.sailing.db.entities.RaceResultEntity;
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

	@PostMapping("/race/add")
	public List<RaceResultEntity> addRace(@RequestBody String raceUrl)
	{
		log.debug("Adding results for race " + raceUrl);

		String decodedUrl = URLDecoder.decode(raceUrl, StandardCharsets.UTF_8);
		return raceService.saveRace(decodedUrl);
	}
}
