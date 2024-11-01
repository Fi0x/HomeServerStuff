package io.fi0x.homeserver.recipes.rest;


import io.fi0x.homeserver.general.service.AuthenticationService;
import io.fi0x.homeserver.recipes.logic.dto.RecipeDto;
import io.fi0x.homeserver.recipes.service.RecipeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
@SessionAttributes({"username"})
public class RecipeController
{
	private AuthenticationService authenticationService;
	private RecipeService recipeService;

	@GetMapping("/recipe/random")
	public String getRandomRecipe(ModelMap model)
	{
		log.info("getRandomRecipe() called");

		//TODO: Add actual filter options
		List<RecipeDto> possibleRecipes = recipeService.getAllowedRecipes(
				authenticationService.getAuthenticatedUsername(), Collections.emptyList(), Collections.emptyList());
		RecipeDto recipe = recipeService.getRandomRecipe(possibleRecipes);

		model.put("recipe", recipe);
		return "redirect:/recipe/" + recipe.getId();
	}

	@GetMapping("/recipes")
	public String getRecipeList(ModelMap model)
	{
		log.info("getRecipeList() called");

		model.put("recipeList", recipeService.getAllRecipes(authenticationService.getAuthenticatedUsername()));

		return "recipes";
	}

	@GetMapping("/recipe/{recipeId}")
	public String showRecipe(ModelMap model, @PathVariable Long recipeId)
	{
		log.info("showRecipe() called");

		RecipeDto recipe = recipeService.getRecipe(recipeId);
		if(!recipe.getVisible() && !recipe.getUsername().equals(authenticationService.getAuthenticatedUsername()))
			throw new ResponseStatusException(HttpStatusCode.valueOf(404),
											  "Could not find a recipe with id " + recipeId);

		model.put("recipe", recipe);
		return "show-recipe";
	}

	@GetMapping("/recipe/{recipeId}/edit")
	public String editRecipe(ModelMap model, @PathVariable Long recipeId)
	{
		log.info("editRecipe() called");

		model.put("recipe", recipeService.getRecipe(recipeId));

		return "edit-recipe";
	}

	@GetMapping("/recipe/{recipeId}/delete")
	public String deleteRecipe(ModelMap model, @PathVariable Long recipeId)
	{
		log.info("deleteRecipe() called");

		//TODO: Delete recipe

		model.put("recipeList", recipeService.getAllRecipes(authenticationService.getAuthenticatedUsername()));

		return "recipes";
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
