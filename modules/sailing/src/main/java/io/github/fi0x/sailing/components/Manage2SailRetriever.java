package io.github.fi0x.sailing.components;

import io.github.fi0x.sailing.logic.dto.M2sClass;
import io.github.fi0x.sailing.logic.dto.M2sClassResultsJsonDto;
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
		if (raceClassesScript == null)
			return Collections.emptyList();
		if (raceClassesScript.childNodeSize() == 0)
			return Collections.emptyList();
		Element raceClassesDivDoc = Jsoup.parse(raceClassesScript.data().trim());
		Element raceClassesTable = raceClassesDivDoc.getElementsByTag("table").first();
		if (raceClassesTable == null)
			return Collections.emptyList();
		Element tableBody = raceClassesTable.getElementsByTag("tbody").first();
		if (tableBody == null)
			return Collections.emptyList();
		Elements rows = tableBody.children();
		rows.remove(0);
		List<M2sClass> classes = new ArrayList<>();
		rows.forEach(element -> {
			M2sClass m2sClass = getClass(element);
			if (m2sClass != null)
				classes.add(m2sClass);
		});
		Element eventNameElement = raceOverviewPage.getElementsByClass("eventName").first();
		if (eventNameElement == null)
			return Collections.emptyList();
		String eventName = eventNameElement.child(0).text();
		classes.forEach(m2sClass -> m2sClass.setRaceEventName(eventName));
		return classes;
	}

	public List<Object> getClassResults(String classUrl)
	{
		RestTemplate restTemplate = new RestTemplate();
		M2sClassResultsJsonDto classResultsJson = restTemplate.getForObject(classUrl, M2sClassResultsJsonDto.class);
		//TODO: Load class-results from m2s and return them

		return null;
	}

	private M2sClass getClass(Element row)
	{
		M2sClass raceClass = new M2sClass();
		raceClass.setClassName(row.child(0).text());
		Element resultUrlCell = row.child(2);
		if (resultUrlCell.children().isEmpty())
			return null;
		Element urlAnchor = resultUrlCell.child(0);
		raceClass.setClassUrl(urlAnchor.attribute("href").getValue());
		return raceClass;
	}
}
