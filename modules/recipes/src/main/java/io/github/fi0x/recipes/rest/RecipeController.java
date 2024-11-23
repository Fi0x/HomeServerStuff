package io.github.fi0x.recipes.rest;

import io.github.fi0x.recipes.logic.dto.RecipeDto;
import io.github.fi0x.recipes.service.RecipeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.io.InvalidObjectException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
@SessionAttributes({"username"})
public class RecipeController
{
	private RecipeService recipeService;

	@GetMapping({"/"})
	public String getRecipeList(ModelMap model)
	{
		log.info("getRecipeList() called");

		model.put("recipeList", recipeService.getAllRecipes());

		return "recipe-list";
	}

	@GetMapping("/recipe/random")
	public String getRandomRecipe(ModelMap model)
	{
		log.info("getRandomRecipe() called");

		//TODO: Add actual filter options
		List<RecipeDto> possibleRecipes =
				recipeService.getAllowedRecipes(Collections.emptyList(), Collections.emptyList(),
												Collections.emptyList(), Collections.emptyList(), 0f, 0f, 0f, 0f);
		if(possibleRecipes.isEmpty())
			throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Could not find any available recipes");

		RecipeDto recipe = possibleRecipes.get((int) (Math.random() * possibleRecipes.size()));

		model.put("recipe", recipe);
		return "redirect:/recipe/" + recipe.getId();
	}

	@GetMapping("/recipe/{recipeId}")
	public String showRecipe(ModelMap model, @PathVariable Long recipeId)
	{
		log.info("showRecipe() called");

		try
		{
			model.put("recipe", recipeService.getRecipe(recipeId));
		} catch(InvalidObjectException e)
		{
			log.warn("Could not convert a recipe-entity to a dto.", e);
			throw new ResponseStatusException(HttpStatusCode.valueOf(500),
											  "Could not convert a recipe-entity to a dto.", e);
		}

		return "show-recipe";
	}

	@GetMapping("/recipe/{recipeId}/edit")
	public String editRecipe(ModelMap model, @PathVariable Long recipeId)
	{
		log.info("editRecipe() called");

		try
		{
			model.put("recipe", recipeService.getRecipe(recipeId));
		} catch(InvalidObjectException e)
		{
			log.warn("Could not convert a recipe-entity to a dto.", e);
			throw new ResponseStatusException(HttpStatusCode.valueOf(500),
											  "Could not convert a recipe-entity to a dto.", e);
		}

		return "edit-recipe";
	}

	@GetMapping("/recipe/{recipeId}/delete")
	public String deleteRecipe(ModelMap model, @PathVariable Long recipeId)
	{
		log.info("deleteRecipe() called");

		recipeService.deleteRecipe(recipeId);

		model.put("recipeList", recipeService.getAllRecipes());

		return "recipe-list";
	}

	@GetMapping("/recipe")
	public String newRecipe(ModelMap model)
	{
		log.info("newRecipe() called");

		RecipeDto recipe = new RecipeDto();

		model.put("recipe", recipe);
		return "edit-recipe";
	}
}
