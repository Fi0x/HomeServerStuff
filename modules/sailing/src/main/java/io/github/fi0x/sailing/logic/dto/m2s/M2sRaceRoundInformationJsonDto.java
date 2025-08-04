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
public class M2sRaceRoundInformationJsonDto
{
	@JsonProperty("Id")
	private String id;
	@JsonProperty("RaceName")
	private String roundName;
}
