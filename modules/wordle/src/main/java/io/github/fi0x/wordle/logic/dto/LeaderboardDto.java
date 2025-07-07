package io.github.fi0x.wordle.logic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardDto
{
	private Long gameTimestamp;
	private Date dateTime;
	private String word;
	private String type;
	private List<GameResultDto> playerResults;
}
