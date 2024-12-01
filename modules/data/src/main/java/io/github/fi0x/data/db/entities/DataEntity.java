package io.github.fi0x.data.db.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DTDATA")
@IdClass(DataId.class)
public class DataEntity
{
	@Id
	private String sensor;
	@Id
	private Long timestamp;

	private Double value;
}
