package io.github.fi0x.sailing.db.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
@Table(name = "SAILRESULTS")
@IdClass(RaceResultId.class)
public class RaceResultEntity
{
	@Id
	private String name;
	@Id
	private Long startDate;
	@Id
	private String raceGroup;
	@Id
	private String skipper;

	private String shipName;
	private Integer position;
	private String shipClass;
}
