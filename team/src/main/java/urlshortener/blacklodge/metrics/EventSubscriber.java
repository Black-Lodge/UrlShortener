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
public class EventSubscriber implements Runnable{
	private static final Logger logger = LoggerFactory.getLogger(MetricsServerEndpoint.class);
	private Thread thread;
	private volatile boolean someCondition = true;
	CopyOnWriteArrayList<Session> listSessions ;
	GlobalInformation gi;
	int seconds = 120;
	
	public EventSubscriber(){
		this.listSessions = new CopyOnWriteArrayList<Session>();
		this.thread = new Thread(this);
		thread.start();
		logger.info("EventSubscriber running.");
	}
	public EventSubscriber(int time){
		this.listSessions = new CopyOnWriteArrayList<Session>();
		this.thread = new Thread(this);
		thread.start();
		logger.info("EventSubscriber running.");
		seconds = time;
	}
	/**
	 * Add the client with Session s in the list of Metrics clients.
	 * @param s
	 */
	public void addSession(Session s) {
		logger.info("EventSubscriber added Session.");
		listSessions.add(s);
	}
	
	/**
	 * Removes the client with Session s from the publisher
	 * @param s
	 */
	public void removeSession(Session s) {
		logger.info("EventSubscriber removed Session.");
		listSessions.remove(s);
	}
	
	@Override
	public void run() {
		logger.info("Thread run of EventSubscriber running.");
		while(someCondition) {
			try {
				logger.info("Clients connected: "+listSessions.size());
				if (listSessions.size() >0) {
					updateMetrics();
					sendMetrics();
				}
				
				// Tiempo de espera antes de volver a enviar metricas.
				Thread.sleep(seconds *  // seconds to a minute
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
		int users;
		try {
			users = ((Double) result.get("gauge.users")).intValue();
		} catch (JSONException e) {
			logger.error("Can't read metrics.users");
			users= 0;
		}
		int lastPetition;
		try {
			lastPetition = ((Double) result.get("gauge.lastPetition")).intValue();
		} catch (JSONException e) {
			logger.error("Can't read metrics.lastPetition");
			lastPetition= 0;
		}
		int lastRedirection;
		try {
			lastRedirection = ((Double) result.get("gauge.lastRedirection")).intValue();
		} catch (JSONException e) {
			logger.error("Can't read metrics.lastRedirection");
			lastRedirection= 0;
		}
	    return new GlobalInformation(time,users,uris,clicks,lastRedirection,lastPetition,used,available);
	    
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