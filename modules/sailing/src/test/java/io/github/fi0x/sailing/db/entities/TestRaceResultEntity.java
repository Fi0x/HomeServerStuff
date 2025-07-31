package io.github.fi0x.sailing.db.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TestRaceResultEntity
{
	private static final String RACE_NAME = "Race against time";
	private static final Long START_DATE = 10293847L;
	private static final String RACE_GROUP = "ORC 1";
	private static final String SKIPPER = "Joachim Herbst";
	private static final String SHIP_NAME = "Linda";
	private static final Integer POSITION = 3;
	private static final Double SCORE = 5.3;
	private static final String SHIP_CLASS = "Wilke 39";

	@Test
	void test_clone()
	{
		RaceResultEntity original = RaceResultEntity.builder().name(RACE_NAME).startDate(START_DATE)
													.raceGroup(RACE_GROUP).skipper(SKIPPER).shipName(SHIP_NAME)
													.position(POSITION).score(SCORE).shipClass(SHIP_CLASS).build();

		RaceResultEntity copy = original.clone();
		assertThat(copy).usingRecursiveComparison().isEqualTo(original);
		Assertions.assertNotSame(original, copy);
	}
}
