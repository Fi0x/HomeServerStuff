package io.github.fi0x.data.rest;

import io.github.fi0x.data.logic.dto.DataDto;
import io.github.fi0x.data.logic.dto.ExpandedDataDto;
import io.github.fi0x.data.logic.dto.SensorDataDto;
import io.github.fi0x.data.logic.dto.SensorDto;
import io.github.fi0x.data.service.DataService;
import io.github.fi0x.data.service.NotificationService;
import io.github.fi0x.data.service.SensorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SensorController
{
	private final DataService dataService;
	private final SensorService sensorService;
	private final NotificationService notificationService;

	@PostMapping("/upload")
	public void uploadData(HttpServletRequest request, @RequestBody DataDto requestDto)
	{
		log.debug("uploadData() called from {} with dto: {}", request.getRemoteAddr(), requestDto);

		long timestamp = dataService.addData(request.getRemoteAddr(), requestDto);
		notificationService.notifyDataUpdate(
				ExpandedDataDto.builder().address(request.getRemoteAddr()).sensorName(requestDto.getSensorName())
							   .timestamp(timestamp).value(requestDto.getValue()).build());
	}

	@PostMapping("/new-data")
	public void uploadDataForNewSensor(HttpServletRequest request, @Valid @RequestBody SensorDataDto requestDto)
	{
		log.debug("uploadDataForNewSensor() called from {} with dto: {}", request.getRemoteAddr(), requestDto);

		long timestamp = sensorService.saveSensorAndData(request.getRemoteAddr(), requestDto);
		notificationService.notifyDataUpdate(
				ExpandedDataDto.builder().address(request.getRemoteAddr()).sensorName(requestDto.getName())
							   .timestamp(timestamp).value(requestDto.getValue()).min(requestDto.getMinValue())
							   .max(requestDto.getMaxValue()).delay(requestDto.getDataDelay())
							   .unit(requestDto.getUnit()).build());
	}

	@GetMapping("/sensors")
	public List<SensorDto> listSensors(HttpServletRequest request)
	{
		log.debug("listSensors() called from {}", request.getRemoteAddr());

		return sensorService.getAllSensors();
	}
}
