package io.fi0x.homeserver.recipes.logic.converter;


import io.fi0x.homeserver.recipes.db.entities.RecipeEntity;
import io.fi0x.homeserver.recipes.logic.dto.RecipeDto;

import java.util.Arrays;
import java.util.List;

public class ToRecipeDtoConverter
{
	public static RecipeDto convert(RecipeEntity entity)
	{
		return RecipeDto.builder().id(entity.getId()).username(entity.getUsername()).visible(entity.getVisible())
						.name(entity.getName()).tags(convertToList(entity.getTags()))
						.ingredients(convertToList(entity.getIngredients())).time(entity.getTime())
						.rating(entity.getRating()).build();
	}

	private static List<String> convertToList(String stringList)
	{
		return Arrays.stream(stringList.replace(" ", "").split(",")).toList();
	}
}
