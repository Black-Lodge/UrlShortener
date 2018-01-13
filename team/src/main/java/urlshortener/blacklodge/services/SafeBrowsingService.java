package urlshortener.blacklodge.services;

/**
 * Interface for services that check the safety of some URLs
 */
public interface SafeBrowsingService {
    /**
     * Check URL safety
     * @param url URL to check
     * @return Returns true if the URL is safe, false otherwise
     */
    boolean checkSafetyUrl(String url);
}
