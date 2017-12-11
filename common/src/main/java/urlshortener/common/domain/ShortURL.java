package urlshortener.common.domain;

import java.net.URI;
import java.sql.Date;

public class ShortURL {

	private String hash;
	private Integer hash1;
	private Integer hash2;
	private String target;
	private URI uri;
	private String sponsor;
	private Date created;
	private String owner;
	private Integer mode;
	private Boolean safe;
	private String ip;
	private String country;
	private URI image;

	public ShortURL(String hash, Integer hash1, Integer hash2, String target, URI uri, String sponsor,
			Date created, String owner, Integer mode, Boolean safe, String ip,
			String country, URI image) {
		this.hash = hash;
		this.hash1 = hash1;
		this.hash2 = hash2;
		this.target = target;
		this.uri = uri;
		this.sponsor = sponsor;
		this.created = created;
		this.owner = owner;
		this.mode = mode;
		this.safe = safe;
		this.ip = ip;
		this.country = country;
		this.image = image;
	}

	public ShortURL() {
	}

	public String getHash() {
		return hash;
	}

	public Integer getHash1() { return hash1; }

	public Integer getHash2() { return hash2; }

	public String getTarget() {
		return target;
	}

	public URI getUri() {
		return uri;
	}

	public Date getCreated() {
		return created;
	}

	public String getOwner() {
		return owner;
	}

	public Integer getMode() {
		return mode;
	}

	public String getSponsor() {
		return sponsor;
	}

	public Boolean getSafe() {
		return safe;
	}

	public String getIP() {
		return ip;
	}

	public String getCountry() {
		return country;
	}

	public URI getImage() { return image; }
}
