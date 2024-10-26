package io.fi0x.homeserver.db;

import io.fi0x.homeserver.db.entities.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepo extends JpaRepository<RecipeEntity, Long>
{
}
