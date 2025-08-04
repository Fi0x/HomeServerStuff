package io.github.fi0x.sailing.db.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TestRaceEntity
{
	private static final String RACE_NAME = "Race against time";
	private static final Long START_DATE = 10293847L;
	private static final String RACE_GROUP = "Esse 850";

	@Test
	void test_getId()
	{
		RaceId expected = new RaceId(RACE_NAME, START_DATE, RACE_GROUP);
		RaceEntity entity = RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP).build();

		Assertions.assertEquals(expected, entity.getId());
	}

	@Test
	void test_getGroupAndYear()
	{
		Calendar c = new GregorianCalendar();
		c.setTime(new Date(START_DATE));
		String expected = RACE_GROUP + " - " + c.get(Calendar.YEAR);
		RaceEntity entity = RaceEntity.builder().raceGroup(RACE_GROUP).startDate(START_DATE).build();

		Assertions.assertEquals(expected, entity.getGroupAndYear());
	}

	@Test
	void test_getBooleans()
	{
		RaceEntity entity11 = RaceEntity.builder().orcRace(true).bufferRace(true).build();
		RaceEntity entity01 = RaceEntity.builder().orcRace(true).bufferRace(false).build();
		RaceEntity entity10 = RaceEntity.builder().orcRace(false).bufferRace(true).build();
		RaceEntity entity00 = RaceEntity.builder().orcRace(false).bufferRace(false).build();

		Assertions.assertEquals(3, entity11.getBooleans());
		Assertions.assertEquals(2, entity10.getBooleans());
		Assertions.assertEquals(1, entity01.getBooleans());
		Assertions.assertEquals(0, entity00.getBooleans());
	}
}
