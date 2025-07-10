package io.github.fi0x.sailing.logic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"ERRORS"})
public class OrcOverviewXmlDto
{
	@JsonProperty("DATA")
	private OrcOverviewXmlDataDto DATA;
}
