package urlshortener.blacklodge.web;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import urlshortener.blacklodge.metrics.EventSuscriber;


@ServerEndpoint(value = "/globalinformation")
public class MetricsServerEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(MetricsServerEndpoint.class);

	@Autowired
	static EventSuscriber es = null;
	
	@OnOpen
	public void onOpen(Session session) {
		if (es == null) {
			es = new EventSuscriber(new CopyOnWriteArrayList<Session>());
		}
		logger.info("Server Connected ... "+ session.getId());
		es.addSession(session);
	}
	
	@OnMessage
	public void onMessage(String message, Session session) {
		logger.info("Server Message ... "+ session.getId());
	}
	
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		logger.info(String.format("Session %s closed because of %s",session.getId(),closeReason ));
		es.removeSession(session);
	}
	
	@OnError
	public void onError(Session session, Throwable errorReason) {
		logger.error(String.format("Session %s closed because of %s", session.getId(), errorReason.getClass().getName()),
				errorReason);
	}
	
	
}
