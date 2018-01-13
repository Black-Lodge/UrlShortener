package urlshortener.blacklodge.services;

/**
 * Common interface for services that check that the URL provided is available
 */
public interface CheckAvailabilityService {
    /**
     * Check if the provided URL is available
     * @param url URL to check
     * @return True if the URL is available, false otherwise
     */
    boolean check (String url);
}
