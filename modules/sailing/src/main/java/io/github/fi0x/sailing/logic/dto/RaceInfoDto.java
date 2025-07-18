package io.github.fi0x.sailing.logic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaceInfoDto
{
	private String name;
	private Date startDate;
	private String raceGroup;
	private Double scoreModifier;
	private Boolean orcRace;
	private Boolean bufferRace;
	private Integer participants;
}
