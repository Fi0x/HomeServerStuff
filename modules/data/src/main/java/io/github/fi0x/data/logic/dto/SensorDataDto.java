package io.github.fi0x.data.logic.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorDataDto
{
	@NotBlank
	private String name;
	private String description;
	private String unit;
	private String type;
	private List<String> tags;
	private Long dataDelay;
	private Double valueAdjustment;
	private Double minValue;
	private Double maxValue;
	private Double value;
}
