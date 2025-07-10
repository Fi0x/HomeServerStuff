package io.github.fi0x.sailing.db.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RaceId implements Serializable
{
	private String name;
	private Long startDate;
	private String raceGroup;
}
