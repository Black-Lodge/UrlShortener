package urlshortener.blacklodge.metrics;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;


import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import urlshortener.blacklodge.web.MetricsServerEndpoint;



public class EventSuscriber implements Runnable{
	private static final Logger logger = LoggerFactory.getLogger(MetricsServerEndpoint.class);
	private Thread thread;
	private volatile boolean someCondition = true;
	
	CopyOnWriteArrayList<Session> listSessions = new CopyOnWriteArrayList<Session>();
	
	public EventSuscriber(CopyOnWriteArrayList<Session> listSessions ){
		this.listSessions = listSessions;
		this.thread = new Thread(this);
		thread.start();
	}
	
	
	public void addSession(Session s) {
		listSessions.add(s);
	}
	public void removeSession(Session s) {
		listSessions.remove(s);
	}
	
	@Override
	public void run() {
		while(someCondition) {
			try {
				Thread.sleep(//2 * // minutes to sleep
							20 *  // sconds to a minute
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
		logger.info("Sending metrics to clients ("+listSessions.size()+")");
		for (Iterator<Session> iterator = listSessions.iterator(); iterator.hasNext();) {
			Session session = (Session) iterator.next();
			session.getAsyncRemote().sendText("Han pasado dos minutos.");
			
		}
		
	}
}