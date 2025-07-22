package io.github.fi0x.sailing.components;

import io.github.fi0x.sailing.logic.dto.RaceResultDto;
import io.github.fi0x.sailing.logic.dto.m2s.M2sClass;
import io.github.fi0x.sailing.logic.dto.m2s.M2sClassResultsJsonDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class Manage2SailRetriever
{
	public List<M2sClass> getRaceClasses(String raceOverviewUrl) throws IOException
	{
		Document raceOverviewPage = Jsoup.connect(raceOverviewUrl).get();
		Element raceClassesScript = raceOverviewPage.getElementById("classes");
		if(raceClassesScript == null)
			return Collections.emptyList();
		if(raceClassesScript.childNodeSize() == 0)
			return Collections.emptyList();
		Element raceClassesDivDoc = Jsoup.parse(raceClassesScript.data().trim());
		Element raceClassesTable = raceClassesDivDoc.getElementsByTag("table").first();
		if(raceClassesTable == null)
			return Collections.emptyList();
		Element tableBody = raceClassesTable.getElementsByTag("tbody").first();
		if(tableBody == null)
			return Collections.emptyList();
		Elements rows = tableBody.children();
		rows.remove(0);
		List<M2sClass> classes = new ArrayList<>();
		rows.forEach(element -> {
			M2sClass m2sClass = getClass(element);
			if(m2sClass != null)
				classes.add(m2sClass);
		});
		Element eventNameElement = raceOverviewPage.getElementsByClass("eventName").first();
		if(eventNameElement == null)
			return Collections.emptyList();
		String eventName = eventNameElement.child(0).text();
		classes.forEach(m2sClass -> m2sClass.setRaceEventName(eventName));
		return classes;
	}

	public List<RaceResultDto> getClassResults(String classUrl)
	{
		RestTemplate restTemplate = new RestTemplate();
		M2sClassResultsJsonDto classResultsJson = restTemplate.getForObject(classUrl, M2sClassResultsJsonDto.class);

		List<RaceResultDto> resultList = new ArrayList<>();
		if(classResultsJson.getResults().isEmpty())
		{
			classResultsJson.getEntries().forEach(m2sEntryResultJsonDto -> resultList.add(
					RaceResultDto.builder().raceGroup(classResultsJson.getClassName())
								 .skipper(m2sEntryResultJsonDto.getSkipperName())
								 .shipName(m2sEntryResultJsonDto.getShipName())
								 .shipClass(m2sEntryResultJsonDto.getShipClass()).build()));
		}
		else
		{
			classResultsJson.getResults().forEach(m2sScoreResultJsonDto -> {
				String subName = m2sScoreResultJsonDto.getName();
				m2sScoreResultJsonDto.getResult().getEntries().forEach(subEntry -> resultList.add(
						RaceResultDto.builder().raceGroup(subName).skipper(subEntry.getSkipperName())
									 .shipName(subEntry.getShipName()).shipClass(subEntry.getShipClass()).build()));
			});
		}

		return resultList;
	}

	private M2sClass getClass(Element row)
	{
		M2sClass raceClass = new M2sClass();
		raceClass.setClassName(row.child(0).text());
		Element resultUrlCell = row.child(2);
		if(resultUrlCell.children().isEmpty())
			return null;
		Element urlAnchor = resultUrlCell.child(0);
		raceClass.setClassUrl(urlAnchor.attribute("href").getValue());
		return raceClass;
	}
}
