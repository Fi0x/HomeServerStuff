package io.github.fi0x.sailing.logic.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@Deprecated
@NoArgsConstructor
@AllArgsConstructor
public class M2sRaceResultJsonDto
{
	@JsonProperty("RegattaName")
	private String raceGroup;

	@JsonProperty("EntryResults")
	List<M2sSingleResultJsonDto> shipResults;
}
