package io.github.fi0x.sailing.service;

import io.github.fi0x.sailing.db.OrcCertificateRepo;
import io.github.fi0x.sailing.db.entities.CertificateEntity;
import io.github.fi0x.sailing.logic.converter.OrcInformationToEntityMerger;
import io.github.fi0x.sailing.logic.converter.StringToOrcOverviewConverter;
import io.github.fi0x.sailing.logic.dto.orc.OrcOverviewXmlRowDto;
import io.github.fi0x.util.components.Authenticator;
import io.github.fi0x.util.dto.UserRoles;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class TestOrcService
{
	private static final String CERT_ID = "Sdflk4j3";
	private static final String REQUEST_RESULT = "{some restdata}";

	@Mock
	private RestTemplate restTemplate;
	@Mock
	private Authenticator authenticator;
	@Mock
	private OrcCertificateRepo orcRepo;
	@Mock
	private StringToOrcOverviewConverter overviewConverter;
	@Mock
	private OrcInformationToEntityMerger orcMerger;

	@InjectMocks
	private OrcService service;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void test_getAllCertificates()
	{
		Assertions.assertDoesNotThrow(() -> service.getAllCertificates());
		verify(orcRepo, times(1)).findAll();
	}

	@Test
	void test_saveCertificate_success()
	{
		Optional<CertificateEntity> existingEntity = Optional.of(CertificateEntity.builder().build());
		when(orcRepo.findById(CERT_ID)).thenReturn(existingEntity);
		ReflectionTestUtils.setField(service, "restTemplate", restTemplate);
		when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(REQUEST_RESULT);
		OrcOverviewXmlRowDto rowDto = OrcOverviewXmlRowDto.builder().build();
		when(overviewConverter.convert(REQUEST_RESULT)).thenReturn(rowDto);
		Document page = new Document("asdf");
		//TODO: Static mock for Jsoup.connect() required to return page
		CertificateEntity mergedEntity = CertificateEntity.builder().build();
		when(orcMerger.merge(rowDto, page)).thenReturn(mergedEntity);

		Assertions.assertSame(mergedEntity, service.saveCertificate(CERT_ID));
		verify(authenticator, times(1)).restAuthenticate(UserRoles.ADMIN);
		verify(orcRepo, times(1)).findById(CERT_ID);
		verify(restTemplate, times(1)).getForObject(anyString(), eq(String.class));
		verify(overviewConverter, times(1)).convert(REQUEST_RESULT);
		//TODO: Verify call to Jsoup.connect()
		verify(orcMerger, times(1)).merge(rowDto, page);
		verify(orcRepo, times(1)).save(mergedEntity);
	}

	@Test
	void test_saveCertificate_noAuthorization()
	{
		Assertions.fail();
	}

	@Test
	void test_saveCertificate_alreadyExists()
	{
		Assertions.fail();
	}

	@Test
	void test_saveCertificate_noConversionPossible()
	{
		Assertions.fail();
	}

	@Test
	void test_saveCertificate_jsoupIOException()
	{
		Assertions.fail();
	}

	@Test
	void test_getCertificate()
	{
		Assertions.assertDoesNotThrow(() -> service.getCertificate(CERT_ID));
		verify(orcRepo, times(1)).findById(CERT_ID);
	}

	@Test
	void test_removeCertificate_success()
	{
		Assertions.assertDoesNotThrow(() -> service.removeCertificate(CERT_ID));
		verify(authenticator, times(1)).restAuthenticate(UserRoles.ADMIN);
		verify(orcRepo, times(1)).deleteById(CERT_ID);
	}

	@Test
	void test_removeCertificate_noAuthorization()
	{
		doThrow(new ResponseStatusException(HttpStatus.FORBIDDEN)).when(authenticator)
																  .restAuthenticate(UserRoles.ADMIN);

		Assertions.assertThrows(ResponseStatusException.class, () -> service.removeCertificate(CERT_ID));
		verify(authenticator, times(1)).restAuthenticate(UserRoles.ADMIN);
		verify(orcRepo, times(0)).deleteById(CERT_ID);
	}
}
