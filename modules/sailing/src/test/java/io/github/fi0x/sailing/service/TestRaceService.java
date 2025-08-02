package io.github.fi0x.sailing.service;

import io.github.fi0x.sailing.components.Manage2SailRetriever;
import io.github.fi0x.sailing.db.RaceRepo;
import io.github.fi0x.sailing.db.RaceResultRepo;
import io.github.fi0x.sailing.logic.converter.RaceConverter;
import io.github.fi0x.sailing.logic.converter.RaceResultToDtoConverter;
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
public class TestRaceService
{
	@Mock
	private Authenticator authenticator;
	@Mock
	private RaceRepo raceRepo;
	@Mock
	private RaceResultRepo resultRepo;
	@Mock
	private RaceResultToDtoConverter raceResultConverter;
	@Mock
	private RaceConverter raceConverter;
	@Mock
	private Manage2SailRetriever m2sRetriever;

	@InjectMocks
	private RaceService service;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void test_getRace()
	{
		Assertions.fail();
	}

	@Test
	void test_getAllOrcRaces()
	{
		Assertions.fail();
	}

	@Test
	void test_getAllRaces()
	{
		Assertions.fail();
	}

	@Test
	void test_getAllRaces_withGroupAndYear()
	{
		Assertions.fail();
	}

	@Test
	void test_getAllRaceGroupsWithYear()
	{
		Assertions.fail();
	}

	@Test
	void test_getAllResults_withGroup()
	{
		Assertions.fail();
	}

	@Test
	void test_getAllResults()
	{
		Assertions.fail();
	}

	@Test
	void test_getRaceClasses()
	{
		Assertions.fail();
	}

	@Test
	void test_loadSpecificRaceClassResults()
	{
		Assertions.fail();
	}

	@Test
	void test_saveRaceInformation()
	{
		Assertions.fail();
	}

	@Test
	void test_saveRaceResults()
	{
		Assertions.fail();
	}

	@Test
	void test_deleteResult()
	{
		Assertions.fail();
	}

	@Test
	void test_updateRace()
	{
		Assertions.fail();
	}
}
