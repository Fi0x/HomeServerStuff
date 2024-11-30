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
@Table(name = "DATATAGS")
@IdClass(DataTagId.class)
public class DataTagEntity
{
	@Id
	private Long dataId;
	@Id
	private Long tagId;
}
