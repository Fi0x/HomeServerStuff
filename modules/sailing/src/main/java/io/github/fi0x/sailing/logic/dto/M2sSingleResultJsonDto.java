package io.github.fi0x.sailing.logic.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class M2sSingleResultJsonDto
{
	@JsonProperty("Rank")
	private Integer position;
	@JsonProperty("Name")
	private String skipper;
	@JsonProperty("BoatName")
	private String shipName;
	@JsonProperty("BoatType")
	private String shipClass;
	@JsonProperty("EntryRaceResults")
	private List<M2sSingleResultDetailsJsonDto> raceEntries;
}
