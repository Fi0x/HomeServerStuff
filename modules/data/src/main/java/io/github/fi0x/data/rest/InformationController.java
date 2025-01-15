package io.github.fi0x.data.rest;

import io.github.fi0x.data.service.DataService;
import io.github.fi0x.data.service.SensorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
@RequiredArgsConstructor
public class InformationController
{
	private final SensorService sensorService;
	private final DataService dataService;

	@GetMapping("/")
	public String showSensorList(ModelMap model)
	{
		log.info("showSensorList() called");

		model.put("sensorList", sensorService.getAllDetailedSensors());
		model.put("tagList", sensorService.getAllSensorTags());

		return "sensor-list";
	}

	@GetMapping("/sensor/{address}/{name}")
	public String showSensor(ModelMap model, @PathVariable String address, @PathVariable String name)
	{
		log.info("showSensor() called");

		model.put("sensor", sensorService.getDetailedSensor(address, name));
		model.put("data", dataService.getAllData(address, name));

		return "show-sensor";
	}

	@GetMapping("/sensor/{address}/{name}/edit")
	public String editSensor(ModelMap model, @PathVariable String address, @PathVariable String name)
	{
		log.info("editSensor() called");

		model.put("sensor", sensorService.getDetailedSensor(address, name));

		return "edit-sensor";
	}
}
