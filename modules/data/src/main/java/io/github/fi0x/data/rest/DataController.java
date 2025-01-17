package io.github.fi0x.data.rest;

import io.github.fi0x.data.service.DataService;
import io.github.fi0x.data.service.SensorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.SortedMap;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DataController
{
	private final DataService dataService;
	private final SensorService sensorService;

	@GetMapping("/data/{address}/{name}")
	public SortedMap<Date, Double> getSensorData(@PathVariable String address, @PathVariable String name)
	{
		log.debug("getSensorData() called");

		Double adjustment = sensorService.getDetailedSensor(address, name).getValueAdjustment();
		return dataService.getAllData(address, name, adjustment);
	}
}
