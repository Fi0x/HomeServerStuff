package io.github.fi0x.sailing.logic.converter;

import io.github.fi0x.sailing.db.entities.CertificateEntity;
import io.github.fi0x.sailing.logic.dto.CertificateType;
import io.github.fi0x.sailing.logic.dto.OrcOverviewXmlRowDto;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrcInformationToEntityMerger
{
	private static final String URL_BASE = "https://data.orc.org/public/WPub.dll/CC/";

	public CertificateEntity merge(OrcOverviewXmlRowDto rowDto, Document detailsPage)
	{
		CertificateEntity entity = toEntity(rowDto);

		try
		{
			Element body = detailsPage.getElementsByTag("html").first().getElementsByTag("body").first();
			Element scores = body.getElementsByClass("page").get(1).getElementById("country" + entity.getCountry());
			Element tripleNumbers = scores.getElementsByClass("countryScoring").first().getElementsByClass("p3group")
										  .first().getElementsByTag("table").first().getElementsByTag("tbody").first();

			addTripleNumbers(entity, tripleNumbers);
		} catch (NullPointerException e)
		{
			log.warn("Could not add scoring details to certificate '{}'", entity.getId());
			entity.setSingleNumber(1.0);
			entity.setTripleLongLow(1.0);
			entity.setTripleLongMid(1.0);
			entity.setTripleLongHigh(1.0);
			entity.setTripleUpDownLow(1.0);
			entity.setTripleUpDownMid(1.0);
			entity.setTripleUpDownHigh(1.0);
		}

		return entity;
	}

	private CertificateEntity toEntity(OrcOverviewXmlRowDto rowDto)
	{
		return CertificateEntity.builder().id(rowDto.getRefNo()).shipName(rowDto.getYachtName()).certificateType(
										CertificateType.valueOf(rowDto.getCertName().toUpperCase().replace(" ", "_")))
								.country(rowDto.getCountryId()).shipClass(rowDto.getShipClass())
								.url(URL_BASE + rowDto.getDxtID()).build();
	}

	private void addTripleNumbers(CertificateEntity entity, Element tripleNumbers)
	{
		entity.setSingleNumber(getValue(tripleNumbers.getElementsByAttributeValue("code", "OC").first()));
		entity.setTripleLongLow(getValue(tripleNumbers.getElementsByAttributeValue("code", "TNOL").first()));
		entity.setTripleLongMid(getValue(tripleNumbers.getElementsByAttributeValue("code", "TNOM").first()));
		entity.setTripleLongHigh(getValue(tripleNumbers.getElementsByAttributeValue("code", "TNOH").first()));
		entity.setTripleUpDownLow(getValue(tripleNumbers.getElementsByAttributeValue("code", "TNIL").first()));
		entity.setTripleUpDownMid(getValue(tripleNumbers.getElementsByAttributeValue("code", "TNIM").first()));
		entity.setTripleUpDownHigh(getValue(tripleNumbers.getElementsByAttributeValue("code", "TNIH").first()));
	}

	private Double getValue(Element dataElement)
	{
		if (dataElement == null)
			return 1.0;

		String text = dataElement.getElementsByTag("td").first().text();
		return Double.parseDouble(text);
	}
}
