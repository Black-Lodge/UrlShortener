package urlshortener.blacklodge.services;

public interface SafeBrowsing {

    boolean lookupURL(String url);

}