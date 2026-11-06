package io.github.fi0x.data.logic.dto;

public enum SensorType
{
	HUMIDITY, TEMPERATURE, VPD;

	public boolean is(String comparison)
	{
		return this.name().equalsIgnoreCase(comparison);
	}
}
