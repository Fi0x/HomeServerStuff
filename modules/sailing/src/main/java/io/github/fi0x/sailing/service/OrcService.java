package io.github.fi0x.sailing.service;

import io.github.fi0x.sailing.db.OrcCertificateRepo;
import io.github.fi0x.sailing.db.entities.CertificateEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrcService
{
	private final OrcCertificateRepo orcRepo;

	public List<CertificateEntity> getAllCertificates()
	{
		return orcRepo.findAll();
	}
}
