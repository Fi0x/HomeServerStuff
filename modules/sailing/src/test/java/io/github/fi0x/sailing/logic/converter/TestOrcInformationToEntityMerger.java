package io.github.fi0x.sailing.logic.converter;

import io.github.fi0x.sailing.db.entities.CertificateEntity;
import io.github.fi0x.sailing.logic.dto.orc.CertificateType;
import io.github.fi0x.sailing.logic.dto.orc.OrcOverviewXmlRowDto;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
public class TestOrcInformationToEntityMerger
{
	private static final String BASE_URL = "something.com";
	private static final String ORC_BASE_URL = "https://data.orc.org/public/WPub.dll/CC/";
	private static final String CERT_ID = "AKC3D0";
	private static final String SHIP_NAME = "MINNE MUUS";
	private static final CertificateType CERT_TYPE = CertificateType.CLUB;
	private static final String CERT_NAME = CERT_TYPE.name();
	private static final String COUNTRY = "GER";
	private static final String SHIP_CLASS = "Esse 850";
	private static final String URL_ID = "23091234";
	private static final Double SINGLE_NUMBER = 234.9;
	private static final Double TRI_LONG_LOW = 23.34;
	private static final Double TRI_LONG_MID = 485.34;
	private static final Double TRI_LONG_HIGH = 9856.093;
	private static final Double TRI_UP_DOWN_LOW = 923.23;
	private static final Double TRI_UP_DOWN_MID = 123.456;
	private static final Double TRI_UP_DOWN_HIGH = 545.45;

	@InjectMocks
	private OrcInformationToEntityMerger merger;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void test_merge_success()
	{
		CertificateEntity expected = getExpectedCertEntity();
		addNumbers(expected);

		Document document = new Document(BASE_URL);
		Element html = addElement(document, "html", Collections.emptyList(), null, Collections.emptyMap());
		Element body = addElement(html, "body", Collections.emptyList(), null, Collections.emptyMap());
		addElement(body, "div", List.of("page"), null, Collections.emptyMap());
		Element page1 = addElement(body, "div", List.of("page"), null, Collections.emptyMap());
		Element country =
				addElement(page1, "div", Collections.emptyList(), "country" + COUNTRY, Collections.emptyMap());
		Element scoring = new Element("div");
		scoring.addClass("countryScoring");
		country.appendChild(scoring);
		Element p3group = new Element("div");
		p3group.addClass("p3group");
		scoring.appendChild(p3group);
		Element table = new Element("table");
		p3group.appendChild(table);
		Element tbody = new Element("tbody");
		table.appendChild(tbody);
		addNumberElements(tbody);
		OrcOverviewXmlRowDto rowDto =
				OrcOverviewXmlRowDto.builder().refNo(CERT_ID).YachtName(SHIP_NAME).CertName(CERT_NAME)
									.CountryId(COUNTRY).shipClass(SHIP_CLASS).dxtID(URL_ID).build();

		Assertions.assertEquals(expected, merger.merge(rowDto, document));
	}

	@Test
	void test_merge_NPEFromMissingTag()
	{
		CertificateEntity expected = getExpectedCertEntity();
		addNumbers(expected, 1.0);

		Document document = new Document(BASE_URL);
		OrcOverviewXmlRowDto rowDto =
				OrcOverviewXmlRowDto.builder().refNo(CERT_ID).YachtName(SHIP_NAME).CertName(CERT_NAME)
									.CountryId(COUNTRY).shipClass(SHIP_CLASS).dxtID(URL_ID).build();

		Assertions.assertEquals(expected, merger.merge(rowDto, document));
	}

	@Test
	void test_merge_noValues()
	{
		CertificateEntity expected = getExpectedCertEntity();
		addNumbers(expected, 1.0);

		Document document = new Document(BASE_URL);
		Element html = addElement(document, "html", Collections.emptyList(), null, Collections.emptyMap());
		Element body = addElement(html, "body", Collections.emptyList(), null, Collections.emptyMap());
		addElement(body, "div", List.of("page"), null, Collections.emptyMap());
		Element page1 = addElement(body, "div", List.of("page"), null, Collections.emptyMap());
		Element country =
				addElement(page1, "div", Collections.emptyList(), "country" + COUNTRY, Collections.emptyMap());
		Element scoring = new Element("div");
		scoring.addClass("countryScoring");
		country.appendChild(scoring);
		Element p3group = new Element("div");
		p3group.addClass("p3group");
		scoring.appendChild(p3group);
		Element table = new Element("table");
		p3group.appendChild(table);
		Element tbody = new Element("tbody");
		table.appendChild(tbody);
		OrcOverviewXmlRowDto rowDto =
				OrcOverviewXmlRowDto.builder().refNo(CERT_ID).YachtName(SHIP_NAME).CertName(CERT_NAME)
									.CountryId(COUNTRY).shipClass(SHIP_CLASS).dxtID(URL_ID).build();

		Assertions.assertEquals(expected, merger.merge(rowDto, document));
	}

	private CertificateEntity getExpectedCertEntity()
	{
		return CertificateEntity.builder().id(CERT_ID).shipName(SHIP_NAME).certificateType(CERT_TYPE).country(COUNTRY)
								.shipClass(SHIP_CLASS).url(ORC_BASE_URL + URL_ID).build();
	}

	private void addNumbers(CertificateEntity entity)
	{
		entity.setSingleNumber(SINGLE_NUMBER);
		entity.setTripleLongLow(TRI_LONG_LOW);
		entity.setTripleLongMid(TRI_LONG_MID);
		entity.setTripleLongHigh(TRI_LONG_HIGH);
		entity.setTripleUpDownLow(TRI_UP_DOWN_LOW);
		entity.setTripleUpDownMid(TRI_UP_DOWN_MID);
		entity.setTripleUpDownHigh(TRI_UP_DOWN_HIGH);
	}

	private void addNumbers(CertificateEntity entity, Double valueForAll)
	{
		entity.setSingleNumber(valueForAll);
		entity.setTripleLongLow(valueForAll);
		entity.setTripleLongMid(valueForAll);
		entity.setTripleLongHigh(valueForAll);
		entity.setTripleUpDownLow(valueForAll);
		entity.setTripleUpDownMid(valueForAll);
		entity.setTripleUpDownHigh(valueForAll);
	}

	private void addNumberElements(Element parent)
	{
		addNumberValueElement(parent, SINGLE_NUMBER, "OC");
		addNumberValueElement(parent, TRI_LONG_LOW, "TNOL");
		addNumberValueElement(parent, TRI_LONG_MID, "TNOM");
		addNumberValueElement(parent, TRI_LONG_HIGH, "TNOH");
		addNumberValueElement(parent, TRI_UP_DOWN_LOW, "TNIL");
		addNumberValueElement(parent, TRI_UP_DOWN_MID, "TNIM");
		addNumberValueElement(parent, TRI_UP_DOWN_HIGH, "TNIH");
	}

	private void addNumberValueElement(Element parent, Double value, String code)
	{
		Element row = addElement(parent, "tr", Collections.emptyList(), null, new HashMap<>()
		{{
			put("code", code);
		}});
		Element cell = addElement(row, "td", Collections.emptyList(), null, Collections.emptyMap());
		cell.text(value.toString());
	}

	private Element addElement(Element parent, String tag, List<String> classes, String id,
							   Map<String, String> attributes)
	{
		Element e = new Element(tag);

		for(String clazz : classes)
		{
			e.addClass(clazz);
		}
		if(id != null)
			e.id(id);
		for(Map.Entry<String, String> entry : attributes.entrySet())
		{
			e.attr(entry.getKey(), entry.getValue());
		}

		parent.appendChild(e);
		return e;
	}
}
