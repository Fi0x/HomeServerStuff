package io.github.fi0x.wordle.logic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameSettings
{
	private String gameModeName;
	private Long timestamp;
	private String playerName;
	private Long started;
}
