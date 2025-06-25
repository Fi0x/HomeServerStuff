package io.github.fi0x.sailing.logic.converter;

import io.github.fi0x.sailing.db.entities.RaceResultEntity;
import io.github.fi0x.sailing.logic.dto.RaceResultDto;
import org.springframework.stereotype.Component;

@Component
public class RaceResultToDtoConverter
{
	public RaceResultDto convert(RaceResultEntity entity)
	{
		return RaceResultDto.builder().name(entity.getName()).startDate(entity.getStartDate())
							.raceGroup(entity.getRaceGroup()).skipper(entity.getSkipper())
							.shipName(entity.getShipName()).position(entity.getPosition()).score(entity.getScore())
							.shipClass(entity.getShipClass()).crossed(false).build();
	}
}
