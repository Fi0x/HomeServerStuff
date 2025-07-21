package io.github.fi0x.sailing.db;

import io.github.fi0x.sailing.db.entities.RaceResultEntity;
import io.github.fi0x.sailing.db.entities.RaceResultId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RaceResultRepo extends JpaRepository<RaceResultEntity, RaceResultId>
{
	List<RaceResultEntity> findAllByNameAndStartDateAndRaceGroup(String name, Long startDate, String raceGroup);
	void deleteAllByNameAndStartDateAndRaceGroup(String name, Long startDate, String raceGroup);
}
