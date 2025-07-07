package io.github.fi0x.data.service;

import io.github.fi0x.data.logic.dto.ExpandedDataDto;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class NotificationService
{
	private static final List<AddressedEmitter> emitters = new ArrayList<>();

	public void addEmitter(SseEmitter emitter, String sensorAddress, String sensorName)
	{
		AddressedEmitter newEmitter = new AddressedEmitter(emitter, sensorAddress, sensorName);

		if(!emitters.contains(newEmitter))
			emitters.add(newEmitter);
	}

	public void removeEmitter(SseEmitter emitter, String sensorAddress, String sensorName)
	{
		emitters.remove(new AddressedEmitter(emitter, sensorAddress, sensorName));
	}

	public void notifyDataUpdate(ExpandedDataDto dataDto)
	{
		List<AddressedEmitter> deadEmitters = new ArrayList<>();
		for(AddressedEmitter emitter : emitters)
		{
			try
			{
				if(emitter.sensorAddress != null && !emitter.sensorAddress.equals(dataDto.getAddress()))
					continue;
				if(emitter.sensorName != null && !emitter.sensorName.equals(dataDto.getSensorName()))
					continue;
				//				TODO: Find out why this IOException gets printed in console
				emitter.emitter.send(SseEmitter.event().data(dataDto));
			} catch(IOException e)
			{
				deadEmitters.add(emitter);
				log.info("lost connection to emitter");
			}
		}
		emitters.removeAll(deadEmitters);
	}

	@Builder
	private static class AddressedEmitter
	{
		private SseEmitter emitter;
		private String sensorAddress;
		private String sensorName;

		@Override
		public boolean equals(Object o)
		{
			if(this == o)
				return true;
			if(o == null || getClass() != o.getClass())
				return false;
			AddressedEmitter other = (AddressedEmitter) o;
			return Objects.equals(emitter, other.emitter) && Objects.equals(sensorAddress,
																			other.sensorAddress) && Objects.equals(
					sensorName, other.sensorName);
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(emitter, sensorAddress, sensorName);
		}
	}
}
