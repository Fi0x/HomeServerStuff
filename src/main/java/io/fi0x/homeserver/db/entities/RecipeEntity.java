package io.fi0x.homeserver.db.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "RECIPES")
public class RecipeEntity
{
	@Id
	private Long id;

	private String name;
	private String tags;
	private String ingredients;
	private Integer time;
}
