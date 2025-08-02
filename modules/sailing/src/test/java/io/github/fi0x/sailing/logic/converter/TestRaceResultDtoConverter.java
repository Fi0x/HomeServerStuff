package io.github.fi0x.sailing.logic.converter;

import io.github.fi0x.sailing.db.entities.RaceResultEntity;
import io.github.fi0x.sailing.logic.dto.RaceResultDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TestRaceResultDtoConverter
{
	private static final String NAME = "Vendee Globe";
	private static final Long START_LONG = 11298389L;
	private static final String RACE_GROUP = "IMOCA";
	private static final String SKIPPER = "Alex Thomson";
	private static final String SHIP_NAME = "Boss";
	private static final Integer POSITION = 2;
	private static final Double SCORE = 343.8;
	private static final String SHIP_CLASS = "OPEN 60";
	private static final Boolean CROSSED = false;

	@InjectMocks
	private RaceResultToDtoConverter converter;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void test_convert_success()
	{
		RaceResultDto expected =
				RaceResultDto.builder().name(NAME).startDate(START_LONG).raceGroup(RACE_GROUP).skipper(SKIPPER)
							 .shipName(SHIP_NAME).position(POSITION).score(SCORE).shipClass(SHIP_CLASS).crossed(CROSSED)
							 .build();
		RaceResultEntity input =
				RaceResultEntity.builder().name(NAME).startDate(START_LONG).raceGroup(RACE_GROUP).skipper(SKIPPER)
								.shipName(SHIP_NAME).position(POSITION).score(SCORE).shipClass(SHIP_CLASS).build();

		Assertions.assertEquals(RaceResultDto.builder().crossed(false).build(),
								converter.convert(new RaceResultEntity()));
		Assertions.assertEquals(expected, converter.convert(input));
	}
}
