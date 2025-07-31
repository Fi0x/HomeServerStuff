package io.github.fi0x.sailing.components;

import io.github.fi0x.sailing.db.RaceRepo;
import io.github.fi0x.sailing.db.RaceResultRepo;
import io.github.fi0x.sailing.db.entities.RaceResultEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class TestRaceCleanup
{
	@Mock
	private RaceRepo raceRepo;
	@Mock
	private RaceResultRepo resultRepo;
	@InjectMocks
	private RaceCleanup component;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void test_cleanDatabase_successEmpty()
	{
		Assertions.assertDoesNotThrow(() -> component.cleanDatabase());
		verify(raceRepo, times(1)).findAll();
		verify(resultRepo, times(1)).findAll();
		verify(resultRepo, times(1)).deleteAll(Collections.emptyList());
	}

	@Test
	void test_cleanDatabase_success()
	{
		when(resultRepo.findAll()).thenReturn(List.of(RaceResultEntity.builder().build()));

		Assertions.assertDoesNotThrow(() -> component.cleanDatabase());
		verify(raceRepo, times(1)).findAll();
		verify(resultRepo, times(1)).findAll();
		verify(resultRepo, times(1)).deleteAll(any());
	}
}
