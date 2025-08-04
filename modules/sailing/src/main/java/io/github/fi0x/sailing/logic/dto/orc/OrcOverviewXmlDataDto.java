package io.github.fi0x.sailing.logic.dto.orc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrcOverviewXmlDataDto
{
	@JsonProperty("ROW")
	private OrcOverviewXmlRowDto ROW;
}
