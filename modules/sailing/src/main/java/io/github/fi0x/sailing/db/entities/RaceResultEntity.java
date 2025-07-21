package io.github.fi0x.sailing.db.entities;

import io.github.fi0x.sailing.logic.dto.RaceInformation;
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
public class RaceResultEntity implements RaceInformation
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
	private Double score;
	private String shipClass;

	public RaceResultEntity clone()
	{
		RaceResultEntity newEntity;
		try
		{
			newEntity = (RaceResultEntity) super.clone();
		} catch (CloneNotSupportedException e)
		{
			newEntity = new RaceResultEntity();
		}

		newEntity.name = name;
		newEntity.startDate = startDate;
		newEntity.raceGroup = raceGroup;
		newEntity.skipper = skipper;
		newEntity.shipName = shipName;
		newEntity.position = position;
		newEntity.score = score;
		newEntity.shipClass = shipClass;

		return newEntity;
	}
}
