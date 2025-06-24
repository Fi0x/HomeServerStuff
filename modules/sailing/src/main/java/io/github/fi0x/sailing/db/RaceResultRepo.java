package io.github.fi0x.sailing.db;

import io.github.fi0x.sailing.db.entities.RaceResultEntity;
import io.github.fi0x.sailing.db.entities.RaceResultId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RaceResultRepo extends JpaRepository<RaceResultEntity, RaceResultId>
{
	List<RaceResultEntity> findAllByNameAndStartDate(String name, Long startDate);
}
