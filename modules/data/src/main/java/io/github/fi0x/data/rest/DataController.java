package io.github.fi0x.data.rest;

import io.github.fi0x.data.service.DataService;
import io.github.fi0x.data.service.SensorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.SortedMap;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DataController
{
	@Value("${homeserver.data.overview.data-age}")
	private Integer dataAge;

	private final DataService dataService;
	private final SensorService sensorService;

	@GetMapping("/data/{address}/{name}")
	public SortedMap<Date, Double> getSensorData(@PathVariable String address, @PathVariable String name,
												 @RequestParam(name = "amount", required = false) Integer amount,
												 @RequestParam(name = "oldest", required = false) Long oldest)
	{
		log.debug("getSensorData() called");

		if(amount != null && oldest != null)
			throw new ResponseStatusException(HttpStatusCode.valueOf(400),
											  "Either 'amount' OR 'oldest' must be set, not both");

		Double adjustment = sensorService.getDetailedSensor(address, name).getValueAdjustment();

		if(amount != null)
			return dataService.getData(address, name, adjustment, amount);
		else if(oldest != null)
			return dataService.getData(address, name, adjustment, oldest);
		else
			return dataService.getAllData(address, name, adjustment);
	}

	@GetMapping("/data/{address}/{name}/last-timeframe")
	public SortedMap<Date, Double> getLimitedSensorData(@PathVariable String address, @PathVariable String name)
	{
		log.debug("getLimitedSensorData() called");

		Double adjustment = sensorService.getDetailedSensor(address, name).getValueAdjustment();
		Long timestamp = System.currentTimeMillis() - dataAge;

		return dataService.getData(address, name, adjustment, timestamp);
	}
}
