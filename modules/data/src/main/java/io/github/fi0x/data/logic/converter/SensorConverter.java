package io.github.fi0x.data.logic.converter;

import io.github.fi0x.data.db.entities.SensorEntity;
import io.github.fi0x.data.logic.dto.SensorDto;

public class SensorConverter
{
	public static SensorDto toDto(SensorEntity source)
	{
		return SensorDto.builder().name(source.getName()).description(source.getDescription()).unit(source.getUnit())
						.type(source.getType()).build();
	}
}
