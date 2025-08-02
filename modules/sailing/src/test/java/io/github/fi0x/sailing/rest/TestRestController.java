package io.github.fi0x.sailing.rest;

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

@RunWith(SpringRunner.class)
public class TestRestController
{

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
		Assertions.fail();
	}

	@Test
	void test_removeOrcCertificate()
	{
		Assertions.fail();
	}

	@Test
	void test_getRaceResultsForClass()
	{
		Assertions.fail();
	}

	@Test
	void test_saveRaceInfo()
	{
		Assertions.fail();
	}

	@Test
	void test_saveRaceResults()
	{
		Assertions.fail();
	}

	@Test
	void test_removeRaceResult()
	{
		Assertions.fail();
	}

	@Test
	void test_updateRace()
	{
		Assertions.fail();
	}
}
