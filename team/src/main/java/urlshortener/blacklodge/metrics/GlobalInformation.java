package urlshortener.blacklodge.metrics;


/**
 * Simple class that contains 
 * the data that needs to be send to 
 * the client in the metrics.
 * 
 * @author Daniel
 *
 */
public class GlobalInformation {

	private int time;
	private int users;
	private int uris;
	private int clicks;
	private int lastRedirection;
	private int lastPetition;
	private int used;
	private int avaible;
	
	
	public GlobalInformation(int time, int users, int uris, int clicks, int lastRedirection, int lastPetition, int used,
			int avaible) {
		
		this.time = time;
		this.users = users;
		this.uris = uris;
		this.clicks = clicks;
		this.lastRedirection = lastRedirection;
		this.lastPetition = lastPetition;
		this.used = used;
		this.avaible = avaible;
	}
	
	
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getUsers() {
		return users;
	}
	public void setUsers(int users) {
		this.users = users;
	}
	public int getUris() {
		return uris;
	}
	public void setUris(int uris) {
		this.uris = uris;
	}
	public int getClicks() {
		return clicks;
	}
	public void setClicks(int clicks) {
		this.clicks = clicks;
	}
	public int getLastRedirection() {
		return lastRedirection;
	}
	public void setLastRedirection(int lastRedirection) {
		this.lastRedirection = lastRedirection;
	}
	public int getLastPetition() {
		return lastPetition;
	}
	public void setLastPetition(int lastPetition) {
		this.lastPetition = lastPetition;
	}
	public int getUsed() {
		return used;
	}
	public void setUsed(int used) {
		this.used = used;
	}
	public int getAvaible() {
		return avaible;
	}
	public void setAvaible(int avaible) {
		this.avaible = avaible;
	}
	public String getJSON() {
		String json =
				"{"
					+"'time':"+getTime()
					+",'users':"+getUsers()
					+",'uris':"+getUris()
					+",'clicks':"+getClicks()
					+",'lastRedirection':"+getLastRedirection()
					+",'lastPetition':"+getLastPetition()
					+",'used':"+getUsed()
					+",'avaible':"+getAvaible()
					
				 +"}"
				;
		
		return json;
	}
}
