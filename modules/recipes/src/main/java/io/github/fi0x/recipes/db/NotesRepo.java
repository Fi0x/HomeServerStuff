package io.github.fi0x.recipes.db;

import io.github.fi0x.recipes.db.entities.NotesEntity;
import io.github.fi0x.recipes.db.entities.TextId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NotesRepo extends JpaRepository<NotesEntity, TextId>
{
	List<NotesEntity> findAllByRecipeId(Long recipeId);

	@Query(value = "SELECT MAX(TEXT_NUMBER) FROM notes WHERE RECIPE_ID = ?1", nativeQuery = true)
	Optional<Long> getHighestTextNumber(Long recipeId);
}
