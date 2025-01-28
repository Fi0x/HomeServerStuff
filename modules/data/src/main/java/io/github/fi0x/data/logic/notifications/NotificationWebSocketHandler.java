package io.github.fi0x.data.logic.notifications;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class NotificationWebSocketHandler extends TextWebSocketHandler
{
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception
	{
		//TODO: Use the method to send useful messages
		String notification = "Test Notification Text" + message.getPayload();
		session.sendMessage(new TextMessage(notification));
	}
}
