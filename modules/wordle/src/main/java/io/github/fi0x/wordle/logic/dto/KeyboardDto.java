package io.github.fi0x.wordle.logic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeyboardDto
{
	private List<KeyDto> firstRow;
	private List<KeyDto> secondRow;
	private List<KeyDto> thirdRow;
}
