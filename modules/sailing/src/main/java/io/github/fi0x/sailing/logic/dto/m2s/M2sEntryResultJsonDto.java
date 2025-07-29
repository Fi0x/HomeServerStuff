package io.github.fi0x.sailing.logic.dto.m2s;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Locale;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class M2sEntryResultJsonDto
{
	@JsonProperty("Id")
	private String id;
	@JsonProperty("Rank")
	private Integer position;
	@JsonProperty("BoatType")
	private String shipClass;
	@JsonProperty("BoatName")
	private String shipName;
	@JsonProperty("EntryRaceResults")
	private List<M2sEntryRaceResultJsonDto> resultEntries;
	@JsonProperty("Subtotals")
	private List<M2sSubtotalsJsonDto> totals;
	@JsonProperty("TotalPoints")
	private String totalPoints;
	@JsonProperty("NetPoints")
	private String netPoints;
	@JsonProperty("Skipper")
	private SkipperDto skipperDto;

	public String getSkipperName()
	{
		return skipperDto.firstName + " " + skipperDto.lastName.toUpperCase(Locale.ROOT);
	}

	private static class SkipperDto
	{
		@JsonProperty("FirstName")
		private String firstName;
		@JsonProperty("LastName")
		private String lastName;
	}
}
