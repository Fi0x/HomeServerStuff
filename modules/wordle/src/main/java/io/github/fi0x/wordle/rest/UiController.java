package io.github.fi0x.wordle.rest;

import io.github.fi0x.wordle.service.DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UiController
{
	private final DataService dataService;

	@GetMapping("/")
	public String showMainPage()
	{
		log.info("showMainPage() called");

		return "main";
	}

	@GetMapping("/play")
	public String showPlayPage(ModelMap model)
	{
		log.info("showPlayPage() called");

		model.put("keyboard", dataService.getKeyboardData());

		return "play";
	}
}
