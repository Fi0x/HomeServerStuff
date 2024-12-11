package io.github.fi0x.data.rest;

import io.github.fi0x.data.logic.dto.DataDto;
import io.github.fi0x.data.logic.dto.SensorDataDto;
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
	public void uploadData(HttpServletRequest request, @RequestBody DataDto requestDto)
	{
		log.debug("uploadData() called");

		dataService.addData(request.getRemoteAddr(), requestDto);
	}

	@Deprecated
	@PostMapping("/register")
	public void registerSensor(HttpServletRequest request, @Valid @RequestBody SensorDto requestDto)
	{
		log.debug("registerSensor() called");

		requestDto.setDataDelay(requestDto.getDataDelay() == null ? null : requestDto.getDataDelay() / 1000);
		sensorService.saveSensor(request.getRemoteAddr(), requestDto);
	}

	@PostMapping("/new-data")
	public void uploadDataForNewSensor(HttpServletRequest request, @Valid @RequestBody SensorDataDto requestDto)
	{
		log.debug("uploadDataForNewSensor() called from {} with dto: {}", request.getRemoteAddr(), requestDto);

		sensorService.saveSensorAndData(request.getRemoteAddr(), requestDto);
	}
}
