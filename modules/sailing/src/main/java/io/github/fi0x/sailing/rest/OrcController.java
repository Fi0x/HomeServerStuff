package io.github.fi0x.sailing.rest;


import io.github.fi0x.sailing.db.entities.CertificateEntity;
import io.github.fi0x.sailing.service.OrcService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrcController
{
	private final OrcService orcService;

	@GetMapping("/orc/add/{certificateId}")
	public CertificateEntity addOrcCertificate(@PathVariable String certificateId)
	{
		log.info("addOrcCertificate() called with id {}", certificateId);

		return orcService.saveCertificate(certificateId);
	}
}
