package io.github.fi0x.data.rest;

import io.github.fi0x.data.logic.dto.ExpandedSensorDto;
import io.github.fi0x.data.service.DataService;
import io.github.fi0x.data.service.SensorService;
import io.github.fi0x.util.components.Authenticator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class InformationController
{
	private final SensorService sensorService;
	private final DataService dataService;
	private final Authenticator authenticator;

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

		ExpandedSensorDto sensorDto = sensorService.getDetailedSensor(address, name);
		model.put("sensor", sensorDto);
		model.put("data", dataService.getAllData(address, name, sensorDto.getValueAdjustment()));
		model.put("username", authenticator.getAuthenticatedUsername());

		return "show-sensor";
	}

	//	TODO: Only show this page to logged in users
	@GetMapping("/sensor/{address}/{name}/edit")
	public String editSensor(ModelMap model, @PathVariable String address, @PathVariable String name)
	{
		log.info("editSensor() called");

		model.put("sensor", sensorService.getDetailedSensor(address, name));

		return "edit-sensor";
	}

	//	TODO: Verify user is logged in before updating any data
	@GetMapping("/sensor/{address}/{name}/update")
	public String updateSensor(ModelMap model, @PathVariable String address, @PathVariable String name,
							   @RequestParam(value = "valueAdjustment", required = false) Double valueAdjustment,
							   @RequestParam(value = "deleteValues", required = false) String valueDeletion)
	{
		log.info("updateSensor() called");

		if (valueAdjustment != null)
			sensorService.saveSensorValueAdjustment(address, name, valueAdjustment);

		if (valueDeletion != null)
		{
			if (valueDeletion.equals("ALL"))
				dataService.deleteForSensor(address, name, null);
			else if (NumberUtils.isCreatable(valueDeletion))
				dataService.deleteForSensor(address, name, Double.parseDouble(valueDeletion));
		}

		model.put("sensor", sensorService.getDetailedSensor(address, name));

		return showSensor(model, address, name);
	}
}
