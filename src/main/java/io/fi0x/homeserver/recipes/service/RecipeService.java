package io.fi0x.homeserver.recipes.service;


import io.fi0x.homeserver.recipes.db.RecipeRepo;
import io.fi0x.homeserver.recipes.db.entities.RecipeEntity;
import io.fi0x.homeserver.recipes.logic.converter.ToRecipeDtoConverter;
import io.fi0x.homeserver.recipes.logic.dto.RecipeDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class RecipeService
{
	private RecipeRepo recipeRepo;

	private static boolean isAllowed(RecipeDto dto, List<String> requiredTags, List<String> forbiddenTags)
	{
		for(String tag : dto.getTags())
		{
			if(forbiddenTags.contains(tag))
				return false;
		}
		for(String tag : requiredTags)
		{
			if(!dto.getTags().contains(tag))
				return false;
		}
		return true;
	}

	private static boolean isAllowed(RecipeDto dto, List<String> requiredIngredients, List<String> forbiddenIngredients, Float minRating, Float maxRating, Float minTime, Float maxTime)
	{
		for(String ingredient : dto.getIngredients())
		{
			if(forbiddenIngredients.contains(ingredient))
				return false;
		}
		for(String ingredient : requiredIngredients)
		{
			if(!dto.getIngredients().contains(ingredient))
				return false;
		}

		if(dto.getRating() > maxRating || dto.getRating() < minRating)
			return false;

		return dto.getTime() <= maxTime && dto.getTime() >= minTime;
	}

	public List<RecipeDto> getAllowedRecipes(String username, List<String> requiredIngredients, List<String> forbiddenIngredients, Float minRating, Float maxRating, Float minTime, Float maxTime)
	{
		return getAllRecipes(username).stream().filter(recipeDto -> isAllowed(recipeDto, requiredIngredients,
																			  forbiddenIngredients, minRating,
																			  maxRating, minTime, maxTime)).toList();
	}

	public List<RecipeDto> getAllowedRecipes(String username, List<String> requiredTags, List<String> forbiddenTags)
	{
		return getAllRecipes(username).stream().filter(recipeDto -> isAllowed(recipeDto, requiredTags, forbiddenTags))
									  .toList();
	}

	public List<RecipeDto> getAllRecipes(String username)
	{
		if(username == null)
			throw new ResponseStatusException(HttpStatusCode.valueOf(403), "Could not find a username");
		return recipeRepo.findAll().stream().filter(dto -> dto.getUsername().equals(username) || dto.getVisible())
						 .map(ToRecipeDtoConverter::convert).toList();
	}

	public RecipeDto getRandomRecipe(List<RecipeDto> possibleRecipes)
	{
		return possibleRecipes.get((int) (Math.random() * possibleRecipes.size()));
	}

	public RecipeDto getRecipe(Long recipeId)
	{
		RecipeEntity recipeEntity = recipeRepo.findById(recipeId).orElseThrow(
				() -> new ResponseStatusException(HttpStatusCode.valueOf(404),
												  "Could not find a recipe with id '" + recipeId + "'"));
		return ToRecipeDtoConverter.convert(recipeEntity);
	}
}
