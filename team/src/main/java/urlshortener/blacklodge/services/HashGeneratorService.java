package urlshortener.blacklodge.services;

/**
 * Interface for Hashing services
 */
public interface HashGeneratorService {

  /**
   * Hashes the given URL
   * @param url URL to hash
   * @return Long hash based on the given url
   */
  Long hash(String url);

}
