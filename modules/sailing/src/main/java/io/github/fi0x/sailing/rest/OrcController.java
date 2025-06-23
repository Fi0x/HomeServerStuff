package io.github.fi0x.sailing.rest;


import io.github.fi0x.sailing.db.OrcCertificateRepo;
import io.github.fi0x.sailing.db.entities.CertificateEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrcController
{
	private final OrcCertificateRepo certificateRepo;

	@GetMapping("/orc/add/{certificateId}")
	public CertificateEntity addOrcCertificate(@PathVariable String certificateId)
	{
		log.debug("addOrcCertificate() called with id {}", certificateId);

		Optional<CertificateEntity> entity = certificateRepo.findById(certificateId);
		if (entity.isPresent())
			return entity.get();

		String url = "https://data.orc.org/public/WPub.dll?action=activecerts&refNo=" + certificateId;
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(url, String.class);

		//TODO: Convert result to usable dto and gather remaining information for handicap numbers, then save in
		// database
		return CertificateEntity.builder().id(certificateId).build();
	}
}
