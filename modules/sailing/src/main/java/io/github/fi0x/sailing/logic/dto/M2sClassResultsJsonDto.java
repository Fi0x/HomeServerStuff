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
public class M2sClassResultsJsonDto
{
	@JsonProperty("RegattaName")
	private String className;
	@JsonProperty("Id")
	private String m2sId;
	@JsonProperty("IsLowPointSystem")
	private Boolean isLowPoint;
	@JsonProperty("ResultReports")
	private List<Object> raceRoundInformation;
	@JsonProperty("LastRaceIndex")
	private Integer lastRaceIdx;
	@JsonProperty("EntryResults")
	private List<Object> entries;
	@JsonProperty("ScoringResults")
	private List<Object> results;
}
