package io.github.fi0x.data.logic.converter;

import io.github.fi0x.data.db.entities.SensorEntity;
import io.github.fi0x.data.logic.dto.ExpandedSensorDto;

public class SensorConverter
{
	public static ExpandedSensorDto toExpandedDto(SensorEntity source)
	{
		boolean isOffline = System.currentTimeMillis() - source.getLastUpdate() > source.getDataDelay() * 2;

		return ExpandedSensorDto.builder().address(source.getAddress()).name(source.getName())
								.description(source.getDescription()).unit(source.getUnit()).type(source.getType())
								.dataDelay(source.getDataDelay()).offline(isOffline).build();
	}
}
