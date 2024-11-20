package io.github.fi0x.recipes.logic.converter;


import io.github.fi0x.recipes.db.entities.RecipeEntity;
import io.github.fi0x.recipes.logic.dto.RecipeDto;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ToRecipeDtoConverter
{

	public static RecipeDto convertFully(RecipeEntity entity) throws InvalidObjectException
	{
		if(entity.getId() == null)
			throw new InvalidObjectException("Cannot convert entity to dto, id is null");

		if(entity.getUsername() == null)
			throw new InvalidObjectException("Cannot convert entity to dto, username is null");

		if(entity.getVisible() == null)
			throw new InvalidObjectException("Cannot convert entity to dto, visibility is null");

		if(entity.getName() == null)
			throw new InvalidObjectException("Cannot convert entity to dto, name is null");

		if(entity.getTime() == null)
			throw new InvalidObjectException("Cannot convert entity to dto, time is null");

		if(entity.getRating() == null)
			throw new InvalidObjectException("Cannot convert entity to dto, rating is null");

		return convert(entity);
	}

	private static RecipeDto convert(RecipeEntity entity)
	{
		return RecipeDto.builder().id(entity.getId()).username(entity.getUsername()).visible(entity.getVisible())
						.name(entity.getName()).tags(convertToList(entity.getTags()))
						.ingredients(convertToList(entity.getIngredients())).time(entity.getTime())
						.rating(entity.getRating()).build();
	}

	private static List<String> convertToList(String stringList)
	{
		if(stringList == null)
			return new ArrayList<>();

		return Arrays.stream(stringList.replace(" ", "").split(",")).toList();
	}
}
