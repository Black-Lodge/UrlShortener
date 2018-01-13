package urlshortener.blacklodge.services;

/**
 * Interface for a service that check if a sentence contains offensive words
 */
public interface CheckWordsService {
    /**
     * Checks if the given sentence contains offensive words
     * @param query Sentence to check
     * @return True if it contains offensive words, false otherwise
     */
    boolean check(String query);
}
