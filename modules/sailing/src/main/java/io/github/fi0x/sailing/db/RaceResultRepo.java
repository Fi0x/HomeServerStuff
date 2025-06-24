package io.github.fi0x.sailing.db;

import io.github.fi0x.sailing.db.entities.RaceId;
import io.github.fi0x.sailing.db.entities.RaceResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RaceResultRepo extends JpaRepository<RaceResultEntity, RaceId>
{
}
