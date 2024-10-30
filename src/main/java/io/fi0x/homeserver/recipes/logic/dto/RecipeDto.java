package io.fi0x.homeserver.recipes.logic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class RecipeDto
{
	private Long id;

	private String name;
	private List<String> tags;
	private List<String> ingredients;
	private Integer time;
	private Float rating;
}
