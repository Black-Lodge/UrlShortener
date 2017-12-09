package urlshortener.blacklodge.infrastructure;

public interface SafeBrowsing {

    boolean lookupURL(String url);

}