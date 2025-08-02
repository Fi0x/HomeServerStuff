package io.github.fi0x.sailing.rest;

import io.github.fi0x.sailing.db.entities.CertificateEntity;
import io.github.fi0x.sailing.logic.dto.orc.CertificateType;
import io.github.fi0x.sailing.service.OrcService;
import io.github.fi0x.sailing.service.RaceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.ModelMap;

import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class TestInformationController
{
	private static final CertificateType CERTIFICATE_TYPE = CertificateType.CLUB;
	private static final String COUNTRY = "GER";

	@Mock
	private OrcService orcService;
	@Mock
	private RaceService raceService;

	@InjectMocks
	private InformationController controller;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void test_showOrcCertificates()
	{
		List<CertificateEntity> expectedCertificates =
				List.of(CertificateEntity.builder().certificateType(CERTIFICATE_TYPE).country(COUNTRY).build());
		when(orcService.getAllCertificates()).thenReturn(expectedCertificates);
		ModelMap map = new ModelMap();

		Assertions.assertDoesNotThrow(() -> controller.showOrcCertificates(map));
		Assertions.assertSame(expectedCertificates, map.get("certificates"));
		Assertions.assertEquals(List.of(CERTIFICATE_TYPE), map.getAttribute("certificateTypes"));
		Assertions.assertEquals(List.of(COUNTRY), map.getAttribute("countries"));
	}

	@Test
	void test_showMainPage()
	{
		Assertions.fail();
	}

	@Test
	void test_showOrcRaceResults()
	{
		Assertions.fail();
	}

	@Test
	void test_showRaceResults()
	{
		Assertions.fail();
	}

	@Test
	void test_showRaceList()
	{
		Assertions.fail();
	}

	@Test
	void test_getClassListForRace()
	{
		Assertions.fail();
	}

	@Test
	void test_addNewRaceManually()
	{
		Assertions.fail();
	}

	@Test
	void test_editRaceResultsManually()
	{
		Assertions.fail();
	}
}
