package urlshortener.blacklodge.repository;

/**
 * Interface for the adj repositories
 */
public interface AdjRepository {

  /**
   * Get an adj based on its id
   * @param id id of the adj to be searched
   * @return The adj if it was found
   */
  String get(int id);

  /**
   * Get the number of adjs on the database
   * @return Number of adjs
   */
  Integer number();

}
