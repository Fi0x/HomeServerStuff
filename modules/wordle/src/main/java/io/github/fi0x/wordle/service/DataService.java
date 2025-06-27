package io.github.fi0x.wordle.service;

import io.github.fi0x.util.components.Authenticator;
import io.github.fi0x.wordle.db.GameRepo;
import io.github.fi0x.wordle.db.GameResultRepo;
import io.github.fi0x.wordle.db.WordRepo;
import io.github.fi0x.wordle.db.entities.GameEntity;
import io.github.fi0x.wordle.db.entities.GameResultEntity;
import io.github.fi0x.wordle.db.entities.GameResultId;
import io.github.fi0x.wordle.db.entities.WordEntity;
import io.github.fi0x.wordle.logic.converter.GameResultConverter;
import io.github.fi0x.wordle.logic.dto.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DataService
{
	@Value("${homeserver.wordle.verified-amount.riddle}")
	private Integer minVerificationsForRiddle;
	private static final Integer MAX_SEARCH_TRIES = 100;
	private static final String DAILY = "daily";
	private static final String TEN_MINUTES = "ten-minutes";
	private static final String FIVE_MINUTES = "five-minutes";
	private static final String ENDLESS = "endless";
	private static final List<GameModeDto> GAME_MODES = List.of(new GameModeDto(DAILY, "Daily"),
																new GameModeDto(TEN_MINUTES, "Ten Minutes"),
																new GameModeDto(FIVE_MINUTES, "Five Minutes"),
																new GameModeDto(ENDLESS, "Endless"));
	private static final KeyboardDto KEYBOARD_LAYOUT_DE = KeyboardDto.builder().firstRow(
			List.of(new KeyDto("Q", 1, 81), new KeyDto("W", 1, 87), new KeyDto("E", 1, 69), new KeyDto("R", 1, 82),
					new KeyDto("T", 1, 84), new KeyDto("Z", 1, 90), new KeyDto("U", 1, 85), new KeyDto("I", 1, 73),
					new KeyDto("O", 1, 79), new KeyDto("P", 1, 80), new KeyDto("Ü", 1, 219))).secondRow(
			List.of(new KeyDto("A", 1, 65), new KeyDto("S", 1, 83), new KeyDto("D", 1, 68), new KeyDto("F", 1, 70),
					new KeyDto("G", 1, 71), new KeyDto("H", 1, 72), new KeyDto("J", 1, 74), new KeyDto("K", 1, 75),
					new KeyDto("L", 1, 76), new KeyDto("Ö", 1, 186), new KeyDto("Ä", 1, 222))).thirdRow(
			List.of(new KeyDto("<-", 2, 8), new KeyDto("Y", 1, 89), new KeyDto("X", 1, 88), new KeyDto("C", 1, 67),
					new KeyDto("V", 1, 86), new KeyDto("B", 1, 66), new KeyDto("N", 1, 78), new KeyDto("M", 1, 77),
					new KeyDto("Enter", 2, 13))).build();

	private final Authenticator authenticator;
	private final GameRepo gameRepo;
	private final WordRepo wordRepo;
	private final GameResultRepo resultRepo;
	private final GameResultConverter gameResultConverter;

	public List<GameModeDto> getGameModes()
	{
		String username = authenticator.getAuthenticatedUsername();
		return GAME_MODES.stream().filter(gameMode -> resultRepo.findById(
				new GameResultId(getTimestampForLastGame(gameMode.getId()), username)).isEmpty()).toList();
	}

	public KeyboardDto getKeyboardData(@NotNull String language)
	{
		if ("DE".equalsIgnoreCase(language))
			return KEYBOARD_LAYOUT_DE;
		throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
										  "The requested language does not exist in the database");
	}

	public GameSettings getNewSettings(String gameMode)
	{
		long timestamp = getTimestampForLastGame(gameMode);

		String username = authenticator.getAuthenticatedUsername();
		if (resultRepo.existsById(new GameResultId(timestamp, username)))
			throw new ResponseStatusException(HttpStatus.GONE, "You already played this level");

		if (gameRepo.findById(timestamp).isEmpty())
			createNewGame(timestamp);

		return GameSettings.builder().gameModeName(gameMode).timestamp(timestamp).playerName(username)
						   .started(System.currentTimeMillis()).build();
	}

	public GameResultDto saveGame(GameResultDto resultDto)
	{
		resultDto.setPlayerName(authenticator.getAuthenticatedUsername());
		Optional<GameResultEntity> resultEntity = resultRepo.findById(
				new GameResultId(resultDto.getTimestamp(), resultDto.getPlayerName()));
		if (resultEntity.isPresent())
		{
			log.warn("Result already saved for that user");
			return gameResultConverter.convert(resultEntity.get());
		}

		resultDto.setRequiredTime(System.currentTimeMillis() - resultDto.getRequiredTime());

		resultRepo.save(gameResultConverter.convert(resultDto));
		return resultDto;
	}

	private void createNewGame(Long timestamp)
	{
		String word = getRandomWord().getWord();
		GameEntity newGame = GameEntity.builder().timestamp(timestamp).word(word).build();
		gameRepo.save(newGame);
	}

	private long getTimestampForLastGame(String gameMode)
	{
		long timestamp = System.currentTimeMillis();
		switch (gameMode)
		{
			case DAILY -> timestamp = timestamp / 1000 / 60 / 60 / 24 * 24 * 60 * 60 * 1000;
			case TEN_MINUTES -> timestamp = timestamp / 1000 / 60 / 10 * 10 * 60 * 1000;
			case FIVE_MINUTES -> timestamp = timestamp / 1000 / 60 / 5 * 5 * 60 * 1000;
			case ENDLESS -> timestamp = timestamp / 1000 * 1000;
		}
		return timestamp;
	}

	private WordEntity getRandomWord()
	{
		log.trace("Searching word");

		WordEntity word = null;
		int tries = 0;
		while (word == null && tries < MAX_SEARCH_TRIES)
		{
			long count = wordRepo.count();
			int idx = (int) (Math.random() * count);
			Page<WordEntity> wordPage = wordRepo.findAll(PageRequest.of(idx, 1));

			if (!wordPage.hasContent())
				throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No known words yet");

			word = wordPage.getContent().get(0);
			log.trace("Found '{}' for index {}", word, idx);
			if (word.getVerified() < minVerificationsForRiddle && word.getVerified() >= 0)
				word = null;
			tries++;
		}

		if (word == null)
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No known word found");

		return word;
	}
}
