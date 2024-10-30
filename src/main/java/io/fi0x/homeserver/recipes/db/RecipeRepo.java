package io.fi0x.homeserver.recipes.db;

import io.fi0x.homeserver.recipes.db.entities.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepo extends JpaRepository<RecipeEntity, Long>
{
}
