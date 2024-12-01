package io.github.fi0x.data.db.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TagId implements Serializable
{
	private String sensorName;
	private String tag;
}
