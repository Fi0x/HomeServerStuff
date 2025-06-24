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
@Table(name = "SAILRACE")
@IdClass(RaceId.class)
public class RaceEntity
{
	@Id
	private String name;
	@Id
	private Long startDate;
	@Id
	private String raceGroup;

	private Long endDate;
	private Double scoreModifier;
	private String url;
	private Boolean orcRace;
	private Boolean bufferRace;
	private Integer participants;

	public RaceId getId()
	{
		return new RaceId(name, startDate, raceGroup);
	}
}
