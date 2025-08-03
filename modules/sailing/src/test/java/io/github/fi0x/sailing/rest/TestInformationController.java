package io.github.fi0x.sailing.rest;

import io.github.fi0x.sailing.db.entities.CertificateEntity;
import io.github.fi0x.sailing.db.entities.RaceEntity;
import io.github.fi0x.sailing.logic.dto.RaceInfoDto;
import io.github.fi0x.sailing.logic.dto.RaceResultDto;
import io.github.fi0x.sailing.logic.dto.ShipRaceResults;
import io.github.fi0x.sailing.logic.dto.m2s.M2sClass;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class TestInformationController
{
	private static final CertificateType CERTIFICATE_TYPE = CertificateType.CLUB;
	private static final String COUNTRY = "GER";
	private static final String RACE_GROUP = "ORC 2";
	private static final Integer YEAR = 2025;
	private static final String RACE_URL = "somem2surl.com";
	private static final String RACE_NAME = "Blubber";
	private static final Long START_DATE = 2345234L;

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
		List<String> expectedRaceGroups = List.of(RACE_GROUP);
		when(raceService.getAllRaceGroupsWithYear()).thenReturn(expectedRaceGroups);
		ModelMap map = new ModelMap();

		Assertions.assertDoesNotThrow(() -> controller.showMainPage(map));
		Assertions.assertSame(expectedRaceGroups, map.get("raceGroups"));
	}

	@Test
	void test_showOrcRaceResults()
	{
		List<RaceEntity> expectedRaces = List.of(RaceEntity.builder().build());
		List<ShipRaceResults> expectedResults = List.of(ShipRaceResults.builder().build());
		when(raceService.getAllOrcRaces(RACE_GROUP, YEAR)).thenReturn(expectedRaces);
		when(raceService.getAllResults(RACE_GROUP, YEAR)).thenReturn(expectedResults);
		ModelMap map = new ModelMap();

		Assertions.assertDoesNotThrow(() -> controller.showOrcRaceResults(map, RACE_GROUP, YEAR));
		Assertions.assertSame(expectedRaces, map.get("races"));
		Assertions.assertSame(expectedResults, map.get("raceResults"));
	}

	@Test
	void test_showRaceResults()
	{
		List<RaceEntity> expectedRaces = List.of(RaceEntity.builder().build());
		List<ShipRaceResults> expectedResults = List.of(ShipRaceResults.builder().build());
		when(raceService.getAllRaces(RACE_GROUP, YEAR)).thenReturn(expectedRaces);
		when(raceService.getAllResults(RACE_GROUP, YEAR)).thenReturn(expectedResults);
		ModelMap map = new ModelMap();

		Assertions.assertDoesNotThrow(() -> controller.showRaceResults(map, RACE_GROUP, YEAR));
		Assertions.assertSame(expectedRaces, map.get("races"));
		Assertions.assertSame(expectedResults, map.get("raceResults"));
	}

	@Test
	void test_showRaceList()
	{
		List<RaceInfoDto> expectedRaces = List.of(RaceInfoDto.builder().build());
		when(raceService.getAllRaces()).thenReturn(expectedRaces);
		ModelMap map = new ModelMap();

		Assertions.assertDoesNotThrow(() -> controller.showRaceList(map));
		Assertions.assertSame(expectedRaces, map.get("races"));
	}

	@Test
	void test_getClassListForRace()
	{
		List<M2sClass> expectedRaceClasses = List.of(M2sClass.builder().build());
		when(raceService.getRaceClasses(anyString())).thenReturn(expectedRaceClasses);
		ModelMap map = new ModelMap();

		Assertions.assertDoesNotThrow(() -> controller.getClassListForRace(map, RACE_URL));
		Assertions.assertSame(expectedRaceClasses, map.get("raceClasses"));
	}

	@Test
	void test_addNewRaceManually()
	{
		Assertions.assertDoesNotThrow(() -> controller.addNewRaceManually());
	}

	@Test
	void test_editRaceResultsManually()
	{
		RaceEntity expectedInfo = RaceEntity.builder().build();
		List<RaceResultDto> expectedResults = List.of(RaceResultDto.builder().build());
		when(raceService.getRace(RACE_NAME, START_DATE, RACE_GROUP)).thenReturn(expectedInfo);
		when(raceService.getAllResults(RACE_NAME, START_DATE, RACE_GROUP)).thenReturn(expectedResults);
		ModelMap map = new ModelMap();

		Assertions.assertDoesNotThrow(() -> controller.editRaceResultsManually(map, RACE_NAME, START_DATE, RACE_GROUP));
		Assertions.assertSame(expectedInfo, map.get("raceInfo"));
		Assertions.assertSame(expectedResults, map.get("raceResults"));
	}
}
