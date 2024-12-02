package io.github.fi0x.data.logic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataDto
{
	private String sensorName;//TODO: Use this to distinguish between sensors with the same address
	private Double value;
}
