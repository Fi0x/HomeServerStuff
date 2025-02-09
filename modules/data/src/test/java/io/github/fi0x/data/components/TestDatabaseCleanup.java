package io.github.fi0x.data.components;

import io.github.fi0x.data.db.DataRepo;
import io.github.fi0x.data.db.SensorRepo;
import io.github.fi0x.data.db.entities.DataEntity;
import io.github.fi0x.data.db.entities.SensorEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class TestDatabaseCleanup
{
	private final String SENSOR_ADDRESS1 = "123.123.234.564";
	private final String SENSOR_ADDRESS2 = "123.098.234.564";
	private final String SENSOR_NAME1 = "SADFsdf sdflkj";
	private final String SENSOR_NAME2 = "lkjho lkjhz";

	@Mock
	private DataRepo dataRepo;
	@Mock
	private SensorRepo sensorRepo;

	@InjectMocks
	private DatabaseCleanup component;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);
		ReflectionTestUtils.setField(component, "maxValueTime", 604800L);

		when(sensorRepo.findAll()).thenReturn(findAllSensorEntities());

		when(dataRepo.findFirstByAddressAndSensorOrderByTimestampAsc(SENSOR_ADDRESS1, SENSOR_NAME1)).thenReturn(
				Optional.of(getEntity(SENSOR_ADDRESS1, SENSOR_NAME1)));
		when(dataRepo.findFirstByAddressAndSensorOrderByTimestampAsc(SENSOR_ADDRESS2, SENSOR_NAME2)).thenReturn(
				Optional.of(getEntity(SENSOR_ADDRESS2, SENSOR_NAME2)));

		when(dataRepo.findFromSensorOlderThan(eq(SENSOR_ADDRESS1), eq(SENSOR_NAME1), any())).thenReturn(
				getDataEntities(SENSOR_ADDRESS1, SENSOR_NAME1));
		when(dataRepo.findFromSensorOlderThan(eq(SENSOR_ADDRESS2), eq(SENSOR_NAME2), any())).thenReturn(
				getDataEntities(SENSOR_ADDRESS2, SENSOR_NAME2));
	}

	@Test
	void test_cleanDatabase()
	{
		Assertions.assertDoesNotThrow(() -> component.cleanDatabase());

		verify(dataRepo, times(1)).findFirstByAddressAndSensorOrderByTimestampAsc(eq(SENSOR_ADDRESS1),
																				  eq(SENSOR_NAME1));
		verify(dataRepo, times(1)).findFirstByAddressAndSensorOrderByTimestampAsc(eq(SENSOR_ADDRESS2),
																				  eq(SENSOR_NAME2));
		verify(dataRepo, times(2)).findFromSensorOlderThan(any(), any(), any());
		//		TODO: Get test working again
		verify(dataRepo, times(0)).deleteAll(any());
		verify(dataRepo, times(0)).save(any());
	}

	private List<SensorEntity> findAllSensorEntities()
	{
		return List.of(SensorEntity.builder().address(SENSOR_ADDRESS1).name(SENSOR_NAME1).build(),
					   SensorEntity.builder().address(SENSOR_ADDRESS2).name(SENSOR_NAME2).build());
	}

	private DataEntity getEntity(String address, String name)
	{
		return DataEntity.builder().address(address).sensor(name).value(0.0).timestamp(0L).build();
	}

	private List<DataEntity> getDataEntities(String address, String name)
	{
		return List.of();
	}
}
