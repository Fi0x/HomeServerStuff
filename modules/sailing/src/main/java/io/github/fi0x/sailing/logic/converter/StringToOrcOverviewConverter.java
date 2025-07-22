package io.github.fi0x.sailing.logic.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.github.fi0x.sailing.logic.dto.orc.OrcOverviewXmlDto;
import io.github.fi0x.sailing.logic.dto.orc.OrcOverviewXmlRowDto;
import org.springframework.stereotype.Component;

@Component
public class StringToOrcOverviewConverter
{
	public OrcOverviewXmlRowDto convert(String inputXml)
	{
		XmlMapper mapper = new XmlMapper();
		try
		{
			OrcOverviewXmlDto overviewDto = mapper.readValue(inputXml, OrcOverviewXmlDto.class);
			return overviewDto.getDATA().getROW();
		} catch (JsonProcessingException | NullPointerException e)
		{
			return null;
		}
	}
}
