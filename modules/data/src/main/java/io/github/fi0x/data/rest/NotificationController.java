package io.github.fi0x.data.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController
{
	@PostMapping("/send")
	public ResponseEntity<String> sendNotification(@RequestBody String message)
	{
		//TODO: Notify all other clients
		return ResponseEntity.ok("Notification sent");
	}
}
