package io.github.fi0x.sailing.logic.dto.m2s;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class M2sClass
{
	private String raceEventName;
	private String className;
	private String classUrl;
	private Long startDate;
	private Long endDate;
	private String eventUrl;
	private String eventId;
}
