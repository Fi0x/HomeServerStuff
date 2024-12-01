package io.github.fi0x.data.rest;

import io.github.fi0x.data.service.SensorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class InformationController
{
	private final SensorService sensorService;

	@GetMapping("/")
	public String showSensorList(ModelMap model)
	{
		log.info("showSensorList() called");

		model.put("sensorList", sensorService.getAllDetailedSensors());
		model.put("typeList", sensorService.getAllSensorTypes());

		return "sensor-list";
	}

	@GetMapping("/sensor/{address}/{name}")
	public String showSensor(@PathVariable String address, @PathVariable String name)
	{
		log.info("showSensor() called");

		//TODO: Show a page with the sensor data
		throw new ResponseStatusException(HttpStatusCode.valueOf(501), "This page is not yet finished");
	}
}
