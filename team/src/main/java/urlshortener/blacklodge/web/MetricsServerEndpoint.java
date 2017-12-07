package urlshortener.blacklodge.web;


import java.util.concurrent.CopyOnWriteArrayList;


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


import urlshortener.blacklodge.metrics.EventSubscriber;

/**
 * Websockets Enpoint for Metrics
 * 
 * This endpoint catch the 
 * new websockets clients to the Metrics Service.
 * Adding them to a List of EventSuscriber Class that 
 * periodically send them the new metrics in JSON.  
 * 
 * @author Daniel
 *
 */
@ServerEndpoint(value = "/globalinformation")
public class MetricsServerEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(MetricsServerEndpoint.class);


	static EventSubscriber es = new EventSubscriber();
	
	
	
	
	@OnOpen
	public void onOpen(Session session) {
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
