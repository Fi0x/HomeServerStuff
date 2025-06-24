package io.github.fi0x.sailing.db;

import io.github.fi0x.sailing.db.entities.RaceEntity;
import io.github.fi0x.sailing.db.entities.RaceId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RaceRepo extends JpaRepository<RaceEntity, RaceId>
{
	List<RaceEntity> findAllByOrcRaceOOrderByStartDateAsc(Boolean orcRace);
}
