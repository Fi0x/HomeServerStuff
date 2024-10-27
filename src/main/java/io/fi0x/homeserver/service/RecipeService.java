package io.fi0x.homeserver.service;

import io.fi0x.homeserver.db.RecipeRepo;
import io.fi0x.homeserver.logic.converter.ToRecipeDtoConverter;
import io.fi0x.homeserver.logic.dto.RecipeDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

	//TODO: Add filter options for ingredients and time
	public List<RecipeDto> getAllowedRecipes(List<String> requiredTags, List<String> forbiddenTags)
	{
		return recipeRepo.findAll().stream().map(ToRecipeDtoConverter::convert)
						 .filter(recipeDto -> isAllowed(recipeDto, requiredTags, forbiddenTags))
						 .toList();
	}

	public RecipeDto getRandomRecipe(List<RecipeDto> possibleRecipes)
	{
		return possibleRecipes.get((int) (Math.random() * possibleRecipes.size()));
	}
}
