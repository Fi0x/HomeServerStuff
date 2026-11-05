package io.github.fi0x.data.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VPDService
{

	/**
	 * Calculates Vapor Pressure Deficit according to the following formula
	 * eBlatt = 0,6112 kPa × Math.pow(e,(17,62 × TBlatt ÷ (243,12 + TBlatt)))
	 * eTemp = 0,6112 kPa × Math.pow(e, (17,62 × TLuft ÷ (243,12 + TLuft)))
	 * VPD = eBlatt − eTemp * (rF ÷ 100)
	 *
	 * @param humidity    The relative humidity percentage value
	 * @param temperature The air-temperature
	 * @return The vapor-pressure-deficit
	 */
	public static double calculateVPD(double humidity, double temperature)
	{
		double leafTemp = temperature - 2;
		double eLeaf = 0.6112 * Math.pow(Math.E, (17.62 * leafTemp / (leafTemp + 243.12)));
		double svp = 0.6112 * Math.pow(Math.E, (17.62 * temperature / (temperature + 243.12)));
		double avp = svp * (humidity / 100);
		double vpd = eLeaf - avp;

		return (double) Math.round(vpd * 100) / 100;
	}
}
