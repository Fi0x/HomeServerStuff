package io.github.fi0x.sailing.components;

import io.github.fi0x.sailing.logic.dto.RaceResultDto;
import io.github.fi0x.sailing.logic.dto.m2s.M2sClass;
import io.github.fi0x.sailing.logic.dto.m2s.M2sClassResultsJsonDto;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class Manage2SailRetriever
{
	private final RestTemplate restTemplate = new RestTemplate();

	public List<M2sClass> getRaceClasses(String raceOverviewUrl) throws IOException
	{
		Document raceOverviewPage = Jsoup.connect(raceOverviewUrl).get();
		Element raceClassesScript = raceOverviewPage.getElementById("classes");
		if (raceClassesScript == null)
		{
			log.debug("raceClassesScript could not be found in html page");
			return Collections.emptyList();
		}
		Element raceClassesDivDoc = Jsoup.parse(raceClassesScript.data().trim());
		Element raceClassesTable = raceClassesDivDoc.getElementsByTag("table").first();
		if (raceClassesTable == null)
		{
			log.debug("raceClassesTable could not be found in script on html page");
			return Collections.emptyList();
		}
		Element tableBody = raceClassesTable.getElementsByTag("tbody").first();
		if (tableBody == null)
		{
			log.debug("Could not find a body in the classes-table");
			return Collections.emptyList();
		}
		Element dateElement = raceOverviewPage.getElementsByClass("eventDates").first();
		if (dateElement == null)
		{
			log.debug("Could not find a date element on the html-page");
			return Collections.emptyList();
		}
		String[] dateStrings = dateElement.text().split(" - ");
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
		long startDate;
		long endDate;
		try
		{
			startDate = dateFormatter.parse(dateStrings[0]).getTime();
			if (dateStrings.length > 1)
				endDate = dateFormatter.parse(dateStrings[1]).getTime();
			else
				endDate = startDate;
		} catch (ParseException e)
		{
			log.debug("Could not convert the date-String to a valid start- or end-date");
			return Collections.emptyList();
		}
		Element eventNameElement = raceOverviewPage.getElementsByClass("eventName").first();
		if (eventNameElement == null)
		{
			log.debug("Could not find an element for the event-name on the html-page");
			return Collections.emptyList();
		}
		String eventName = eventNameElement.child(0).text();

		Element detailsScript = raceOverviewPage.getElementById("details");
		if (detailsScript == null || detailsScript.childNodeSize() == 0)
		{
			log.debug("Could not find details-script or its children");
			return Collections.emptyList();
		}
		Element detailsDivElement = Jsoup.parse(detailsScript.data().trim());

		Element infoTable = detailsDivElement.getElementsByClass("table-info").first();
		if (infoTable == null)
		{
			log.debug("Could not find an element for the table-info on the html-page");
			return Collections.emptyList();
		}
		Element infoTableBody = infoTable.children().last();
		if (infoTableBody == null)
		{
			log.debug("Could not find a body for the info-table");
			return Collections.emptyList();
		}
		Element idElement = infoTableBody.children().last();
		if (idElement == null)
		{
			log.debug("Could not find an element for the id on the html-page");
			return Collections.emptyList();
		}
		String eventId = idElement.child(1).text();

		Elements rows = tableBody.children();

		List<M2sClass> classes = new ArrayList<>();
		rows.forEach(element -> {
			M2sClass m2sClass = getClass(element, eventId, eventName, startDate, endDate, raceOverviewUrl);
			if (m2sClass != null)
				classes.add(m2sClass);
		});
		return classes;
	}

	public List<RaceResultDto> getClassResults(String classUrl, String eventName, Long startDate, Long endDate,
											   String eventUrl)
	{
		M2sClassResultsJsonDto classResultsJson = restTemplate.getForObject(classUrl, M2sClassResultsJsonDto.class);

		List<RaceResultDto> resultList = new ArrayList<>();
		if (classResultsJson.getResults().isEmpty())
		{
			String groupName = classResultsJson.getClassName();
			classResultsJson.getEntries().forEach(m2sEntryResultJsonDto -> resultList.add(
					RaceResultDto.builder().name(eventName).startDate(startDate).raceGroup(groupName)
								 .skipper(m2sEntryResultJsonDto.getSkipperName()).endDate(endDate).url(eventUrl)
								 .shipName(m2sEntryResultJsonDto.getShipName())
								 .position(m2sEntryResultJsonDto.getPosition())
								 .resultStatusCode(m2sEntryResultJsonDto.getResultEntries().get(0).getRaceStatusCode())
								 .shipClass(Objects.requireNonNullElse(m2sEntryResultJsonDto.getShipClass(),
																	   groupName))
								 .build()));
		} else
		{
			classResultsJson.getResults().forEach(m2sScoreResultJsonDto -> {
				String subName = m2sScoreResultJsonDto.getName();
				m2sScoreResultJsonDto.getResult().getEntries().forEach(subEntry -> resultList.add(
						RaceResultDto.builder().name(eventName).startDate(startDate).raceGroup(subName)
									 .skipper(subEntry.getSkipperName()).endDate(endDate).url(eventUrl)
									 .shipName(subEntry.getShipName()).position(subEntry.getPosition())
									 .resultStatusCode(subEntry.getResultEntries().get(0).getRaceStatusCode())
									 .shipClass(Objects.requireNonNullElse(subEntry.getShipClass(), subName)).build()));
			});
		}

		return resultList;
	}

	private M2sClass getClass(Element row, String eventId, String eventName, Long startDate, Long endDate,
							  String eventUrl)
	{
		Element resultUrlCell = row.child(2);
		if (resultUrlCell.children().isEmpty())
			return null;

		Element urlAnchor = resultUrlCell.child(0);
		M2sClass raceClass = new M2sClass();
		raceClass.setClassName(row.child(0).text());
		raceClass.setClassUrl(urlAnchor.attribute("href").getValue());
		raceClass.setRaceEventName(eventName);
		raceClass.setStartDate(startDate);
		raceClass.setEndDate(endDate);
		raceClass.setEventUrl(eventUrl);
		raceClass.setEventId(eventId);
		return raceClass;
	}
}
