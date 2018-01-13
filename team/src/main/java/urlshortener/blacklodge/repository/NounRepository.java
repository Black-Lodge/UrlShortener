package urlshortener.blacklodge.repository;

/**
 * Interface for the noun repositories
 */
public interface NounRepository {
    /**
     * Get a noun based on its id
     * @param id id of the noun to be searched
     * @return The noun if it was found
     */
    String get(int id);

    /**
     * Get the number of nouns on the database
     * @return Number of nouns
     */
    Integer number();
}
