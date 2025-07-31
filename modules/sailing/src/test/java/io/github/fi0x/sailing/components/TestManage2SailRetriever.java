package io.github.fi0x.sailing.components;

import io.github.fi0x.sailing.logic.dto.m2s.M2sClass;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class TestManage2SailRetriever
{
	private static final String OVERVIEW_PAGE_URL = "manage2sail.com/overview";
	private static final String OVERVIEW_HTML_FILE = "overview.html";
	private static final String EVENT_NAME = "74. RUND UM 2025";
	private static final String FIRST_CLASS_NAME = "Mehrrumpfboote YSZ kleiner gleich 79 (SG0)";
	private static final String FIRST_CLASS_URL = "/de-DE/event/41eb060f-4eef-4406-a1bf-dd839852a42e/#!results?classId" +
			"=d446b41b-9d00-4590-9e7f-becd7fadccf0";
	private static final Long START_DATE = 1750284000000L;
	private static final Long END_DATE = 1750456800000L;
	private static final String EVENT_ID = "41eb060f-4eef-4406-a1bf-dd839852a42e";
	private static final Integer CLASSES = 4;

	private MockedStatic<Jsoup> staticMock;
	@Mock
	private Connection connection;
	@InjectMocks
	private Manage2SailRetriever component;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void cleanup()
	{
		staticMock.close();
	}

	@Test
	void test_getRaceClasses_success() throws IOException, URISyntaxException
	{
		staticMock = mockStatic(Jsoup.class, Mockito.CALLS_REAL_METHODS);
		staticMock.when(() -> Jsoup.connect(OVERVIEW_PAGE_URL)).thenReturn(connection);
		Document doc = getOverviewDocument();
		when(connection.get()).thenReturn(doc);

		List<M2sClass> results = component.getRaceClasses(OVERVIEW_PAGE_URL);
		Assertions.assertEquals(CLASSES, results.size());
		Assertions.assertEquals(EVENT_NAME, results.get(0).getRaceEventName());
		Assertions.assertEquals(FIRST_CLASS_NAME, results.get(0).getClassName());
		Assertions.assertEquals(FIRST_CLASS_URL, results.get(0).getClassUrl());
		Assertions.assertEquals(START_DATE, results.get(0).getStartDate());
		Assertions.assertEquals(END_DATE, results.get(0).getEndDate());
		Assertions.assertEquals(OVERVIEW_PAGE_URL, results.get(0).getEventUrl());
		Assertions.assertEquals(EVENT_ID, results.get(0).getEventId());
	}

	private Document getOverviewDocument() throws URISyntaxException, IOException
	{
		URL fileUrl = getClass().getClassLoader().getResource(OVERVIEW_HTML_FILE);
		File file = new File(Objects.requireNonNull(fileUrl).toURI());
		return Jsoup.parse(file);
	}
}
