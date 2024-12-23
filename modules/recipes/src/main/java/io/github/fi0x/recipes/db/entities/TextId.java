package io.github.fi0x.recipes.db.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TextId implements Serializable
{
	private Long recipeId;
	private Long textNumber;
}
