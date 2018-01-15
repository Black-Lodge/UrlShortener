package urlshortener.blacklodge.model;

import urlshortener.common.domain.ShortURL;

/**
 * Interface for url shorteners
 */
public interface UrlShortenerModel {

  /**
   * Shortens a given url
   * @param url URL to be shortened
   * @param sponsor Ads
   * @param owner unique id of the owner
   * @param ip IP from the request
   * @return The shortened URL
   */
  ShortURL shorten(String url, String sponsor, String owner, String ip);

}
