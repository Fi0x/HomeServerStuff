package io.fi0x.homeserver.recipes.rest;

import io.fi0x.homeserver.recipes.logic.dto.RecipeDto;
import io.fi0x.homeserver.recipes.service.RecipeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.Collections;
import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
@SessionAttributes({})
public class RecipeController
{
	private RecipeService recipeService;

	@GetMapping("/recipe/random")
	public String getRandomRecipe(ModelMap model)
	{
		//TODO: Add actual filter options
		List<RecipeDto> possibleRecipes = recipeService.getAllowedRecipes(Collections.emptyList(),
																		  Collections.emptyList());
		RecipeDto recipe = recipeService.getRandomRecipe(possibleRecipes);

		model.put("recipe", recipe);
		return "show-recipe";
	}

	@GetMapping("/recipes")
	public String getRecipeList(ModelMap model)
	{
		List<RecipeDto> possibleRecipes = recipeService.getAllRecipes();

		model.put("recipeList", possibleRecipes);
		return "recipes";
	}
}
