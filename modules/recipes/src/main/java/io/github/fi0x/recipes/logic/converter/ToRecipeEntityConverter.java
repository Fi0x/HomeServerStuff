package io.github.fi0x.recipes.logic.converter;

import io.github.fi0x.recipes.db.entities.RecipeEntity;
import io.github.fi0x.recipes.logic.dto.RecipeDto;

public class ToRecipeEntityConverter
{
	public static RecipeEntity convert(RecipeDto recipeDto) throws IllegalArgumentException
	{
		if(recipeDto.getId() == null)
			throw new IllegalArgumentException("RecipeDto does not contain a valid id");
		if(recipeDto.getUsername() == null)
			throw new IllegalArgumentException("RecipeDto does not contain a valid username");

		return RecipeEntity.builder().id(recipeDto.getId()).username(recipeDto.getUsername())
						   .visible(recipeDto.isVisible()).name(recipeDto.getName())
						   .tags(recipeDto.getTags().toString()).ingredients(recipeDto.getIngredients().toString())
						   .time(recipeDto.getTime()).rating(recipeDto.getRating()).build();
	}
}
