package io.github.fi0x.data.logic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpandedSensorDto
{
	private String address;
	private String name;
	private String description;
	private String unit;
	private String type;
	private List<String> tags;
	private Double value;
	private Long dataDelay;
	private Boolean offline;
	private Date lastUpdate;
	private Double valueAdjustment;
	private Double minValue;
	private Double maxValue;
}
