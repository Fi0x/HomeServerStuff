package io.github.fi0x.sailing.service;

import io.github.fi0x.sailing.db.OrcCertificateRepo;
import io.github.fi0x.sailing.db.entities.CertificateEntity;
import io.github.fi0x.sailing.logic.converter.OrcInformationToEntityMerger;
import io.github.fi0x.sailing.logic.converter.StringToOrcOverviewConverter;
import io.github.fi0x.sailing.logic.dto.orc.OrcOverviewXmlRowDto;
import io.github.fi0x.util.components.Authenticator;
import io.github.fi0x.util.dto.UserRoles;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class TestOrcService
{
	private static final String OVERVIEW_PAGE_URL = "https://data.orc.org/public/WPub.dll/CC/";
	private static final String CERT_ID = "Sdflk4j3";
	private static final String REQUEST_RESULT = "{some restdata}";

	private MockedStatic<Jsoup> staticMock;
	@Mock
	private Connection connection;
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

		staticMock = mockStatic(Jsoup.class, Mockito.CALLS_REAL_METHODS);
		staticMock.when(() -> Jsoup.connect(OVERVIEW_PAGE_URL + CERT_ID)).thenReturn(connection);
	}

	@AfterEach
	void cleanup()
	{
		staticMock.close();
	}

	@Test
	void test_getAllCertificates()
	{
		Assertions.assertDoesNotThrow(() -> service.getAllCertificates());
		verify(orcRepo, times(1)).findAll();
	}

	@Test
	void test_saveCertificate_success() throws IOException
	{
		when(orcRepo.findById(CERT_ID)).thenReturn(Optional.empty());
		ReflectionTestUtils.setField(service, "restTemplate", restTemplate);
		when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(REQUEST_RESULT);
		OrcOverviewXmlRowDto rowDto = OrcOverviewXmlRowDto.builder().dxtID(CERT_ID).build();
		when(overviewConverter.convert(REQUEST_RESULT)).thenReturn(rowDto);
		Document page = new Document("asdf");
		when(connection.get()).thenReturn(page);
		CertificateEntity mergedEntity = CertificateEntity.builder().build();
		when(orcMerger.merge(rowDto, page)).thenReturn(mergedEntity);

		Assertions.assertSame(mergedEntity, service.saveCertificate(CERT_ID));
		verify(authenticator, times(1)).restAuthenticate(UserRoles.ADMIN);
		verify(orcRepo, times(1)).findById(CERT_ID);
		verify(restTemplate, times(1)).getForObject(anyString(), eq(String.class));
		verify(overviewConverter, times(1)).convert(REQUEST_RESULT);
		verify(connection, times(1)).get();
		verify(orcMerger, times(1)).merge(rowDto, page);
		verify(orcRepo, times(1)).save(mergedEntity);
	}

	@Test
	void test_saveCertificate_noAuthorization() throws IOException
	{
		doThrow(new ResponseStatusException(HttpStatus.FORBIDDEN)).when(authenticator)
				.restAuthenticate(UserRoles.ADMIN);

		Assertions.assertThrows(ResponseStatusException.class, () -> service.saveCertificate(CERT_ID));
		verify(authenticator, times(1)).restAuthenticate(UserRoles.ADMIN);
		verify(orcRepo, never()).findById(CERT_ID);
		verify(restTemplate, never()).getForObject(anyString(), eq(String.class));
		verify(overviewConverter, never()).convert(REQUEST_RESULT);
		verify(connection, never()).get();
		verify(orcMerger, never()).merge(any(), any());
		verify(orcRepo, never()).save(any());
	}

	@Test
	void test_saveCertificate_alreadyExists() throws IOException
	{
		Optional<CertificateEntity> existingEntity = Optional.of(CertificateEntity.builder().build());
		when(orcRepo.findById(CERT_ID)).thenReturn(existingEntity);

		Assertions.assertSame(existingEntity.get(), service.saveCertificate(CERT_ID));
		verify(authenticator, times(1)).restAuthenticate(UserRoles.ADMIN);
		verify(orcRepo, times(1)).findById(CERT_ID);
		verify(restTemplate, never()).getForObject(anyString(), eq(String.class));
		verify(overviewConverter, never()).convert(REQUEST_RESULT);
		verify(connection, never()).get();
		verify(orcMerger, never()).merge(any(), any());
		verify(orcRepo, never()).save(any());
	}

	@Test
	void test_saveCertificate_noConversionPossible() throws IOException
	{
		when(orcRepo.findById(CERT_ID)).thenReturn(Optional.empty());
		ReflectionTestUtils.setField(service, "restTemplate", restTemplate);
		when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(REQUEST_RESULT);
		OrcOverviewXmlRowDto rowDto = OrcOverviewXmlRowDto.builder().dxtID(CERT_ID).build();
		when(overviewConverter.convert(REQUEST_RESULT)).thenReturn(null);

		Assertions.assertNull(service.saveCertificate(CERT_ID));
		verify(authenticator, times(1)).restAuthenticate(UserRoles.ADMIN);
		verify(orcRepo, times(1)).findById(CERT_ID);
		verify(restTemplate, times(1)).getForObject(anyString(), eq(String.class));
		verify(overviewConverter, times(1)).convert(REQUEST_RESULT);
		verify(connection, never()).get();
		verify(orcMerger, never()).merge(eq(rowDto), any());
		verify(orcRepo, never()).save(any());
	}

	@Test
	void test_saveCertificate_jsoupIOException() throws IOException
	{
		when(orcRepo.findById(CERT_ID)).thenReturn(Optional.empty());
		ReflectionTestUtils.setField(service, "restTemplate", restTemplate);
		when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(REQUEST_RESULT);
		OrcOverviewXmlRowDto rowDto = OrcOverviewXmlRowDto.builder().dxtID(CERT_ID).build();
		when(overviewConverter.convert(REQUEST_RESULT)).thenReturn(rowDto);
		Document page = new Document("asdf");
		when(connection.get()).thenThrow(new IOException());

		Assertions.assertNull(service.saveCertificate(CERT_ID));
		verify(authenticator, times(1)).restAuthenticate(UserRoles.ADMIN);
		verify(orcRepo, times(1)).findById(CERT_ID);
		verify(restTemplate, times(1)).getForObject(anyString(), eq(String.class));
		verify(overviewConverter, times(1)).convert(REQUEST_RESULT);
		verify(connection, times(1)).get();
		verify(orcMerger, never()).merge(rowDto, page);
		verify(orcRepo, never()).save(any());
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
