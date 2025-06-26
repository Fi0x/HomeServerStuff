package io.github.fi0x.wordle.rest;

import io.github.fi0x.wordle.logic.dto.GameSettings;
import io.github.fi0x.wordle.logic.dto.WordValidationDto;
import io.github.fi0x.wordle.service.WordService;
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
	private final WordService wordService;

	@PostMapping("/words/verify")
	public WordValidationDto getSensorData(@RequestParam String word, @RequestBody GameSettings settings)
	{
		log.debug("verify() called with word '{}'", word);

		if (!wordService.validate(word))
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Word is not in word-list");
		return wordService.match(settings, word);
	}
}
