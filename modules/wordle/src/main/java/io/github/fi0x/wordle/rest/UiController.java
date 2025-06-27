package io.github.fi0x.wordle.rest;

import io.github.fi0x.wordle.service.DataService;
import io.github.fi0x.wordle.service.WordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UiController
{
	private final DataService dataService;
	private final WordService wordService;

	@GetMapping("/")
	public String showMainPage(ModelMap model)
	{
		log.info("showMainPage() called");

		model.put("gameModes", dataService.getGameModes());

		return "main";
	}

	@GetMapping("/play")
	public String showPlayPage(ModelMap model, @RequestParam String gameMode,
							   @RequestParam(defaultValue = "de") String language)
	{
		log.info("showPlayPage() called");

		model.put("keyboard", dataService.getKeyboardData(language));
		model.put("gameMode", dataService.getNewSettings(gameMode));

		return "play";
	}

	@GetMapping("/validation")
	public String showValidationPage(ModelMap model)
	{
		log.info("showValidationPage() called");

		model.put("wordToVerify", wordService.getWordToValidate());

		return "validate";
	}
}
