package io.github.fi0x.recipes.service;

import io.github.fi0x.recipes.db.RecipeRepo;
import io.github.fi0x.recipes.db.entities.RecipeEntity;
import io.github.fi0x.recipes.logic.converter.ToRecipeDtoConverter;
import io.github.fi0x.recipes.logic.dto.RecipeDto;
import io.github.fi0x.util.components.Authenticator;
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


	public List<RecipeDto> getAllowedRecipes(List<String> requiredIngredients, List<String> forbiddenIngredients,
											 List<String> requiredTags, List<String> forbiddenTags, Float minRating,
											 Float maxRating, Float minTime, Float maxTime)
	{
		log.trace("getAllowedRecipes() called");

		return getAllRecipes().stream().filter(recipeDto -> {
			boolean ingredients = ingredientsAllowed(recipeDto, requiredIngredients, forbiddenIngredients);
			boolean tags = tagsAllowed(recipeDto, requiredTags, forbiddenTags);
			boolean numbers = numbersAllowed(recipeDto, minRating, maxRating, minTime, maxTime);
			return ingredients && tags && numbers;
		}).toList();
	}

	public List<RecipeDto> getAllRecipes()
	{
		log.trace("getAllRecipes() called");

		List<RecipeEntity> recipeEntities = recipeRepo.findAllByUsernameOrVisible(
				authenticator.getAuthenticatedUsername(), true);

		List<RecipeDto> dtoList = new ArrayList<>();
		for (RecipeEntity entity : recipeEntities)
		{
			try
			{
				dtoList.add(ToRecipeDtoConverter.convertFully(entity));
			} catch (InvalidObjectException e)
			{
				log.warn("Could not convert a recipe-entity to a dto.", e);
			}
		}

		return dtoList;
	}

	public RecipeDto getRecipe(Long recipeId) throws InvalidObjectException
	{
		log.trace("getRecipe() called");

		RecipeEntity recipeEntity = recipeRepo.findById(recipeId).orElseThrow(
				() -> new ResponseStatusException(HttpStatusCode.valueOf(404),
												  "Could not find a recipe with id '" + recipeId + "'"));

		if (!recipeEntity.getUsername().equals(authenticator.getAuthenticatedUsername()) && !recipeEntity.getVisible())
			throw new ResponseStatusException(HttpStatusCode.valueOf(403),
											  "You are not authorized to view this recipe");

		return ToRecipeDtoConverter.convertFully(recipeEntity);
	}

	public void deleteRecipe(Long recipeId)
	{
		log.trace("deleteRecipe() called");

		RecipeEntity recipeEntity = recipeRepo.findById(recipeId).orElseThrow(
				() -> new ResponseStatusException(HttpStatusCode.valueOf(404),
												  "Could not find a recipe with id '" + recipeId + "'"));

		if (!recipeEntity.getUsername().equals(authenticator.getAuthenticatedUsername()))
			throw new ResponseStatusException(HttpStatusCode.valueOf(403),
											  "You are not authorized to delete this recipe");

		recipeRepo.deleteById(recipeId);
	}

	private static boolean ingredientsAllowed(RecipeDto dto, List<String> requiredIngredients,
											  List<String> forbiddenIngredients)
	{
		for (String ingredient : dto.getIngredients())
		{
			if (forbiddenIngredients.contains(ingredient))
				return false;
		}
		for (String ingredient : requiredIngredients)
		{
			if (!dto.getIngredients().contains(ingredient))
				return false;
		}
		return true;
	}

	private static boolean tagsAllowed(RecipeDto dto, List<String> requiredTags, List<String> forbiddenTags)
	{
		for (String tag : dto.getTags())
		{
			if (forbiddenTags.contains(tag))
				return false;
		}
		for (String tag : requiredTags)
		{
			if (!dto.getTags().contains(tag))
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
}
