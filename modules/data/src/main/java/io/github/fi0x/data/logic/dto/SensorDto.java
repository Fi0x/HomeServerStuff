package io.github.fi0x.data.logic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorDto
{
	private String name;
	private String description;
	private String unit;
	private String type;
	private List<String> tags;
}
