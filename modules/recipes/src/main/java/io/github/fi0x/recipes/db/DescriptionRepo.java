package io.github.fi0x.recipes.db;

import io.github.fi0x.recipes.db.entities.DescriptionEntity;
import io.github.fi0x.recipes.db.entities.TextId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DescriptionRepo extends JpaRepository<DescriptionEntity, TextId>
{
	List<DescriptionEntity> findAllByRecipeId(Long recipeId);

	@Query(value = "SELECT MAX(TEXT_NUMBER) FROM descriptions WHERE RECIPE_ID = ?1", nativeQuery = true)
	Optional<Long> getHighestTextNumber(Long recipeId);
}
