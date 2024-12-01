package io.github.fi0x.recipes.logic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto
{
	private Long id;
	private String username;
	private boolean visible;

	private String name;
	private List<String> tags;
	private List<String> ingredients;
	private Integer time;
	private Float rating;

	private List<String> description;
	private Map<String, List<String>> additionalNotes;
}
