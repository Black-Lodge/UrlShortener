package urlshortener.blacklodge.metrics;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;


import javax.websocket.Session;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.client.RestTemplate;


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
				if (listSessions.size() >0) {
					updateMetrics();
					sendMetrics();
				}
				
				// Tiempo de espera antes de volver a enviar metricas.
				Thread.sleep(//2 * // minutes to sleep
							1 *  // sconds to a minute
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
		
			gi = realStats();
	
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
		int clicks = ran.nextInt(1000);
		int lastRedirection = ran.nextInt(10);
		int lastPetition = ran.nextInt(10);
		int used = ran.nextInt(100);			
		int avaible = ran.nextInt(100);			
		return new GlobalInformation(time, users, uris, clicks, lastRedirection, lastPetition, used, avaible);
	}
	/**
	 * Retrieves the real stats of the system
	 * from the Actuator service
	 * 
	 * @return
	 * @throws JSONException
	 */
	private GlobalInformation realStats() {

	    final String uri = "http://localhost:8080/metrics/";

	    RestTemplate restTemplate = new RestTemplate();
	    String str = restTemplate.getForObject(uri, String.class);
	    
	    JSONObject result;
		try {
			result = new JSONObject(str);
		} catch (JSONException e) {
			logger.error("Can't read metrics.");
			return new GlobalInformation(0,0,0,0,0,0,0,0);
		}
	    
	    int time;
		try {
			time = (Integer) result.get("uptime");
		} catch (JSONException e) {
			logger.error("Can't read metrics.time");
			time= 0;
		}
	    int available;
		try {
			available = (Integer) result.get("mem.free");
		} catch (JSONException e) {
			logger.error("Can't read metrics.available");
			available= 0;
		}
	    int used;
		try {
			used = (Integer) result.get("mem") - available;
		} catch (JSONException e) {
			logger.error("Can't read metrics.used");
			used= 0;
		}
	    int clicks;
		try {
			clicks = ((Double) result.get("gauge.clicks")).intValue();
		} catch (JSONException e) {
			logger.error("Can't read metrics.clicks");
			clicks= 0;
		}
	    int uris;
		try {
			uris = ((Double) result.get("gauge.uris")).intValue();
		} catch (JSONException e) {
			logger.error("Can't read metrics.uris");
			uris= 0;
		}
	    return new GlobalInformation(time,0,uris,clicks,0,0,used,available);
	    
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