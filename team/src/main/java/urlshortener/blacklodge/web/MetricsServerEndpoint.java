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
import org.springframework.stereotype.Component;


@ServerEndpoint(value = "/globalinformation")
public class MetricsServerEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(MetricsServerEndpoint.class);
	CopyOnWriteArrayList<Session> listSessions = new CopyOnWriteArrayList<Session>();
	EventSuscriber es = new EventSuscriber();
	
	@OnOpen
	public void onOpen(Session session) {
		logger.info("Server Connected ... "+ session.getId());
		listSessions.add(session);
	}
	
	@OnMessage
	public void onMessage(String message, Session session) {
		logger.info("Server Message ... "+ session.getId());
	}
	
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		logger.info(String.format("Session %s closed because of %s",session.getId(),closeReason ));
		listSessions.remove(session);
	}
	
	@OnError
	public void onError(Session session, Throwable errorReason) {
		logger.error(String.format("Session %s closed because of %s", session.getId(), errorReason.getClass().getName()),
				errorReason);
	}
	
	
	@Component
	class EventSuscriber implements Runnable{
		private Thread thread;
		private volatile boolean someCondition = true;
		
		EventSuscriber(){
			this.thread = new Thread(this);
			thread.start();
		}
		
		@Override
		public void run() {
			while(someCondition) {
				try {
					Thread.sleep(2 *   // minutes to sleep
								60 *  // sconds to a minute
								1000  // milliseconds to a second
								);
					sendMetrics();
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
			}
			
		}
		void sendMetrics(){
			for (Iterator<Session> iterator = listSessions.iterator(); iterator.hasNext();) {
				Session session = (Session) iterator.next();
				session.getAsyncRemote().sendText("Han pasado dos minutos.");
				
			}
		}
	}
	
}
