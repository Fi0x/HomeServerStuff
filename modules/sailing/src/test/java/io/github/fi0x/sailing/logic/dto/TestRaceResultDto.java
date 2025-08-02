package io.github.fi0x.sailing.logic.dto;

import io.github.fi0x.sailing.db.entities.RaceId;
import io.github.fi0x.sailing.logic.dto.m2s.RaceResultStatusCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestRaceResultDto
{
	private static final String NAME = "Final Race";
	private static final Long START_DATE = 1232938L;
	private static final String RACE_GROUP = "Yardstick 1";
	private static final String SKIPPER = "Karl Otto";
	private static final String PLACEHOLDER_STRING = "Should not be relevant";
	private static final Integer PLACEHOLDER_INT = 3;
	private static final Long PLACEHOLDER_LONG = 23982347L;
	private static final Double PLACEHOLDER_DOUBLE = 2134.34;
	private static final RaceResultStatusCode STATUS_CODE = RaceResultStatusCode.DNC;

	@Test
	void test_getRaceId()
	{
		RaceId expected = new RaceId(NAME, START_DATE, RACE_GROUP);
		RaceResultDto dto =
				RaceResultDto.builder().name(NAME).startDate(START_DATE).raceGroup(RACE_GROUP).skipper(SKIPPER)
							 .endDate(PLACEHOLDER_LONG).url(PLACEHOLDER_STRING).shipName(PLACEHOLDER_STRING)
							 .position(PLACEHOLDER_INT).resultStatusCode(STATUS_CODE).score(PLACEHOLDER_DOUBLE)
							 .shipClass(PLACEHOLDER_STRING).crossed(null).build();

		Assertions.assertEquals(expected, dto.getRaceId());
	}
}
