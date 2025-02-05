package io.github.fi0x.data.logic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpandedDataDto
{
	private String address;
	private String sensorName;
	private Long timestamp;
	private Double value;
}
