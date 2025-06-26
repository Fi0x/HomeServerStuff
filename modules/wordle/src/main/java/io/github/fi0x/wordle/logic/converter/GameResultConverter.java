package io.github.fi0x.wordle.logic.converter;

import io.github.fi0x.wordle.db.entities.GameResultEntity;
import io.github.fi0x.wordle.logic.dto.GameResultDto;
import org.springframework.stereotype.Component;

@Component
public class GameResultConverter
{
	public GameResultDto convert(GameResultEntity entity)
	{
		return GameResultDto.builder().timestamp(entity.getTimestamp()).playerName(entity.getPlayerName())
							.tries(entity.getTries()).requiredTime(entity.getRequiredTime()).build();
	}

	public GameResultEntity convert(GameResultDto dto)
	{
		return GameResultEntity.builder().timestamp(dto.getTimestamp()).playerName(dto.getPlayerName())
							   .tries(dto.getTries()).requiredTime(dto.getRequiredTime()).build();
	}
}
