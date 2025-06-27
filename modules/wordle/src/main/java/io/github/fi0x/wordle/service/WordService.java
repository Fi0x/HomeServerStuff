package io.github.fi0x.wordle.service;

import io.github.fi0x.wordle.db.GameRepo;
import io.github.fi0x.wordle.db.WordRepo;
import io.github.fi0x.wordle.db.entities.WordEntity;
import io.github.fi0x.wordle.logic.dto.GameSettings;
import io.github.fi0x.wordle.logic.dto.WordValidationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WordService
{
	@Value("${homeserver.wordle.verified-amount.exists}")
	private Integer minVerificationsForExistence;
	@Value("${homeserver.wordle.verification.increase}")
	private Integer verificationIncreaseAmount;

	private final GameRepo gameRepo;
	private final WordRepo wordRepo;

	public boolean validate(String word)
	{
		boolean valid = false;
		WordEntity wordEntity = wordRepo.findById(word).orElse(WordEntity.builder().word(word).verified(0).build());
		if (wordEntity.getVerified() >= minVerificationsForExistence || wordEntity.getVerified() < 0)
			valid = true;

		if (wordEntity.getVerified() >= 0)
		{
			wordEntity.setVerified(wordEntity.getVerified() + 1);
			wordRepo.save(wordEntity);
		}

		return valid;
	}

	public WordValidationDto match(GameSettings settings, String word)
	{
		String currentWord = gameRepo.findById(settings.getTimestamp()).orElseThrow(
											 () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
																			   "No game with the " + "requested " +
																					   "timestamp " + "was " +
																					   "started"))
									 .getWord();

		WordValidationDto validationDto = WordValidationDto.builder().word(word).build();
		for (int i = 0; i < currentWord.length(); i++)
		{
			if (word.charAt(i) == currentWord.charAt(i))
				validationDto.getCharacterValidation().add(1);
			else if (currentWord.contains(String.valueOf(word.charAt(i))))
				validationDto.getCharacterValidation().add(0);
			else
				validationDto.getCharacterValidation().add(-1);
		}

		return validationDto;
	}
}
