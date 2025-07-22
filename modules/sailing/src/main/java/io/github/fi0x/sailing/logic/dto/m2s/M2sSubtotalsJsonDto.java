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
public class M2sSubtotalsJsonDto
{
	@JsonProperty("Total")
	private Double total;
	@JsonProperty("Net")
	private Double net;
	@JsonProperty("AfterRaceIndex")
	private Integer afterRaceIdx;
}
