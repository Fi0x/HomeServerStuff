package io.github.fi0x.sailing.service;

import io.github.fi0x.sailing.components.Manage2SailRetriever;
import io.github.fi0x.sailing.db.RaceRepo;
import io.github.fi0x.sailing.db.RaceResultRepo;
import io.github.fi0x.sailing.db.entities.RaceEntity;
import io.github.fi0x.sailing.db.entities.RaceId;
import io.github.fi0x.sailing.db.entities.RaceResultEntity;
import io.github.fi0x.sailing.db.entities.RaceResultId;
import io.github.fi0x.sailing.logic.converter.RaceConverter;
import io.github.fi0x.sailing.logic.converter.RaceResultToDtoConverter;
import io.github.fi0x.sailing.logic.dto.RaceInfoDto;
import io.github.fi0x.sailing.logic.dto.RaceResultDto;
import io.github.fi0x.sailing.logic.dto.ShipRaceResults;
import io.github.fi0x.sailing.logic.dto.m2s.M2sClass;
import io.github.fi0x.util.components.Authenticator;
import io.github.fi0x.util.dto.UserRoles;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class TestRaceService
{
	private static final String RACE_NAME = "Fast race";
	private static final String WRONG_RACE = "Slow race";
	private static final String RACE_NAME2 = "Race 2";
	private static final String RACE_NAME3 = "Race 3";
	private static final String RACE_NAME4 = "Race 4";
	private static final String RACE_NAME5 = "Race 5";
	private static final Long START_DATE = 324089L;
	private static final Long WRONG_DATE = 2239049203498L;
	private static final String RACE_GROUP = "Fast group";
	private static final String WRONG_GROUP = "Slow ships";
	private static final String SKIPPER = "Hans Olaf";
	private static final String SKIPPER2 = "Klaus Dieter";
	private static final String RACE_URL = "someUrl.com";
	private static final String SHIP_NAME = "Falcon";
	private static final String SHIP_CLASS = "Psaros 33";

	@Mock
	private Authenticator authenticator;
	@Mock
	private RaceRepo raceRepo;
	@Mock
	private RaceResultRepo resultRepo;
	@Mock
	private RaceResultToDtoConverter raceResultConverter;
	@Mock
	private RaceConverter raceConverter;
	@Mock
	private Manage2SailRetriever m2sRetriever;

	@InjectMocks
	private RaceService service;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void test_getRace_success()
	{
		RaceEntity expected = RaceEntity.builder().build();
		when(raceRepo.findById(new RaceId(RACE_NAME, START_DATE, RACE_GROUP))).thenReturn(Optional.of(expected));

		Assertions.assertSame(expected, service.getRace(RACE_NAME, START_DATE, RACE_GROUP));
	}

	@Test
	void test_getRace_notFound()
	{
		when(raceRepo.findById(new RaceId(RACE_NAME, START_DATE, RACE_GROUP))).thenReturn(Optional.empty());

		Assertions.assertThrows(ResponseStatusException.class,
				() -> service.getRace(RACE_NAME, START_DATE, RACE_GROUP));
	}

	@Test
	void test_getAllOrcRaces_success()
	{
		List<RaceEntity> expected = List.of(
				RaceEntity.builder().name(RACE_NAME).raceGroup(RACE_GROUP).startDate(START_DATE).build());
		when(raceRepo.findAllByOrcRaceOrderByStartDateAsc(true)).thenReturn(
				List.of(RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP).build(),
						RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(WRONG_GROUP).build(),
						RaceEntity.builder().name(RACE_NAME).startDate(WRONG_DATE).raceGroup(RACE_GROUP).build()));

		Calendar c = new GregorianCalendar();
		c.setTime(new Date(START_DATE));
		Assertions.assertEquals(expected, service.getAllOrcRaces(RACE_GROUP, c.get(Calendar.YEAR)));
	}

	@Test
	void test_getAllOrcRaces_noYear()
	{
		List<RaceEntity> expected = List.of(
				RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP).build(),
				RaceEntity.builder().name(RACE_NAME).startDate(WRONG_DATE).raceGroup(RACE_GROUP).build());
		when(raceRepo.findAllByOrcRaceOrderByStartDateAsc(true)).thenReturn(
				List.of(RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP).build(),
						RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(WRONG_GROUP).build(),
						RaceEntity.builder().name(RACE_NAME).startDate(WRONG_DATE).raceGroup(RACE_GROUP).build()));

		Assertions.assertEquals(expected, service.getAllOrcRaces(RACE_GROUP, null));
	}

	@Test
	void test_getAllOrcRaces_noGroup()
	{
		List<RaceEntity> expected = List.of(
				RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP).build(),
				RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(WRONG_GROUP).build());
		when(raceRepo.findAllByOrcRaceOrderByStartDateAsc(true)).thenReturn(
				List.of(RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP).build(),
						RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(WRONG_GROUP).build(),
						RaceEntity.builder().name(RACE_NAME).startDate(WRONG_DATE).raceGroup(RACE_GROUP).build()));

		Calendar c = new GregorianCalendar();
		c.setTime(new Date(START_DATE));
		Assertions.assertEquals(expected, service.getAllOrcRaces(null, c.get(Calendar.YEAR)));
	}

	@Test
	void test_getAllRaces()
	{
		RaceEntity e1 = RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP).build();
		RaceEntity e2 = RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(WRONG_GROUP).build();
		RaceEntity e3 = RaceEntity.builder().name(RACE_NAME).startDate(WRONG_DATE).raceGroup(RACE_GROUP).build();
		RaceInfoDto dto1 = RaceInfoDto.builder().name(RACE_NAME).longDate(START_DATE).raceGroup(RACE_GROUP).build();
		RaceInfoDto dto2 = RaceInfoDto.builder().name(RACE_NAME).longDate(WRONG_DATE).raceGroup(RACE_GROUP).build();
		RaceInfoDto dto3 = RaceInfoDto.builder().name(RACE_NAME).longDate(START_DATE).raceGroup(WRONG_GROUP).build();
		List<RaceInfoDto> expected = List.of(dto1, dto2, dto3);
		List<RaceEntity> allRaces = new ArrayList<>();
		allRaces.add(e1);
		allRaces.add(e2);
		allRaces.add(e3);
		when(raceRepo.findAll()).thenReturn(allRaces);
		when(raceConverter.convert(e1)).thenReturn(dto1);
		when(raceConverter.convert(e2)).thenReturn(dto2);
		when(raceConverter.convert(e3)).thenReturn(dto3);

		Assertions.assertEquals(expected, service.getAllRaces());
	}

	@Test
	void test_getAllRaces_withGroupAndYear_success()
	{
		RaceEntity e1 = RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP).build();
		RaceEntity e2 = RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(WRONG_GROUP).build();
		RaceEntity e3 = RaceEntity.builder().name(RACE_NAME).startDate(WRONG_DATE).raceGroup(RACE_GROUP).build();
		List<RaceEntity> expected = List.of(e1, e2, e3);
		List<RaceEntity> allRaces = new ArrayList<>();
		allRaces.add(e1);
		allRaces.add(e3);
		allRaces.add(e2);
		when(raceRepo.findAll()).thenReturn(allRaces);

		Assertions.assertEquals(expected, service.getAllRaces(null, null));
	}

	@Test
	void test_getAllRaces_withGroupAndYear_group()
	{
		RaceEntity e1 = RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP).build();
		RaceEntity e2 = RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(WRONG_GROUP).build();
		RaceEntity e3 = RaceEntity.builder().name(RACE_NAME).startDate(WRONG_DATE).raceGroup(RACE_GROUP).build();
		List<RaceEntity> expected = List.of(e1, e3);
		List<RaceEntity> allRaces = new ArrayList<>();
		allRaces.add(e1);
		allRaces.add(e3);
		allRaces.add(e2);
		when(raceRepo.findAll()).thenReturn(allRaces);

		Assertions.assertEquals(expected, service.getAllRaces(RACE_GROUP, null));
	}

	@Test
	void test_getAllRaces_withGroupAndYear_year()
	{
		RaceEntity e1 = RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP).build();
		RaceEntity e2 = RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(WRONG_GROUP).build();
		RaceEntity e3 = RaceEntity.builder().name(RACE_NAME).startDate(WRONG_DATE).raceGroup(RACE_GROUP).build();
		List<RaceEntity> expected = List.of(e1, e2);
		List<RaceEntity> allRaces = new ArrayList<>();
		allRaces.add(e1);
		allRaces.add(e3);
		allRaces.add(e2);
		when(raceRepo.findAll()).thenReturn(allRaces);

		Calendar c = new GregorianCalendar();
		c.setTime(new Date(START_DATE));
		Assertions.assertEquals(expected, service.getAllRaces(null, c.get(Calendar.YEAR)));
	}

	@Test
	void test_getAllRaces_withGroupAndYear_both()
	{
		RaceEntity e1 = RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP).build();
		RaceEntity e2 = RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(WRONG_GROUP).build();
		RaceEntity e3 = RaceEntity.builder().name(RACE_NAME).startDate(WRONG_DATE).raceGroup(RACE_GROUP).build();
		List<RaceEntity> expected = List.of(e1);
		List<RaceEntity> allRaces = new ArrayList<>();
		allRaces.add(e1);
		allRaces.add(e3);
		allRaces.add(e2);
		when(raceRepo.findAll()).thenReturn(allRaces);

		Calendar c = new GregorianCalendar();
		c.setTime(new Date(START_DATE));
		Assertions.assertEquals(expected, service.getAllRaces(RACE_GROUP, c.get(Calendar.YEAR)));
	}

	@Test
	void test_getAllRaceGroupsWithYear()
	{
		Calendar c1 = new GregorianCalendar();
		c1.setTime(new Date(START_DATE));
		Calendar c2 = new GregorianCalendar();
		c2.setTime(new Date(WRONG_DATE));
		List<String> expected = List.of(RACE_GROUP + " - " + c1.get(Calendar.YEAR),
				WRONG_GROUP + " - " + c1.get(Calendar.YEAR), RACE_GROUP + " - " + c2.get(Calendar.YEAR));
		RaceEntity e1 = RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP).build();
		RaceEntity e2 = RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(WRONG_GROUP).build();
		RaceEntity e3 = RaceEntity.builder().name(RACE_NAME).startDate(WRONG_DATE).raceGroup(RACE_GROUP).build();
		RaceEntity e4 = RaceEntity.builder().name(WRONG_RACE).startDate(START_DATE).raceGroup(RACE_GROUP).build();
		when(raceRepo.findAll()).thenReturn(List.of(e1, e2, e3, e4));

		Assertions.assertEquals(expected, service.getAllRaceGroupsWithYear());
	}

	@Test
	void test_getAllResults_withGroup()
	{
		RaceResultDto dto =
				RaceResultDto.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP).build();
		List<RaceResultDto> expected = List.of(dto);
		RaceResultEntity entity =
				RaceResultEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP)
				.build();
		when(resultRepo.findAllByNameAndStartDateAndRaceGroup(RACE_NAME, START_DATE, RACE_GROUP)).thenReturn(
				List.of(entity));
		when(raceResultConverter.convert(entity)).thenReturn(dto);

		Assertions.assertEquals(expected, service.getAllResults(RACE_NAME, START_DATE, RACE_GROUP));
	}

	@Test
	void test_getAllResults_success()
	{
		List<ShipRaceResults> expected = List.of(
				ShipRaceResults.builder().shipName(SHIP_NAME).skipper(SKIPPER).shipClass(SHIP_CLASS).raceResults(
						List.of(RaceResultDto.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP)
								.skipper(SKIPPER).build())).build(),
				ShipRaceResults.builder().shipName(SHIP_NAME).skipper(SKIPPER2).shipClass(SHIP_CLASS).raceResults(
						List.of(RaceResultDto.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP)
								.skipper(SKIPPER2).build())).build());
		RaceResultEntity raceResultEntity1 = RaceResultEntity.builder().name(RACE_NAME).raceGroup(RACE_GROUP)
				.startDate(START_DATE).skipper(SKIPPER).shipName(SHIP_NAME).shipClass(SHIP_CLASS).build();
		RaceResultEntity raceResultEntity2 = RaceResultEntity.builder().name(RACE_NAME).raceGroup(RACE_GROUP)
				.startDate(START_DATE).skipper(SKIPPER2).shipName(SHIP_NAME).shipClass(SHIP_CLASS).build();
		List<RaceResultEntity> resultEntities = List.of(raceResultEntity1, raceResultEntity2,
				RaceResultEntity.builder().name(RACE_NAME).raceGroup(RACE_GROUP).startDate(WRONG_DATE).skipper(SKIPPER)
						.shipName(SHIP_NAME).shipClass(SHIP_CLASS).build(),
				RaceResultEntity.builder().name(RACE_NAME).raceGroup(WRONG_GROUP).startDate(START_DATE).skipper(SKIPPER)
						.shipName(SHIP_NAME).shipClass(SHIP_CLASS).build());
		when(resultRepo.findAll(any(Sort.class))).thenReturn(resultEntities);
		RaceResultDto resultDto1 = RaceResultDto.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP)
				.skipper(SKIPPER).build();
		RaceResultDto resultDto2 = RaceResultDto.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP)
				.skipper(SKIPPER2).build();
		when(raceResultConverter.convert(raceResultEntity1)).thenReturn(resultDto1);
		when(raceResultConverter.convert(raceResultEntity2)).thenReturn(resultDto2);
		RaceEntity re1 = RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP)
				.bufferRace(false).orcRace(false).build();
		RaceEntity re2 = RaceEntity.builder().name(RACE_NAME).startDate(WRONG_DATE).raceGroup(RACE_GROUP)
				.bufferRace(false).orcRace(false).build();
		RaceEntity re3 = RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(WRONG_GROUP)
				.bufferRace(false).orcRace(false).build();
		when(raceRepo.findAll()).thenReturn(List.of(re1, re2, re3));

		Calendar c = new GregorianCalendar();
		c.setTime(new Date(START_DATE));
		Assertions.assertEquals(expected, service.getAllResults(RACE_GROUP, c.get(Calendar.YEAR)));
	}

	@Test
	void test_getAllResults_noGroup()
	{
		List<ShipRaceResults> expected = List.of(
				ShipRaceResults.builder().shipName(SHIP_NAME).skipper(SKIPPER).shipClass(SHIP_CLASS).raceResults(
						List.of(RaceResultDto.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP)
								.skipper(SKIPPER).build())).build(),
				ShipRaceResults.builder().shipName(SHIP_NAME).skipper(SKIPPER2).shipClass(SHIP_CLASS).raceResults(
						List.of(RaceResultDto.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP)
								.skipper(SKIPPER2).build())).build(),
				ShipRaceResults.builder().shipName(SHIP_NAME).skipper(SKIPPER).shipClass(SHIP_CLASS).raceResults(
						List.of(RaceResultDto.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(WRONG_GROUP)
								.skipper(SKIPPER).build())).build());
		RaceResultEntity raceResultEntity1 = RaceResultEntity.builder().name(RACE_NAME).raceGroup(RACE_GROUP)
				.startDate(START_DATE).skipper(SKIPPER).shipName(SHIP_NAME).shipClass(SHIP_CLASS).build();
		RaceResultEntity raceResultEntity2 = RaceResultEntity.builder().name(RACE_NAME).raceGroup(RACE_GROUP)
				.startDate(START_DATE).skipper(SKIPPER2).shipName(SHIP_NAME).shipClass(SHIP_CLASS).build();
		RaceResultEntity raceResultEntity3 = RaceResultEntity.builder().name(RACE_NAME).raceGroup(RACE_GROUP)
				.startDate(WRONG_DATE).skipper(SKIPPER).shipName(SHIP_NAME).shipClass(SHIP_CLASS).build();
		RaceResultEntity raceResultEntity4 = RaceResultEntity.builder().name(RACE_NAME).raceGroup(WRONG_GROUP)
				.startDate(START_DATE).skipper(SKIPPER).shipName(SHIP_NAME).shipClass(SHIP_CLASS).build();
		List<RaceResultEntity> resultEntities = List.of(raceResultEntity1, raceResultEntity2, raceResultEntity3,
				raceResultEntity4);
		when(resultRepo.findAll(any(Sort.class))).thenReturn(resultEntities);
		RaceResultDto resultDto1 = RaceResultDto.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP)
				.skipper(SKIPPER).build();
		RaceResultDto resultDto2 = RaceResultDto.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP)
				.skipper(SKIPPER2).build();
		RaceResultDto resultDto3 = RaceResultDto.builder().name(RACE_NAME).startDate(WRONG_DATE).raceGroup(RACE_GROUP)
				.skipper(SKIPPER).build();
		RaceResultDto resultDto4 = RaceResultDto.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(WRONG_GROUP)
				.skipper(SKIPPER).build();
		when(raceResultConverter.convert(raceResultEntity1)).thenReturn(resultDto1);
		when(raceResultConverter.convert(raceResultEntity2)).thenReturn(resultDto2);
		when(raceResultConverter.convert(raceResultEntity3)).thenReturn(resultDto3);
		when(raceResultConverter.convert(raceResultEntity4)).thenReturn(resultDto4);
		RaceEntity re1 = RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP)
				.bufferRace(false).orcRace(false).build();
		RaceEntity re2 = RaceEntity.builder().name(RACE_NAME).startDate(WRONG_DATE).raceGroup(RACE_GROUP)
				.bufferRace(false).orcRace(false).build();
		RaceEntity re3 = RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(WRONG_GROUP)
				.bufferRace(false).orcRace(false).build();
		when(raceRepo.findAll()).thenReturn(List.of(re1, re2, re3));

		Calendar c = new GregorianCalendar();
		c.setTime(new Date(START_DATE));
		Assertions.assertEquals(expected, service.getAllResults(null, c.get(Calendar.YEAR)));
	}

	@Test
	void test_getAllResults_noYear()
	{
		List<ShipRaceResults> expected = List.of(
				ShipRaceResults.builder().shipName(SHIP_NAME).skipper(SKIPPER).shipClass(SHIP_CLASS).raceResults(
						List.of(RaceResultDto.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP)
								.skipper(SKIPPER).build())).build(),
				ShipRaceResults.builder().shipName(SHIP_NAME).skipper(SKIPPER2).shipClass(SHIP_CLASS).raceResults(
						List.of(RaceResultDto.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP)
								.skipper(SKIPPER2).build())).build(),
				ShipRaceResults.builder().shipName(SHIP_NAME).skipper(SKIPPER).shipClass(SHIP_CLASS).raceResults(
						List.of(RaceResultDto.builder().name(RACE_NAME).startDate(WRONG_DATE).raceGroup(RACE_GROUP)
								.skipper(SKIPPER).build())).build());
		RaceResultEntity raceResultEntity1 = RaceResultEntity.builder().name(RACE_NAME).raceGroup(RACE_GROUP)
				.startDate(START_DATE).skipper(SKIPPER).shipName(SHIP_NAME).shipClass(SHIP_CLASS).build();
		RaceResultEntity raceResultEntity2 = RaceResultEntity.builder().name(RACE_NAME).raceGroup(RACE_GROUP)
				.startDate(START_DATE).skipper(SKIPPER2).shipName(SHIP_NAME).shipClass(SHIP_CLASS).build();
		RaceResultEntity raceResultEntity3 = RaceResultEntity.builder().name(RACE_NAME).raceGroup(RACE_GROUP)
				.startDate(WRONG_DATE).skipper(SKIPPER).shipName(SHIP_NAME).shipClass(SHIP_CLASS).build();
		RaceResultEntity raceResultEntity4 = RaceResultEntity.builder().name(RACE_NAME).raceGroup(WRONG_GROUP)
				.startDate(START_DATE).skipper(SKIPPER).shipName(SHIP_NAME).shipClass(SHIP_CLASS).build();
		List<RaceResultEntity> resultEntities = List.of(raceResultEntity1, raceResultEntity2, raceResultEntity3,
				raceResultEntity4);
		when(resultRepo.findAll(any(Sort.class))).thenReturn(resultEntities);
		RaceResultDto resultDto1 = RaceResultDto.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP)
				.skipper(SKIPPER).build();
		RaceResultDto resultDto2 = RaceResultDto.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP)
				.skipper(SKIPPER2).build();
		RaceResultDto resultDto3 = RaceResultDto.builder().name(RACE_NAME).startDate(WRONG_DATE).raceGroup(RACE_GROUP)
				.skipper(SKIPPER).build();
		RaceResultDto resultDto4 = RaceResultDto.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(WRONG_GROUP)
				.skipper(SKIPPER).build();
		when(raceResultConverter.convert(raceResultEntity1)).thenReturn(resultDto1);
		when(raceResultConverter.convert(raceResultEntity2)).thenReturn(resultDto2);
		when(raceResultConverter.convert(raceResultEntity3)).thenReturn(resultDto3);
		when(raceResultConverter.convert(raceResultEntity4)).thenReturn(resultDto4);
		RaceEntity re1 = RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP)
				.bufferRace(false).orcRace(false).build();
		RaceEntity re2 = RaceEntity.builder().name(RACE_NAME).startDate(WRONG_DATE).raceGroup(RACE_GROUP)
				.bufferRace(false).orcRace(false).build();
		RaceEntity re3 = RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(WRONG_GROUP)
				.bufferRace(false).orcRace(false).build();
		when(raceRepo.findAll()).thenReturn(List.of(re1, re2, re3));

		Calendar c = new GregorianCalendar();
		c.setTime(new Date(START_DATE));
		Assertions.assertEquals(expected, service.getAllResults(RACE_GROUP, null));
	}

	@Test
	void test_getAllResults_noYearOrGroup()
	{
		List<ShipRaceResults> expected = List.of(
				ShipRaceResults.builder().shipName(SHIP_NAME).skipper(SKIPPER).shipClass(SHIP_CLASS).raceResults(
						List.of(RaceResultDto.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP)
								.skipper(SKIPPER).build())).build(),
				ShipRaceResults.builder().shipName(SHIP_NAME).skipper(SKIPPER2).shipClass(SHIP_CLASS).raceResults(
						List.of(RaceResultDto.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP)
								.skipper(SKIPPER2).build())).build(),
				ShipRaceResults.builder().shipName(SHIP_NAME).skipper(SKIPPER).shipClass(SHIP_CLASS).raceResults(
						List.of(RaceResultDto.builder().name(RACE_NAME).startDate(WRONG_DATE).raceGroup(RACE_GROUP)
										.skipper(SKIPPER).build(),
								RaceResultDto.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(WRONG_GROUP)
										.skipper(SKIPPER).build())).build());
		RaceResultEntity raceResultEntity1 = RaceResultEntity.builder().name(RACE_NAME).raceGroup(RACE_GROUP)
				.startDate(START_DATE).skipper(SKIPPER).shipName(SHIP_NAME).shipClass(SHIP_CLASS).build();
		RaceResultEntity raceResultEntity2 = RaceResultEntity.builder().name(RACE_NAME).raceGroup(RACE_GROUP)
				.startDate(START_DATE).skipper(SKIPPER2).shipName(SHIP_NAME).shipClass(SHIP_CLASS).build();
		RaceResultEntity raceResultEntity3 = RaceResultEntity.builder().name(RACE_NAME).raceGroup(RACE_GROUP)
				.startDate(WRONG_DATE).skipper(SKIPPER).shipName(SHIP_NAME).shipClass(SHIP_CLASS).build();
		RaceResultEntity raceResultEntity4 = RaceResultEntity.builder().name(RACE_NAME).raceGroup(WRONG_GROUP)
				.startDate(START_DATE).skipper(SKIPPER).shipName(SHIP_NAME).shipClass(SHIP_CLASS).build();
		List<RaceResultEntity> resultEntities = List.of(raceResultEntity1, raceResultEntity2, raceResultEntity3,
				raceResultEntity4);
		when(resultRepo.findAll(any(Sort.class))).thenReturn(resultEntities);
		RaceResultDto resultDto1 = RaceResultDto.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP)
				.skipper(SKIPPER).build();
		RaceResultDto resultDto2 = RaceResultDto.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP)
				.skipper(SKIPPER2).build();
		RaceResultDto resultDto3 = RaceResultDto.builder().name(RACE_NAME).startDate(WRONG_DATE).raceGroup(RACE_GROUP)
				.skipper(SKIPPER).build();
		RaceResultDto resultDto4 = RaceResultDto.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(WRONG_GROUP)
				.skipper(SKIPPER).build();
		when(raceResultConverter.convert(raceResultEntity1)).thenReturn(resultDto1);
		when(raceResultConverter.convert(raceResultEntity2)).thenReturn(resultDto2);
		when(raceResultConverter.convert(raceResultEntity3)).thenReturn(resultDto3);
		when(raceResultConverter.convert(raceResultEntity4)).thenReturn(resultDto4);
		RaceEntity re1 = RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP)
				.bufferRace(false).orcRace(false).build();
		RaceEntity re2 = RaceEntity.builder().name(RACE_NAME).startDate(WRONG_DATE).raceGroup(RACE_GROUP)
				.bufferRace(false).orcRace(false).build();
		RaceEntity re3 = RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(WRONG_GROUP)
				.bufferRace(false).orcRace(false).build();
		when(raceRepo.findAll()).thenReturn(List.of(re1, re2, re3));

		Calendar c = new GregorianCalendar();
		c.setTime(new Date(START_DATE));
		Assertions.assertEquals(expected, service.getAllResults(null, null));
	}

	@Test
	void test_getAllResults_withCrossed()
	{
		RaceResultDto race1 = RaceResultDto.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP)
				.skipper(SKIPPER).score(1.0).build();
		RaceResultDto race2 = RaceResultDto.builder().name(WRONG_RACE).startDate(START_DATE).raceGroup(RACE_GROUP)
				.skipper(SKIPPER).score(1.0).build();
		RaceResultDto race3 = RaceResultDto.builder().name(RACE_NAME2).startDate(START_DATE).raceGroup(RACE_GROUP)
				.skipper(SKIPPER).score(10.0).build();
		RaceResultDto race4 = RaceResultDto.builder().name(RACE_NAME3).startDate(START_DATE).raceGroup(RACE_GROUP)
				.skipper(SKIPPER).score(9.0).build();
		RaceResultDto race5 = RaceResultDto.builder().name(RACE_NAME4).startDate(START_DATE).raceGroup(RACE_GROUP)
				.skipper(SKIPPER).score(5.0).build();
		RaceResultDto race6 = RaceResultDto.builder().name(RACE_NAME5).startDate(START_DATE).raceGroup(RACE_GROUP)
				.skipper(SKIPPER).score(7.0).build();
		List<ShipRaceResults> expected = List.of(
				ShipRaceResults.builder().shipName(SHIP_NAME).skipper(SKIPPER).shipClass(SHIP_CLASS)
						.raceResults(List.of(race1, race2, race3, race4, race5, race6)).build());
		RaceResultEntity raceResultEntity1 = RaceResultEntity.builder().name(RACE_NAME).raceGroup(RACE_GROUP)
				.startDate(START_DATE).skipper(SKIPPER).shipName(SHIP_NAME).shipClass(SHIP_CLASS).score(1.0).build();
		RaceResultEntity raceResultEntity2 = RaceResultEntity.builder().name(WRONG_RACE).raceGroup(RACE_GROUP)
				.startDate(START_DATE).skipper(SKIPPER).shipName(SHIP_NAME).shipClass(SHIP_CLASS).score(1.0).build();
		RaceResultEntity raceResultEntity3 = RaceResultEntity.builder().name(RACE_NAME2).raceGroup(RACE_GROUP)
				.startDate(START_DATE).skipper(SKIPPER).shipName(SHIP_NAME).shipClass(SHIP_CLASS).score(10.0).build();
		RaceResultEntity raceResultEntity4 = RaceResultEntity.builder().name(RACE_NAME3).raceGroup(RACE_GROUP)
				.startDate(START_DATE).skipper(SKIPPER).shipName(SHIP_NAME).shipClass(SHIP_CLASS).score(9.0).build();
		RaceResultEntity raceResultEntity5 = RaceResultEntity.builder().name(RACE_NAME4).raceGroup(RACE_GROUP)
				.startDate(START_DATE).skipper(SKIPPER).shipName(SHIP_NAME).shipClass(SHIP_CLASS).score(5.0).build();
		RaceResultEntity raceResultEntity6 = RaceResultEntity.builder().name(RACE_NAME5).raceGroup(RACE_GROUP)
				.startDate(START_DATE).skipper(SKIPPER).shipName(SHIP_NAME).shipClass(SHIP_CLASS).score(7.0).build();
		List<RaceResultEntity> resultEntities = List.of(raceResultEntity1, raceResultEntity2, raceResultEntity3,
				raceResultEntity4, raceResultEntity5, raceResultEntity6);
		when(resultRepo.findAll(any(Sort.class))).thenReturn(resultEntities);
		when(raceResultConverter.convert(raceResultEntity1)).thenReturn(race1);
		when(raceResultConverter.convert(raceResultEntity2)).thenReturn(race2);
		when(raceResultConverter.convert(raceResultEntity3)).thenReturn(race3);
		when(raceResultConverter.convert(raceResultEntity4)).thenReturn(race4);
		when(raceResultConverter.convert(raceResultEntity5)).thenReturn(race5);
		when(raceResultConverter.convert(raceResultEntity6)).thenReturn(race6);
		RaceEntity re1 = RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP)
				.bufferRace(true).orcRace(true).build();
		RaceEntity re2 = RaceEntity.builder().name(WRONG_RACE).startDate(START_DATE).raceGroup(RACE_GROUP)
				.bufferRace(true).orcRace(true).build();
		RaceEntity re3 = RaceEntity.builder().name(RACE_NAME2).startDate(START_DATE).raceGroup(RACE_GROUP)
				.bufferRace(true).orcRace(true).build();
		RaceEntity re4 = RaceEntity.builder().name(RACE_NAME3).startDate(START_DATE).raceGroup(RACE_GROUP)
				.bufferRace(true).orcRace(true).build();
		RaceEntity re5 = RaceEntity.builder().name(RACE_NAME4).startDate(START_DATE).raceGroup(RACE_GROUP)
				.bufferRace(true).orcRace(true).build();
		RaceEntity re6 = RaceEntity.builder().name(RACE_NAME5).startDate(START_DATE).raceGroup(RACE_GROUP)
				.bufferRace(true).orcRace(true).build();
		when(raceRepo.findAll()).thenReturn(List.of(re1, re2, re3, re4, re5, re6));

		Calendar c = new GregorianCalendar();
		c.setTime(new Date(START_DATE));
		Assertions.assertEquals(expected, service.getAllResults(RACE_GROUP, c.get(Calendar.YEAR)));
	}

	@Test
	void test_getRaceClasses_success() throws IOException
	{
		List<M2sClass> expected = List.of(M2sClass.builder().build());
		when(m2sRetriever.getRaceClasses(RACE_URL)).thenReturn(expected);

		Assertions.assertEquals(expected, service.getRaceClasses(RACE_URL));
	}

	@Test
	void test_getRaceClasses_unauthorized() throws IOException
	{
		doThrow(new ResponseStatusException(HttpStatus.FORBIDDEN)).when(authenticator).restAuthenticate();

		Assertions.assertThrows(ResponseStatusException.class, () -> service.getRaceClasses(RACE_URL));
	}

	@Test
	void test_getRaceClasses_noResults() throws IOException
	{
		when(m2sRetriever.getRaceClasses(RACE_URL)).thenReturn(Collections.emptyList());

		Assertions.assertThrows(ResponseStatusException.class, () -> service.getRaceClasses(RACE_URL));
	}

	@Test
	void test_getRaceClasses_ioException() throws IOException
	{
		when(m2sRetriever.getRaceClasses(RACE_URL)).thenThrow(new IOException());

		Assertions.assertThrows(ResponseStatusException.class, () -> service.getRaceClasses(RACE_URL));
	}

	@Test
	void test_loadSpecificRaceClassResults()
	{
		Assertions.fail();
	}

	@Test
	void test_saveRaceInformation()
	{
		Assertions.fail();
	}

	@Test
	void test_saveRaceResults()
	{
		Assertions.fail();
	}

	@Test
	void test_deleteResult_success()
	{
		Assertions.assertDoesNotThrow(() -> service.deleteResult(RACE_NAME, START_DATE, RACE_GROUP, SKIPPER));
		verify(authenticator, times(1)).restAuthenticate(UserRoles.ADMIN);
		verify(resultRepo, times(1)).deleteById(new RaceResultId(RACE_NAME, START_DATE, RACE_GROUP, SKIPPER));
	}

	@Test
	void test_deleteResult_noSkipper_success()
	{
		RaceEntity entity = RaceEntity.builder().name(RACE_NAME).startDate(START_DATE).raceGroup(RACE_GROUP).build();
		when(raceRepo.findById(new RaceId(RACE_NAME, START_DATE, RACE_GROUP))).thenReturn(Optional.of(entity));

		Assertions.assertDoesNotThrow(() -> service.deleteResult(RACE_NAME, START_DATE, RACE_GROUP, null));
		verify(authenticator, times(1)).restAuthenticate(UserRoles.ADMIN);
		verify(resultRepo, times(1)).deleteAllByNameAndStartDateAndRaceGroup(RACE_NAME, START_DATE, RACE_GROUP);
		verify(raceRepo, times(1)).delete(entity);
	}

	@Test
	void test_deleteResult_noSkipper_notFound()
	{
		when(raceRepo.findById(new RaceId(RACE_NAME, START_DATE, RACE_GROUP))).thenReturn(Optional.empty());

		Assertions.assertThrows(ResponseStatusException.class,
				() -> service.deleteResult(RACE_NAME, START_DATE, RACE_GROUP, null));
		verify(authenticator, times(1)).restAuthenticate(UserRoles.ADMIN);
		verify(raceRepo, times(1)).findById(any());
		verify(resultRepo, never()).deleteAllByNameAndStartDateAndRaceGroup(anyString(), anyLong(), anyString());
		verify(raceRepo, never()).delete(any());
	}

	@Test
	void test_deleteResult_unauthorized()
	{
		doThrow(new ResponseStatusException(HttpStatus.FORBIDDEN)).when(authenticator)
				.restAuthenticate(UserRoles.ADMIN);

		Assertions.assertThrows(ResponseStatusException.class,
				() -> service.deleteResult(RACE_NAME, START_DATE, RACE_GROUP, SKIPPER));
		verify(authenticator, times(1)).restAuthenticate(UserRoles.ADMIN);
	}

	@Test
	void test_updateRace()
	{
		Assertions.fail();
	}
}
