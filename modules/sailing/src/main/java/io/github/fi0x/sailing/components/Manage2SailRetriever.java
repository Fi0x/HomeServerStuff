package io.github.fi0x.sailing.components;

import io.github.fi0x.sailing.logic.dto.M2sClass;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class Manage2SailRetriever
{
	public List<M2sClass> getRaceClasses(String raceOverviewUrl) throws IOException
	{
		//TODO: Get this to work
		Document raceOverviewPage = Jsoup.connect(raceOverviewUrl).get();
		Element raceClassesScript = raceOverviewPage.getElementById("classes");
		if (raceClassesScript == null)
			return Collections.emptyList();
		Element raceClassesDiv = raceClassesScript.getElementsByTag("div").first();
		if (raceClassesDiv == null)
			return Collections.emptyList();
		Element raceClassesTable = raceClassesDiv.getElementsByTag("table").first();
		if (raceClassesTable == null)
			return Collections.emptyList();
		Elements rows = raceClassesTable.children();
		rows.remove(0);
		List<M2sClass> classes = new ArrayList<>();
		rows.forEach(element -> classes.add(getClass(element)));
		String eventName = raceOverviewPage.getElementsByClass("eventName").first().child(0).text();
		classes.forEach(m2sClass -> m2sClass.setRaceEventName(eventName));
		return classes;
	}

	private M2sClass getClass(Element row)
	{
		M2sClass raceClass = new M2sClass();
		raceClass.setClassName(row.child(0).text());
		raceClass.setClassUrl(row.child(2).child(0).attribute("href").getValue());
		return raceClass;
	}
}
