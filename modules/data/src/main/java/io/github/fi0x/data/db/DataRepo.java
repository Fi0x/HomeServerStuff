package io.github.fi0x.data.db;

import io.github.fi0x.data.db.entities.DataEntity;
import io.github.fi0x.data.db.entities.DataId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataRepo extends JpaRepository<DataEntity, DataId>
{
}
