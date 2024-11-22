package io.github.fi0x.data.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController
{

	@GetMapping("/")
	public String showHomePage()
	{
		throw new ResponseStatusException(HttpStatusCode.valueOf(501), "Sorry, but this page is not implemented yet");
	}
}
