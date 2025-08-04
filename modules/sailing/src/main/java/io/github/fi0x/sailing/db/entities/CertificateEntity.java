package io.github.fi0x.sailing.db.entities;

import io.github.fi0x.sailing.logic.dto.orc.CertificateType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SAILORC")
public class CertificateEntity
{
	@Id
	private String id;

	private String shipName;
	private CertificateType certificateType;
	private String country;
	private String shipClass;
	private String url;

	private Double singleNumber;
	private Double tripleLongLow;
	private Double tripleLongMid;
	private Double tripleLongHigh;
	private Double tripleUpDownLow;
	private Double tripleUpDownMid;
	private Double tripleUpDownHigh;
}
