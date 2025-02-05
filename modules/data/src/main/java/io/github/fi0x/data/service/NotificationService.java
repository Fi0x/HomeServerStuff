package io.github.fi0x.data.service;

import io.github.fi0x.data.logic.dto.ExpandedDataDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class NotificationService
{
	private static final List<SseEmitter> emitters = new ArrayList<>();

	public void addEmitter(SseEmitter emitter)
	{
		if (!emitters.contains(emitter))
			emitters.add(emitter);
	}

	public void removeEmitter(SseEmitter emitter)
	{
		emitters.remove(emitter);
	}

	public void notifyDataUpdate(ExpandedDataDto dataDto)
	{
		List<SseEmitter> deadEmitters = new ArrayList<>();
		emitters.forEach(emitter -> {
			try
			{
				//				TODO: Find out why this IOException gets printed in console
				emitter.send(SseEmitter.event().data(dataDto));
				log.info("sent data update to emitter");
			} catch (IOException e)
			{
				deadEmitters.add(emitter);
				log.info("lost connection to emitter");
			}
		});
		emitters.removeAll(deadEmitters);
	}


	@Scheduled(fixedRate = 5000)
	public void scheduledUpdate()
	{
		//TODO: Remove this scheduled event after testing
		notifyDataUpdate(new ExpandedDataDto("123", "Testsensor", System.currentTimeMillis(), Math.random()));
	}
}
