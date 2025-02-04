package io.github.fi0x.data.rest;

import io.github.fi0x.data.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@AllArgsConstructor
public class NotificationController
{
	private final NotificationService service;

	@GetMapping("/notification")
	public ResponseEntity<SseEmitter> addEmitter()
	{
		log.info("Adding emitter");
		SseEmitter emitter = new SseEmitter();
		service.addEmitter(emitter);
		service.notifyAllEmitters();
		emitter.onCompletion(() -> service.removeEmitter(emitter));
		emitter.onTimeout(() -> service.removeEmitter(emitter));
		return new ResponseEntity<>(emitter, HttpStatus.OK);
	}

	@PostMapping("/send")
	public ResponseEntity<String> sendNotification(@RequestBody String message)
	{
		//TODO: Notify all other clients
		return ResponseEntity.ok("Notification sent");
	}
}
