package urlshortener.blacklodge.metrics;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;


import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import urlshortener.blacklodge.web.MetricsServerEndpoint;


/**
 * Class that every 2 minutes get the info from the 
 * system and send the new information to the clients
 * 
 * @author Daniel
 *
 */
public class EventSuscriber implements Runnable{
	private static final Logger logger = LoggerFactory.getLogger(MetricsServerEndpoint.class);
	private Thread thread;
	private volatile boolean someCondition = true;
	CopyOnWriteArrayList<Session> listSessions = new CopyOnWriteArrayList<Session>();
	GlobalInformation gi;
	
	
	public EventSuscriber(CopyOnWriteArrayList<Session> listSessions ){
		this.listSessions = listSessions;
		this.thread = new Thread(this);
		thread.start();
	}
	
	/**
	 * Add the client with Session s in the list of Metrics clients.
	 * @param s
	 */
	public void addSession(Session s) {
		listSessions.add(s);
	}
	
	/**
	 * Removes the client with Session s from the publisher
	 * @param s
	 */
	public void removeSession(Session s) {
		listSessions.remove(s);
	}
	
	@Override
	public void run() {
		while(someCondition) {
			try {
				updateMetrics();
				sendMetrics();
				Thread.sleep(//2 * // minutes to sleep
							20 *  // sconds to a minute
							1000  // milliseconds to a second
							);
				
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
		
	}
	
	/**
	 *  Updates the message with the new metrics.
	 *  
	 */
	private void updateMetrics() {
		gi = fakeStats();
	}
	
	/**
	 * Method that returns fake stats of the system.
	 * @return
	 */
	private GlobalInformation fakeStats() {
		Random ran = new Random();
		int time =  ran.nextInt(100);
		int users = ran.nextInt(5);
		int uris = ran.nextInt(200);
		int clicks = ran.nextInt();
		int lastRedirection = ran.nextInt();
		int lastPetition = ran.nextInt();
		int used = ran.nextInt();
		int avaible = ran.nextInt();
		return new GlobalInformation(time, users, uris, clicks, lastRedirection, lastPetition, used, avaible);
	}
	/**
	 * Send the metrics to the clients
	 */
	void sendMetrics(){
		logger.info("Sending metrics to clients ("+listSessions.size()+")");
		for (Iterator<Session> iterator = listSessions.iterator(); iterator.hasNext();) {
			Session session = (Session) iterator.next();
			session.getAsyncRemote().sendText(gi.getJSON());
			
		}
	}
}