package io.github.fi0x.wordle.service;

import io.github.fi0x.wordle.logic.dto.KeyDto;
import io.github.fi0x.wordle.logic.dto.KeyboardDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DataService
{
	private static final KeyboardDto KEYBOARD_LAYOUT_DE = KeyboardDto.builder().firstRow(
			List.of(new KeyDto("Q", 1), new KeyDto("W" + " ", 1), new KeyDto("E ", 1), new KeyDto("R ", 1),
					new KeyDto("T ", 1), new KeyDto("Y ", 1), new KeyDto("U ", 1), new KeyDto("I ", 1),
					new KeyDto("O ", 1), new KeyDto("P ", 1), new KeyDto("Ü", 1))).secondRow(
			List.of(new KeyDto("A ", 1), new KeyDto("S ", 1), new KeyDto("D ", 1), new KeyDto("F ", 1),
					new KeyDto("G ", 1), new KeyDto("H ", 1), new KeyDto("J ", 1), new KeyDto("K ", 1),
					new KeyDto("L ", 1), new KeyDto("Ö ", 1), new KeyDto("Ä", 1))).thirdRow(
			List.of(new KeyDto("<-", 2), new KeyDto("Y ", 1), new KeyDto("X ", 1), new KeyDto("C ", 1),
					new KeyDto("V ", 1), new KeyDto("B ", 1), new KeyDto("N ", 1), new KeyDto("M ", 1),
					new KeyDto("Enter", 2))).build();

	public KeyboardDto getKeyboardData()
	{
		return KEYBOARD_LAYOUT_DE;
	}
}
