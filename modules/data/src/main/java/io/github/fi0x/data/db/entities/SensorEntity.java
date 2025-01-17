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
@Table(name = "DTSENSORS")
@IdClass(SensorId.class)
public class SensorEntity
{
	@Id
	private String address;
	@Id
	private String name;

	private Long lastUpdate;

	private String description;
	private String unit;
	private String type;
	private Long dataDelay;
	private Double valueAdjustment;
	private Double minValue;
	private Double maxValue;
}
