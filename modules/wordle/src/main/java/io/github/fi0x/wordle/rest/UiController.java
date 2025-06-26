package io.github.fi0x.wordle.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UiController
{
	@GetMapping("/")
	public String showMainPage()
	{
		return "main";
	}
}
