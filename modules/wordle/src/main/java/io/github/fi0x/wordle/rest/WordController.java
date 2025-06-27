package io.github.fi0x.wordle.rest;

import io.github.fi0x.wordle.logic.dto.GameResultDto;
import io.github.fi0x.wordle.logic.dto.GameSettings;
import io.github.fi0x.wordle.logic.dto.ValidationResultDto;
import io.github.fi0x.wordle.logic.dto.WordValidationDto;
import io.github.fi0x.wordle.service.DataService;
import io.github.fi0x.wordle.service.WordService;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WordController
{
	private final DataService dataService;
	private final WordService wordService;

	@PostMapping("/words/verify")
	public WordValidationDto getSensorData(@RequestParam String word, @RequestBody GameSettings settings)
	{
		log.debug("verify() called with word '{}'", word);

		if (!wordService.validate(word))
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Word is not in word-list");
		return wordService.match(settings, word);
	}

	@PostMapping("/results/save")
	public GameResultDto saveResult(@RequestBody GameResultDto resultEntity)
	{
		log.debug("saveResult() called");

		return dataService.saveGame(resultEntity);
	}

	@GetMapping("/words/validate/{word}")
	public ValidationResultDto validateWord(@Size(min = 5, max = 5) @PathVariable String word,
											@RequestParam(required = false, defaultValue = "false") Boolean newCreation)
	{
		log.debug("validateWord() called");

		return wordService.validateWordForDb(word, newCreation);
	}

	@GetMapping("/words/invalidate/{word}")
	public ValidationResultDto invalidateWord(@Size(min = 5, max = 5) @PathVariable String word)
	{
		log.debug("invalidateWord() called");

		return wordService.invalidateWordForDb(word);
	}
}
