package io.fi0x.homeserver.recipes.service;

import io.fi0x.homeserver.recipes.db.RecipeRepo;
import io.fi0x.homeserver.recipes.logic.converter.ToRecipeDtoConverter;
import io.fi0x.homeserver.recipes.logic.dto.RecipeDto;
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


    public List<RecipeDto> getAllowedRecipes(List<String> requiredIngredients, List<String> forbiddenIngredients,
                                             Float minRating, Float maxRating, Float minTime, Float maxTime)
    {
        return getAllRecipes().stream().filter(recipeDto ->
                        isAllowed(recipeDto, requiredIngredients, forbiddenIngredients, minRating, maxRating, minTime, maxTime))
                .toList();
    }

    public List<RecipeDto> getAllowedRecipes(List<String> requiredTags, List<String> forbiddenTags)
    {
        return getAllRecipes().stream().filter(recipeDto -> isAllowed(recipeDto, requiredTags, forbiddenTags)).toList();
    }

    public List<RecipeDto> getAllRecipes()
    {
        return recipeRepo.findAll().stream().map(ToRecipeDtoConverter::convert).toList();
    }

    public RecipeDto getRandomRecipe(List<RecipeDto> possibleRecipes)
    {
        return possibleRecipes.get((int) (Math.random() * possibleRecipes.size()));
    }

    private static boolean isAllowed(RecipeDto dto, List<String> requiredTags, List<String> forbiddenTags)
    {
        for (String tag : dto.getTags()) {
            if (forbiddenTags.contains(tag))
                return false;
        }
        for (String tag : requiredTags) {
            if (!dto.getTags().contains(tag))
                return false;
        }
        return true;
    }

    private static boolean isAllowed(RecipeDto dto, List<String> requiredIngredients, List<String> forbiddenIngredients,
                                     Float minRating, Float maxRating, Float minTime, Float maxTime)
    {
        for (String ingredient : dto.getIngredients()) {
            if (forbiddenIngredients.contains(ingredient))
                return false;
        }
        for (String ingredient : requiredIngredients) {
            if (!dto.getIngredients().contains(ingredient))
                return false;
        }

        if (dto.getRating() > maxRating || dto.getRating() < minRating)
            return false;

        return dto.getTime() <= maxTime && dto.getTime() >= minTime;
    }
}
