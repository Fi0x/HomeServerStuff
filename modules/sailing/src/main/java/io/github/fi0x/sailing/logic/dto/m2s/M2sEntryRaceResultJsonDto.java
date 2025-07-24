package io.github.fi0x.sailing.logic.dto.m2s;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class M2sEntryRaceResultJsonDto
{
	@JsonProperty("RaceId")
	private String raceId;
	@JsonProperty("Points")
	private String points;
	@JsonProperty("RaceStatusCode")
	private RaceResultStatusCode raceStatusCode;
	@JsonProperty("Rank")
	private Integer position;
	@JsonProperty("OverallRaceIndex")
	private Integer raceIdx;
}
