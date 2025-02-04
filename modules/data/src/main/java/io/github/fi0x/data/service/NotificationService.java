package io.github.fi0x.data.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

	@Async
	@Scheduled(fixedRate = 5000)
	public void notifyAllEmitters()
	{
		List<SseEmitter> deadEmitters = new ArrayList<>();
		emitters.forEach(emitter -> {
			try
			{
				emitter.send(SseEmitter.event().data("This is my test message"));
			} catch (IOException e)
			{
				deadEmitters.add(emitter);
			}
		});
		emitters.removeAll(deadEmitters);
	}
}
