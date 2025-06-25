package io.github.fi0x.sailing.logic.dto;

import io.github.fi0x.sailing.db.entities.RaceId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaceResultDto
{
	private String name;
	private Long startDate;
	private String raceGroup;
	private String skipper;

	private String shipName;
	private Integer position;
	private Double score;
	private String shipClass;
	private Boolean crossed;

	public RaceId getRaceId()
	{
		return new RaceId(name, startDate, raceGroup);
	}
}
