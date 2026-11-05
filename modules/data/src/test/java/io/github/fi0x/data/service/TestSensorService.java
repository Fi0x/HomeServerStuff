package io.github.fi0x.data.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
class TestSensorService
{
	@InjectMocks
	private SensorService service;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);
	}

	@ParameterizedTest
	@CsvSource({"60,25,0.91", "60,24,0.85", "60,20,0.66", "40,25,1.54", "83,22,0.14", "70,34,1.03", "54,22.3,0.93"})
	void test_calculateVPD(double h, double t, double expected)
	{
		Assertions.assertEquals(expected, service.calculateVPD(h, t));
	}
}
