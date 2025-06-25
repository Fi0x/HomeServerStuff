package io.github.fi0x.sailing.logic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipRaceResults
{
	private String shipName;
	private String skipper;
	private String shipClass;
	private List<RaceResultDto> raceResults;
}
