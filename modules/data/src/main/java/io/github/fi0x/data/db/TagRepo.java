package io.github.fi0x.data.db;

import io.github.fi0x.data.db.entities.TagEntity;
import io.github.fi0x.data.db.entities.TagId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepo extends JpaRepository<TagEntity, TagId>
{
	List<TagEntity> findAllBySensorName(String sensorName);
}
