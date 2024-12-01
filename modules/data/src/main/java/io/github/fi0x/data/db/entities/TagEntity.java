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
@Table(name = "DTTAGS")
@IdClass(TagId.class)
public class TagEntity
{
	@Id
	private String sensorName;
	@Id
	private String tag;
}
