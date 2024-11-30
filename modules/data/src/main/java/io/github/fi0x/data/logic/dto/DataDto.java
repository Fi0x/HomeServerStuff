package io.github.fi0x.data.logic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataDto
{
	private String sender;
	private String type;
	private String value;
	private String unit;
	private List<String> tags;
}
