package io.fi0x.homeserver.recipes.service;


import io.fi0x.homeserver.general.components.Authenticator;
import io.fi0x.homeserver.recipes.db.RecipeRepo;
import io.fi0x.homeserver.recipes.db.entities.RecipeEntity;
import io.fi0x.homeserver.recipes.logic.converter.ToRecipeDtoConverter;
import io.fi0x.homeserver.recipes.logic.dto.RecipeDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class RecipeService
{
	private RecipeRepo recipeRepo;

	private Authenticator authenticator;

	private static boolean ingredientsAllowed(RecipeDto dto, List<String> requiredIngredients,
											  List<String> forbiddenIngredients)
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
		return true;
	}

	private static boolean tagsAllowed(RecipeDto dto, List<String> requiredTags, List<String> forbiddenTags)
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

	private static boolean numbersAllowed(RecipeDto dto, Float minRating, Float maxRating, Float minTime, Float maxTime)
	{
		boolean maxRatingOk = maxRating == null || dto.getRating() <= maxRating;
		boolean minRatingOk = minRating == null || dto.getRating() >= minRating;
		boolean maxTimeOk = maxTime == null || dto.getTime() <= maxTime;
		boolean minTimeOk = minTime == null || dto.getTime() >= minTime;

		return maxRatingOk && minRatingOk && maxTimeOk && minTimeOk;
	}

	public List<RecipeDto> getAllowedRecipes(List<String> requiredIngredients, List<String> forbiddenIngredients,
											 List<String> requiredTags, List<String> forbiddenTags, Float minRating,
											 Float maxRating, Float minTime, Float maxTime)
	{
		return getAllRecipes().stream().filter(recipeDto -> {
			boolean ingredients = ingredientsAllowed(recipeDto, requiredIngredients, forbiddenIngredients);
			boolean tags = tagsAllowed(recipeDto, requiredTags, forbiddenTags);
			boolean numbers = numbersAllowed(recipeDto, minRating, maxRating, minTime, maxTime);
			return ingredients && tags && numbers;
		}).toList();
	}

	public List<RecipeDto> getAllRecipes()
	{
		List<RecipeEntity> recipeEntities = recipeRepo.findAllByUsernameOrVisible(
				authenticator.getAuthenticatedUsername(), true);

		List<RecipeDto> dtoList = new ArrayList<>();
		for(RecipeEntity entity : recipeEntities)
		{
			try
			{
				dtoList.add(ToRecipeDtoConverter.convertFully(entity));
			} catch(InvalidObjectException e)
			{
				log.warn("Could not convert a recipe-entity to a dto.", e);
			}
		}

		return dtoList;
	}

	public RecipeDto getRecipe(Long recipeId) throws InvalidObjectException
	{
		RecipeEntity recipeEntity = recipeRepo.findById(recipeId).orElseThrow(
				() -> new ResponseStatusException(HttpStatusCode.valueOf(404),
												  "Could not find a recipe with id '" + recipeId + "'"));

		if(!recipeEntity.getUsername().equals(authenticator.getAuthenticatedUsername()) && !recipeEntity.getVisible())
			throw new ResponseStatusException(HttpStatusCode.valueOf(403),
											  "You are not authorized to view this recipe");

		return ToRecipeDtoConverter.convertFully(recipeEntity);
	}

	public void deleteRecipe(Long recipeId)
	{
		RecipeEntity recipeEntity = recipeRepo.findById(recipeId).orElseThrow(
				() -> new ResponseStatusException(HttpStatusCode.valueOf(404),
												  "Could not find a recipe with id '" + recipeId + "'"));

		if(!recipeEntity.getUsername().equals(authenticator.getAuthenticatedUsername()))
			throw new ResponseStatusException(HttpStatusCode.valueOf(403),
											  "You are not authorized to delete this recipe");

		recipeRepo.deleteById(recipeId);
	}
}
