package io.github.fi0x.sailing.logic.converter;

import io.github.fi0x.sailing.logic.dto.orc.OrcOverviewXmlRowDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;

@RunWith(SpringRunner.class)
public class TestStringToOrcOverviewConverter
{
	private static final String LIST_FILE = "orcList.html";
	private static final String LIST_FILE_NPE = "orcListNPE.html";

	private static final String COUNTRY = "GER";
	private static final String DXT_ID = "217242";
	private static final String REF_NO = "03520003KD9";
	private static final String SHIP_NAME = "ALL IN";
	private static final String SAIL_NUMBER = "FII 99";
	private static final String SHIP_CLASS = "FIRST CLASS FIGARO 2";
	private static final String CERTIFICATE_NAME = "DH Club";

	@InjectMocks
	private StringToOrcOverviewConverter converter;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void test_convert_success() throws URISyntaxException, IOException
	{
		OrcOverviewXmlRowDto expected =
				OrcOverviewXmlRowDto.builder().CountryId(COUNTRY).dxtID(DXT_ID).refNo(REF_NO).YachtName(SHIP_NAME)
									.SailNo(SAIL_NUMBER).shipClass(SHIP_CLASS).CertName(CERTIFICATE_NAME).build();

		Assertions.assertEquals(expected, converter.convert(getOrcListString(LIST_FILE)));
	}

	@Test
	void test_convert_exception() throws URISyntaxException, IOException
	{
		Assertions.assertNull(converter.convert(getOrcListString(LIST_FILE_NPE)));
	}

	private String getOrcListString(String fileName) throws URISyntaxException, IOException
	{
		URL fileUrl = getClass().getClassLoader().getResource(fileName);
		File file = new File(Objects.requireNonNull(fileUrl).toURI());
		byte[] encoded = Files.readAllBytes(file.toPath());
		return new String(encoded, StandardCharsets.UTF_8);
	}
}