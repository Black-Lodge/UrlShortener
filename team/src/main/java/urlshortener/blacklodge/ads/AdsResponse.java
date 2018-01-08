package urlshortener.blacklodge.ads;

public class AdsResponse {
	
	int countdown;
	String url;
	
	public AdsResponse(int countdown,String url) {
		this.countdown = countdown;
		this.url = url;
	}

	public int getCountdown() {
		return countdown;
	}

	public void setCountdown(int countdown) {
		this.countdown = countdown;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
