package io.github.fi0x.recipes.db;

import io.github.fi0x.recipes.db.entities.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecipeRepo extends JpaRepository<RecipeEntity, Long>
{
	List<RecipeEntity> findAllByUsernameOrVisible(String username, Boolean visible);

	@Query(value = "SELECT MAX(ID) FROM recipes", nativeQuery = true)
	Optional<Long> getHighestId();
}
