package io.github.fi0x.sailing.logic.dto;

import io.github.fi0x.sailing.db.entities.RaceId;
import io.github.fi0x.sailing.logic.dto.m2s.RaceResultStatusCode;
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

	private Long endDate;
	private String url;

	private String shipName;
	private Integer position;
	private RaceResultStatusCode resultStatusCode;
	private Double score;
	private String shipClass;
	private Boolean crossed;

	public RaceId getRaceId()
	{
		return new RaceId(name, startDate, raceGroup);
	}
}
