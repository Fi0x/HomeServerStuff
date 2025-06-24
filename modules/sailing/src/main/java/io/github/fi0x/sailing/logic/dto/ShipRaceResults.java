package io.github.fi0x.sailing.logic.dto;

import io.github.fi0x.sailing.db.entities.RaceResultEntity;
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
	private List<RaceResultEntity> raceResults;
}
