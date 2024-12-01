package io.github.fi0x.recipes.db.entities;

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
@Table(name = "DESCRIPTIONS")
@IdClass(TextId.class)
public class DescriptionEntity
{
	@Id
	private Long recipeId;
	@Id
	private Long textNumber;

	private String text;
}
