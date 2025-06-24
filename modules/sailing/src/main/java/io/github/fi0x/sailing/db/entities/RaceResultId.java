package io.github.fi0x.sailing.db.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RaceResultId implements Serializable
{
	private String name;
	private Long startDate;
	private String raceGroup;
	private String skipper;
}
