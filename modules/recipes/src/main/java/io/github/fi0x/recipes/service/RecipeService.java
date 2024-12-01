package io.github.fi0x.recipes.service;

import io.github.fi0x.recipes.db.DescriptionRepo;
import io.github.fi0x.recipes.db.NotesRepo;
import io.github.fi0x.recipes.db.RecipeRepo;
import io.github.fi0x.recipes.db.entities.DescriptionEntity;
import io.github.fi0x.recipes.db.entities.NotesEntity;
import io.github.fi0x.recipes.db.entities.RecipeEntity;
import io.github.fi0x.recipes.logic.converter.ToRecipeDtoConverter;
import io.github.fi0x.recipes.logic.converter.ToRecipeEntityConverter;
import io.github.fi0x.recipes.logic.dto.RecipeDto;
import io.github.fi0x.util.components.Authenticator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.InvalidObjectException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class RecipeService
{
	private RecipeRepo recipeRepo;
	private DescriptionRepo descriptionRepo;
	private NotesRepo notesRepo;

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

	public List<RecipeDto> getAllRecipes()
	{
		log.trace("getAllRecipes() called");

		List<RecipeEntity> recipeEntities =
				recipeRepo.findAllByUsernameOrVisible(authenticator.getAuthenticatedUsername(), true);

		List<RecipeDto> dtoList = new ArrayList<>();
		for(RecipeEntity entity : recipeEntities)
		{
			try
			{
				RecipeDto recipeDto = ToRecipeDtoConverter.convertFully(entity);
				recipeDto.setDescription(Collections.emptyList());
				recipeDto.setAdditionalNotes(Collections.emptyMap());
				dtoList.add(recipeDto);
			} catch(InvalidObjectException e)
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

		if(!recipeEntity.getUsername().equals(authenticator.getAuthenticatedUsername()) && !recipeEntity.getVisible())
			throw new ResponseStatusException(HttpStatusCode.valueOf(403),
											  "You are not authorized to view this recipe");

		RecipeDto recipeDto = ToRecipeDtoConverter.convertFully(recipeEntity);
		recipeDto.setDescription(getDescriptions(recipeDto.getId()));
		recipeDto.setAdditionalNotes(getAdditionalNotes(recipeDto.getId()));
		return recipeDto;
	}

	public void saveRecipe(RecipeDto recipeDto)
	{
		log.trace("saveRecipe() called");

		if(recipeDto.getUsername() != null && !recipeDto.getUsername()
														.isBlank() && !authenticator.getAuthenticatedUsername()
																					.equals(recipeDto.getUsername()))
			throw new ResponseStatusException(HttpStatusCode.valueOf(403), "User is not allowed to save this recipe");

		if(recipeDto.getUsername() == null || recipeDto.getUsername().isBlank())
			recipeDto.setUsername(authenticator.getAuthenticatedUsername());

		if(recipeDto.getId() == null)
			recipeDto.setId(recipeRepo.getHighestId().orElse(-1L) + 1);

		try
		{
			recipeDto.setTags(recipeDto.getTags() == null ? Collections.emptyList() : recipeDto.getTags().stream()
																							   .filter(tag -> !tag.isBlank())
																							   .toList());
			recipeDto.setIngredients(
					recipeDto.getIngredients() == null ? Collections.emptyList() : recipeDto.getIngredients().stream()
																							.filter(ingredient -> !ingredient.isBlank())
																							.toList());
			recipeRepo.save(ToRecipeEntityConverter.convert(recipeDto));
		} catch(IllegalArgumentException e)
		{
			log.warn("Could not save or convert a recipe", e);
			throw new ResponseStatusException(HttpStatusCode.valueOf(400), e.getMessage());
		}
	}

	public void deleteRecipe(Long recipeId)
	{
		log.trace("deleteRecipe() called");

		RecipeEntity recipeEntity = recipeRepo.findById(recipeId).orElseThrow(
				() -> new ResponseStatusException(HttpStatusCode.valueOf(404),
												  "Could not find a recipe with id '" + recipeId + "'"));

		if(!recipeEntity.getUsername().equals(authenticator.getAuthenticatedUsername()))
			throw new ResponseStatusException(HttpStatusCode.valueOf(403),
											  "You are not authorized to delete this recipe");

		recipeRepo.deleteById(recipeId);
	}

	private static boolean numbersAllowed(RecipeDto dto, Float minRating, Float maxRating, Float minTime, Float maxTime)
	{
		boolean maxRatingOk = maxRating == null || dto.getRating() <= maxRating;
		boolean minRatingOk = minRating == null || dto.getRating() >= minRating;
		boolean maxTimeOk = maxTime == null || dto.getTime() <= maxTime;
		boolean minTimeOk = minTime == null || dto.getTime() >= minTime;

		return maxRatingOk && minRatingOk && maxTimeOk && minTimeOk;
	}

	private List<String> getDescriptions(Long recipeId)
	{
		List<DescriptionEntity> entities = descriptionRepo.findAllByRecipeId(recipeId);
		entities.sort(Comparator.comparing(DescriptionEntity::getTextNumber));
		return entities.stream().map(DescriptionEntity::getText).toList();
	}

	private Map<String, List<String>> getAdditionalNotes(Long recipeId)
	{
		List<NotesEntity> entities = notesRepo.findAllByRecipeId(recipeId);

		Map<String, List<NotesEntity>> entityMap =
				entities.stream().collect(Collectors.groupingBy(NotesEntity::getUsername));

		Map<String, List<String>> resultMap = new HashMap<>();
		for(Map.Entry<String, List<NotesEntity>> entry : entityMap.entrySet())
		{
			resultMap.put(entry.getKey(),
						  entry.getValue().stream().sorted(Comparator.comparing(NotesEntity::getTextNumber))
							   .map(NotesEntity::getText).toList());
		}

		return resultMap;
	}
}
