package io.github.fi0x.data.logic.converter;

import io.github.fi0x.data.db.entities.DataEntity;
import io.github.fi0x.data.logic.dto.DataDto;

public class DataConverter
{
	public static DataEntity toEntity(DataDto source)
	{
		return DataEntity.builder().sender(source.getSender()).type(source.getType()).value(source.getValue())
						 .unit(source.getUnit()).build();
	}
}
