package io.github.fi0x.sailing.logic.dto.orc;

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
@JsonIgnoreProperties({"RowNum", "Family", "dxtDate", "dxtName", "VPPYear", "CertType", "Expiry", "IsOd", "FamilyName"})
public class OrcOverviewXmlRowDto
{
	@JsonProperty("CountryId")
	private String CountryId;
	@JsonProperty("dxtID")
	private String dxtID;
	@JsonProperty("RefNo")
	private String refNo;
	@JsonProperty("YachtName")
	private String YachtName;
	@JsonProperty("SailNo")
	private String SailNo;
	@JsonProperty("Class")
	private String shipClass;
	@JsonProperty("CertName")
	private String CertName;
}
