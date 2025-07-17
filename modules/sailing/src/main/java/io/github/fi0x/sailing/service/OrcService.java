package io.github.fi0x.sailing.service;

import io.github.fi0x.sailing.db.OrcCertificateRepo;
import io.github.fi0x.sailing.db.entities.CertificateEntity;
import io.github.fi0x.sailing.logic.converter.OrcInformationToEntityMerger;
import io.github.fi0x.sailing.logic.converter.StringToOrcOverviewConverter;
import io.github.fi0x.sailing.logic.dto.OrcOverviewXmlRowDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrcService
{
	private static final String ORC_URL = "https://data.orc.org/public/WPub.dll?action=activecerts&refNo=";
	private static final String ORC_DETAILS_URL = "https://data.orc.org/public/WPub.dll/CC/";

	private final OrcCertificateRepo orcRepo;
	private final StringToOrcOverviewConverter overviewConverter;
	private final OrcInformationToEntityMerger orcMerger;

	public List<CertificateEntity> getAllCertificates()
	{
		return orcRepo.findAll();
	}

	public CertificateEntity saveCertificate(String certificateId)
	{
		Optional<CertificateEntity> existingEntity = getCertificate(certificateId);
		if (existingEntity.isPresent())
			return existingEntity.get();

		String url = ORC_URL + certificateId;
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(url, String.class);

		OrcOverviewXmlRowDto overviewDto = overviewConverter.convert(result);
		if (overviewDto == null)
		{
			log.warn("Could not parse an overview result for certificate id '{}'", certificateId);
			return null;
		}

		try
		{
			Document page = Jsoup.connect(ORC_DETAILS_URL + overviewDto.getDxtID()).get();
			CertificateEntity entity = orcMerger.merge(overviewDto, page);
			orcRepo.save(entity);
			log.debug("Added new entity to database: {}", entity);
			return entity;
		} catch (IOException e)
		{
			log.warn("Could not load details for certificateId '{}'", certificateId);
			return null;
		}
	}

	public Optional<CertificateEntity> getCertificate(String certificateId)
	{
		return orcRepo.findById(certificateId);
	}

	public void removeCertificate(String certificateId)
	{
		orcRepo.deleteById(certificateId);
	}
}
