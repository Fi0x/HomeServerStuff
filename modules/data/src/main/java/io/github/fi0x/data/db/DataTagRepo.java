package io.github.fi0x.data.db;

import io.github.fi0x.data.db.entities.DataTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataTagRepo extends JpaRepository<DataTagEntity, Long>
{
}
