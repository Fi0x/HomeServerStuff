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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SAILRACE")
@IdClass(RaceId.class)
public class RaceEntity implements RaceInformation
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

	public String getGroupAndYear()
	{
		Calendar c = new GregorianCalendar();
		c.setTime(new Date(startDate));
		return raceGroup + " - " + c.get(Calendar.YEAR);
	}

	public Integer getBooleans()
	{
		int result = 0;
		if (orcRace)
			result |= 1;
		if (bufferRace)
			result |= 2;
		return result;
	}
}
