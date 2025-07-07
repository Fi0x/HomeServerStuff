package io.github.fi0x.wordle.db.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "WRGAMERES")
@IdClass(GameResultId.class)
public class GameResultEntity
{
	@Id
	private Long timestamp;
	@Id
	private String playerName;

	private Short tries;
	private Long requiredTime;
}
