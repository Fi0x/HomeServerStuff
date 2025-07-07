package io.github.fi0x.wordle.logic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameResultDto
{
	private Long timestamp;
	private String playerName;
	private Short tries;
	private Long requiredTime;
}
