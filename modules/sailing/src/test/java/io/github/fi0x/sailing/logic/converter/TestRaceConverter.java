package io.github.fi0x.sailing.logic.converter;

import io.github.fi0x.sailing.db.entities.RaceEntity;
import io.github.fi0x.sailing.logic.dto.RaceInfoDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
public class TestRaceConverter
{
	private static final String NAME = "Vendee Globe";
	private static final Long START_LONG = 11298389L;
	private static final Date START_DATE = new Date(START_LONG);
	private static final String RACE_GROUP = "IMOCA";
	private static final Double SCORE_MOD = 1.2;
	private static final Boolean ORC_RACE = false;
	private static final Boolean BUFFER_RACE = true;
	private static final Integer PARTICIPANTS = 23;
	private static final String URL = "manage2sail.com";

	@InjectMocks
	private RaceConverter converter;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void test_convert_fromEntity()
	{
		RaceInfoDto expectedMin = RaceInfoDto.builder().startDate(START_DATE).longDate(START_LONG).build();
		RaceEntity inputMin = RaceEntity.builder().startDate(START_LONG).build();
		RaceInfoDto expectedAll =
				RaceInfoDto.builder().name(NAME).startDate(START_DATE).longDate(START_LONG).raceGroup(RACE_GROUP)
						   .scoreModifier(SCORE_MOD).orcRace(ORC_RACE).bufferRace(BUFFER_RACE)
						   .participants(PARTICIPANTS).url(URL).build();
		RaceEntity inputAll =
				RaceEntity.builder().name(NAME).startDate(START_LONG).raceGroup(RACE_GROUP).scoreModifier(SCORE_MOD)
						  .orcRace(ORC_RACE).bufferRace(BUFFER_RACE).participants(PARTICIPANTS).url(URL).build();

		Assertions.assertThrows(NullPointerException.class, () -> converter.convert(new RaceEntity()));

		Assertions.assertEquals(expectedMin, converter.convert(inputMin));
		Assertions.assertEquals(expectedAll, converter.convert(inputAll));
	}

	@Test
	void test_convert_fromDto()
	{
		RaceEntity expectedAll =
				RaceEntity.builder().name(NAME).startDate(START_LONG).raceGroup(RACE_GROUP).scoreModifier(SCORE_MOD)
						  .orcRace(ORC_RACE).bufferRace(BUFFER_RACE).participants(PARTICIPANTS).url(URL).build();
		RaceInfoDto inputAll =
				RaceInfoDto.builder().name(NAME).startDate(START_DATE).longDate(START_LONG).raceGroup(RACE_GROUP)
						   .scoreModifier(SCORE_MOD).orcRace(ORC_RACE).bufferRace(BUFFER_RACE)
						   .participants(PARTICIPANTS).url(URL).build();

		Assertions.assertEquals(new RaceEntity(), converter.convert(new RaceInfoDto()));
		Assertions.assertEquals(expectedAll, converter.convert(inputAll));

	}
}
