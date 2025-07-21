package io.github.fi0x.sailing.logic.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Deprecated
@NoArgsConstructor
@AllArgsConstructor
public class M2sSingleResultDetailsJsonDto
{
	@JsonProperty("RaceStatusCode")
	private String raceStatus;
}
