package io.github.fi0x.sailing.logic.dto.m2s;

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
public class M2sSingleScoreResultJsonDto
{
	@JsonProperty("RegattaName")
	private String className;
	@JsonProperty("RaceCount")
	private Integer races;
	@JsonProperty("ScoringName")
	private String subClassName;
	@JsonProperty("Id")
	private String id;
	@JsonProperty("IsLowPointSystem")
	private Boolean lowPointSystem;
	@JsonProperty("EntryResults")
	private List<M2sEntryResultJsonDto> entries;
}
