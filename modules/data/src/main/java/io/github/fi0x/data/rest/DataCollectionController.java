package io.github.fi0x.data.rest;

import io.github.fi0x.data.logic.dto.DataDto;
import io.github.fi0x.data.service.CollectionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class DataCollectionController
{
	private final CollectionService collectionService;

	@PostMapping("/data")
	public void getData(HttpServletRequest request, @RequestBody DataDto requestDto)
	{
		log.debug("getData() called");

		collectionService.addData(request.getRemoteAddr(), requestDto);
	}
}
