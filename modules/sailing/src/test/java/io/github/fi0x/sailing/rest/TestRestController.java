package io.github.fi0x.sailing.rest;

import io.github.fi0x.sailing.logic.dto.RaceInfoDto;
import io.github.fi0x.sailing.logic.dto.RaceResultDto;
import io.github.fi0x.sailing.logic.dto.m2s.M2sClass;
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

import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class TestRestController
{
	private static final String CERT_ID = "sdf2334lk";
	private static final String RACE_NAME = "Opti-WM";
	private static final Long START_DATE = 234089L;
	private static final String RACE_GROUP = "Juniors";
	private static final String SKIPPER = "Valon";

	@Mock
	private OrcService orcService;
	@Mock
	private RaceService raceService;

	@InjectMocks
	private RestController controller;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void test_addOrcCertificate()
	{
		Assertions.assertDoesNotThrow(() -> controller.addOrcCertificate(CERT_ID));
		verify(orcService, times(1)).saveCertificate(CERT_ID);
	}

	@Test
	void test_removeOrcCertificate()
	{
		Assertions.assertDoesNotThrow(() -> controller.removeOrcCertificate(CERT_ID));
		verify(orcService, times(1)).removeCertificate(CERT_ID);
	}

	@Test
	void test_getRaceResultsForClass()
	{
		List<RaceResultDto> expected = List.of(RaceResultDto.builder().build());
		when(raceService.loadSpecificRaceClassResults(any())).thenReturn(expected);
		M2sClass input = M2sClass.builder().build();

		Assertions.assertSame(expected, controller.getRaceResultsForClass(input));
	}

	@Test
	void test_saveRaceInfo()
	{
		RaceInfoDto input = RaceInfoDto.builder().build();

		Assertions.assertDoesNotThrow(() -> controller.saveRaceInfo(input));
		verify(raceService, times(1)).saveRaceInformation(input);
	}

	@Test
	void test_saveRaceResults()
	{
		List<RaceResultDto> input = List.of(RaceResultDto.builder().build());

		Assertions.assertDoesNotThrow(() -> controller.saveRaceResults(input));
		verify(raceService, times(1)).saveRaceResults(input);
	}

	@Test
	void test_removeRaceResult()
	{
		Assertions.assertDoesNotThrow(() -> controller.removeRaceResult(RACE_NAME, START_DATE, RACE_GROUP, SKIPPER));
		verify(raceService, times(1)).deleteResult(RACE_NAME, START_DATE, RACE_GROUP, SKIPPER);
	}

	@Test
	void test_updateRace()
	{
		RaceInfoDto input = RaceInfoDto.builder().build();

		Assertions.assertDoesNotThrow(() -> controller.updateRace(RACE_NAME, START_DATE, RACE_GROUP, input));
		verify(raceService, times(1)).updateRace(RACE_NAME, START_DATE, RACE_GROUP, input);
	}
}
