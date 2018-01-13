package urlshortener.blacklodge.metrics;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.client.RestTemplate;





/**
 * Class that every 2 minutes get the info from the 
 * system and send the new information to the clients
 * 
 * @author Daniel
 *
 */
public class InfoCollector {
	private static final Logger logger = LoggerFactory.getLogger(InfoCollector.class);
	
	/**
	 * Method that returns fake stats of the system.
	 * @return fake stats
	 */
	public GlobalInformation fakeStats() {

		int time =  100;
		int users = 5;
		int uris = 2;
		int clicks = 1000;
		int lastRedirection = 10;
		int lastPetition = 10;
		int used = 100;
		int available = 100;
		return new GlobalInformation(time, users, uris, clicks, lastRedirection, lastPetition, used, available);
	}
	/**
	 * Retrieves the real stats of the system
	 * from the Actuator service
	 * 
	 * @return Real stats
	 * @throws JSONException
	 */
	public GlobalInformation realStats() {

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
}