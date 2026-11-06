package io.github.fi0x.data.service;

import io.github.fi0x.data.db.entities.DataEntity;
import io.github.fi0x.data.db.entities.SensorEntity;
import io.github.fi0x.data.logic.dto.SensorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculatedSensorService
{
	private final PersistenceSensorService persistenceSensorService;
	private final PersistenceDataService persistenceDataService;

	public void calculateAndSaveSensorDataFor(String address, String sensorName, SensorType calculatedSensorType)
	{
		if (SensorType.VPD.equals(calculatedSensorType))
			calculateAndSaveVDPSensorData(address, sensorName);
	}

	private void calculateAndSaveVDPSensorData(String address, String vpdSensorName)
	{
		log.trace("Calculating VPD value for address {} and sensor {}", address, vpdSensorName);

		// Check, if VPD sensor exists, otherwise ignore calculation and return
		SensorEntity vpdSensor = persistenceSensorService.findSensorEntity(address, vpdSensorName).orElse(null);
		if (vpdSensor == null)
		{
			log.warn("Could not calculate VPD value, because sensor does not exist {} {}", address, vpdSensorName);
			return;
		}

		// Check, if exactly 1 humidity and 1 temperature sensor are linked to this vpd sensor
		List<SensorEntity> vpdInputSensors = findAllVPDDataInputSensors(address, vpdSensorName);
		Optional<SensorEntity> optionalTemperatureSensor = vpdInputSensors.stream()
				.filter(e -> SensorType.TEMPERATURE.is(e.getType())).findFirst();
		Optional<SensorEntity> optionalHumiditySensor = vpdInputSensors.stream()
				.filter(e -> SensorType.HUMIDITY.is(e.getType())).findFirst();
		if (optionalTemperatureSensor.isEmpty() || optionalHumiditySensor.isEmpty())
		{
			log.warn(
					"Could not calculate VPD value, because not exactly 1 humidity and 1 temperature sensors were " +
							"found for address {} Found instead: {}",
					address, vpdInputSensors);
			return;
		}

		// Get the most recent data entries of the input sensors and verify them
		DataEntity temperature = persistenceDataService.getMostRecentEntry(optionalTemperatureSensor.get());
		DataEntity humidity = persistenceDataService.getMostRecentEntry(optionalHumiditySensor.get());
		if (!VPDService.areValidVPDInputs(temperature, humidity, vpdSensor.getDataDelay()))
		{
			log.debug(
					"Most recent data from the 2 sensors does not match or is invalid for VPD calculation in address " + "{}",
					address);
			return;
		}

		// Get values and adjust them according to their sensor value adjustments
		double t = getCorrectedValue(temperature, optionalTemperatureSensor.get());
		double h = getCorrectedValue(humidity, optionalHumiditySensor.get());

		// Calculate VPD with the 2 values and save data
		double vpd = VPDService.calculateVPD(h, t);
		persistenceDataService.saveData(address, vpdSensorName, vpd, getTime());

		log.trace("Saved VPD {} for address {} and sensor {}", vpd, address, vpdSensorName);
	}

	private List<SensorEntity> findAllVPDDataInputSensors(String address, String vpdSensorName)
	{
		// Fetch all sensors with this address
		// Filter for the ones, that have a matching vdpSensorName
		return persistenceSensorService.getAllSensors(address).stream()
				.filter(e -> vpdSensorName.equals(e.getVPDSensorName())).toList();
	}

	private double getCorrectedValue(DataEntity data, SensorEntity sensor)
	{
		double correction = sensor.getValueAdjustment() == null ? 0 : sensor.getValueAdjustment();
		return data.getValue() + correction;
	}

	private long getTime()
	{
		return System.currentTimeMillis();
	}
}
