package io.github.fi0x.data.db.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "DATA")
public class DataEntity
{
	@Id
	private Long id;
	private String sender;

	private String type;
	private String value;
	private String unit;
}
