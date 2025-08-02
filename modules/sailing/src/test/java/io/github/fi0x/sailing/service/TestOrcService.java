package io.github.fi0x.sailing.service;

import io.github.fi0x.sailing.db.OrcCertificateRepo;
import io.github.fi0x.sailing.logic.converter.OrcInformationToEntityMerger;
import io.github.fi0x.sailing.logic.converter.StringToOrcOverviewConverter;
import io.github.fi0x.util.components.Authenticator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TestOrcService
{

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
		Assertions.fail();
	}

	@Test
	void test_saveCertificate()
	{
		Assertions.fail();
	}

	@Test
	void test_getCertificate()
	{
		Assertions.fail();
	}

	@Test
	void test_removeCertificate()
	{
		Assertions.fail();
	}
}
