package io.github.fi0x.wordle.db.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor

public class GameResultId implements Serializable
{
	private Long timestamp;
	private String playerName;
}
