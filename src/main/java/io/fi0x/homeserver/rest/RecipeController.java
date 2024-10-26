package io.fi0x.homeserver.rest;

import io.fi0x.homeserver.logic.dto.RecipeDto;
import io.fi0x.homeserver.service.RecipeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Slf4j
@Controller
@AllArgsConstructor
@SessionAttributes({})
public class RecipeController
{
	private RecipeService recipeService;

	@GetMapping("/random-recipe")
	public String getRandomRecipe(ModelMap model)
	{

		RecipeDto recipe = recipeService.getRandomRecipe();
		return null;
	}
}
