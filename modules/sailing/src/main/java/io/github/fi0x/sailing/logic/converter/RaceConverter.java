package io.github.fi0x.sailing.logic.converter;

import io.github.fi0x.sailing.db.entities.RaceEntity;
import io.github.fi0x.sailing.logic.dto.RaceInfoDto;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RaceConverter
{
	public RaceInfoDto convert(RaceEntity entity)
	{
		return RaceInfoDto.builder().name(entity.getName()).startDate(new Date(entity.getStartDate()))
						  .longDate(entity.getStartDate()).raceGroup(entity.getRaceGroup())
						  .scoreModifier(entity.getScoreModifier()).orcRace(entity.getOrcRace())
						  .bufferRace(entity.getBufferRace()).participants(entity.getParticipants()).build();
	}
}
