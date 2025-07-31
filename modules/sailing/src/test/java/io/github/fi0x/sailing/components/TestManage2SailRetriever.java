package io.github.fi0x.sailing.components;

import io.github.fi0x.sailing.logic.dto.m2s.M2sClass;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
	private static final String OVERVIEW_HTML_FILE_SINGLE_DATE = "overviewSingleDate.html";
	private static final String OVERVIEW_HTML_FILE_NO_CLASSES_TABLE = "overviewNoClassesTable.html";
	private static final String OVERVIEW_HTML_FILE_NO_CLASSES_BODY = "overviewNoClassesBody.html";
	private static final String OVERVIEW_HTML_FILE_WRONG_DATE = "overviewWrongDate.html";
	private static final String OVERVIEW_HTML_FILE_NO_NAME = "overviewNoName.html";
	private static final String OVERVIEW_HTML_FILE_NO_DETAILS = "overviewNoDetails.html";
	private static final String OVERVIEW_HTML_FILE_NO_INFO_TABLE = "overviewNoInfoTable.html";
	private static final String OVERVIEW_HTML_FILE_NO_INFO_BODY = "overviewNoInfoBody.html";
	private static final String OVERVIEW_HTML_FILE_NO_ID = "overviewNoId.html";
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

		staticMock = mockStatic(Jsoup.class, Mockito.CALLS_REAL_METHODS);
		staticMock.when(() -> Jsoup.connect(OVERVIEW_PAGE_URL)).thenReturn(connection);
	}

	@AfterEach
	void cleanup()
	{
		staticMock.close();
	}

	@Test
	void test_getRaceClasses_success() throws IOException, URISyntaxException
	{
		Document doc = getOverviewDocument(OVERVIEW_HTML_FILE);
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

	@Test
	void test_getRaceClasses_success_singleDate() throws IOException, URISyntaxException
	{
		Document doc = getOverviewDocument(OVERVIEW_HTML_FILE_SINGLE_DATE);
		when(connection.get()).thenReturn(doc);

		List<M2sClass> results = component.getRaceClasses(OVERVIEW_PAGE_URL);
		Assertions.assertEquals(CLASSES, results.size());
		Assertions.assertEquals(EVENT_NAME, results.get(0).getRaceEventName());
		Assertions.assertEquals(FIRST_CLASS_NAME, results.get(0).getClassName());
		Assertions.assertEquals(FIRST_CLASS_URL, results.get(0).getClassUrl());
		Assertions.assertEquals(START_DATE, results.get(0).getStartDate());
		Assertions.assertEquals(START_DATE, results.get(0).getEndDate());
		Assertions.assertEquals(OVERVIEW_PAGE_URL, results.get(0).getEventUrl());
		Assertions.assertEquals(EVENT_ID, results.get(0).getEventId());
	}

	@Test
	void test_getRaceClasses_noClassesScript() throws IOException, URISyntaxException
	{
		Document doc = getOverviewDocument(OVERVIEW_HTML_FILE);
		Element classesScript = doc.getElementById("classes");
		classesScript.parent().children().remove(classesScript);
		when(connection.get()).thenReturn(doc);

		List<M2sClass> results = component.getRaceClasses(OVERVIEW_PAGE_URL);
		Assertions.assertTrue(results.isEmpty());
	}

	@Test
	void test_getRaceClasses_noClassesTable() throws IOException, URISyntaxException
	{
		Document doc = getOverviewDocument(OVERVIEW_HTML_FILE_NO_CLASSES_TABLE);
		when(connection.get()).thenReturn(doc);

		List<M2sClass> results = component.getRaceClasses(OVERVIEW_PAGE_URL);
		Assertions.assertTrue(results.isEmpty());
	}

	@Test
	void test_getRaceClasses_noClassesBody() throws IOException, URISyntaxException
	{
		Document doc = getOverviewDocument(OVERVIEW_HTML_FILE_NO_CLASSES_BODY);
		when(connection.get()).thenReturn(doc);

		List<M2sClass> results = component.getRaceClasses(OVERVIEW_PAGE_URL);
		Assertions.assertTrue(results.isEmpty());
	}

	@Test
	void test_getRaceClasses_noDate() throws IOException, URISyntaxException
	{
		Document doc = getOverviewDocument(OVERVIEW_HTML_FILE);
		Element dateElement = doc.getElementsByClass("eventDates").first();
		dateElement.parent().children().remove(dateElement);
		when(connection.get()).thenReturn(doc);

		List<M2sClass> results = component.getRaceClasses(OVERVIEW_PAGE_URL);
		Assertions.assertTrue(results.isEmpty());
	}

	@Test
	void test_getRaceClasses_wrongDate() throws IOException, URISyntaxException
	{
		Document doc = getOverviewDocument(OVERVIEW_HTML_FILE_WRONG_DATE);
		when(connection.get()).thenReturn(doc);

		List<M2sClass> results = component.getRaceClasses(OVERVIEW_PAGE_URL);
		Assertions.assertTrue(results.isEmpty());
	}

	@Test
	void test_getRaceClasses_noEventName() throws IOException, URISyntaxException
	{
		Document doc = getOverviewDocument(OVERVIEW_HTML_FILE_NO_NAME);
		when(connection.get()).thenReturn(doc);

		List<M2sClass> results = component.getRaceClasses(OVERVIEW_PAGE_URL);
		Assertions.assertTrue(results.isEmpty());
	}

	@Test
	void test_getRaceClasses_noDetails() throws IOException, URISyntaxException
	{
		Document doc = getOverviewDocument(OVERVIEW_HTML_FILE_NO_DETAILS);
		when(connection.get()).thenReturn(doc);

		List<M2sClass> results = component.getRaceClasses(OVERVIEW_PAGE_URL);
		Assertions.assertTrue(results.isEmpty());
	}

	@Test
	void test_getRaceClasses_noInfoTable() throws IOException, URISyntaxException
	{
		Document doc = getOverviewDocument(OVERVIEW_HTML_FILE_NO_INFO_TABLE);
		when(connection.get()).thenReturn(doc);

		List<M2sClass> results = component.getRaceClasses(OVERVIEW_PAGE_URL);
		Assertions.assertTrue(results.isEmpty());
	}

	@Test
	void test_getRaceClasses_noInfoBody() throws IOException, URISyntaxException
	{
		Document doc = getOverviewDocument(OVERVIEW_HTML_FILE_NO_INFO_BODY);
		when(connection.get()).thenReturn(doc);

		List<M2sClass> results = component.getRaceClasses(OVERVIEW_PAGE_URL);
		Assertions.assertTrue(results.isEmpty());
	}

	@Test
	void test_getRaceClasses_noId() throws IOException, URISyntaxException
	{
		Document doc = getOverviewDocument(OVERVIEW_HTML_FILE_NO_ID);
		when(connection.get()).thenReturn(doc);

		List<M2sClass> results = component.getRaceClasses(OVERVIEW_PAGE_URL);
		Assertions.assertTrue(results.isEmpty());
	}

	private Document getOverviewDocument(String fileName) throws URISyntaxException, IOException
	{
		URL fileUrl = getClass().getClassLoader().getResource(fileName);
		File file = new File(Objects.requireNonNull(fileUrl).toURI());
		return Jsoup.parse(file);
	}
}
