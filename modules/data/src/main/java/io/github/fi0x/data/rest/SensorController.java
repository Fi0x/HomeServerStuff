package io.github.fi0x.data.rest;

import io.github.fi0x.data.logic.dto.DataDto;
import io.github.fi0x.data.logic.dto.SensorDto;
import io.github.fi0x.data.service.DataService;
import io.github.fi0x.data.service.SensorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SensorController
{
	private final DataService dataService;
	private final SensorService sensorService;

	@PostMapping("/upload")
	public void getData(HttpServletRequest request, @RequestBody DataDto requestDto)
	{
		log.debug("getData() called");

		dataService.addData(request.getRemoteAddr(), requestDto);
	}

	@PostMapping("/register")
	public void registerSensor(HttpServletRequest request, @Valid @RequestBody SensorDto requestDto)
	{
		log.debug("registerSensor() called");

		sensorService.saveSensor(request.getRemoteAddr(), requestDto);
	}
}
