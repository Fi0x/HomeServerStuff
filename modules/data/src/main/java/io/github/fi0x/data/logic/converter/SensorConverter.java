package io.github.fi0x.data.logic.converter;

import io.github.fi0x.data.db.entities.SensorEntity;
import io.github.fi0x.data.logic.dto.ExpandedSensorDto;
import io.github.fi0x.data.logic.dto.SensorDto;

import java.util.Date;

public class SensorConverter
{
	public static ExpandedSensorDto toExpandedDto(SensorEntity source)
	{
		boolean isOffline = source.getDataDelay() == null || source.getLastUpdate() == null || System.currentTimeMillis() - source.getLastUpdate() > source.getDataDelay() * 2;
		Date lastUpdated = source.getLastUpdate() == null ? new Date(0) : new Date(source.getLastUpdate());

		return ExpandedSensorDto.builder().address(source.getAddress()).name(source.getName())
								.description(source.getDescription()).unit(source.getUnit()).type(source.getType())
								.dataDelay(source.getDataDelay()).offline(isOffline).lastUpdate(lastUpdated)
								.valueAdjustment(source.getValueAdjustment()).minValue(source.getMinValue())
								.maxValue(source.getMaxValue()).build();
	}

	public static SensorDto toDto(SensorEntity source)
	{
		return SensorDto.builder().name(source.getName()).description(source.getDescription()).unit(source.getUnit())
						.type(source.getType()).dataDelay(source.getDataDelay()).build();
	}
}
