package urlshortener.blacklodge.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Class that represents a service that checks if a URL is available
 */
@Service
public class CheckAvailabilityServiceImpl implements CheckAvailabilityService {

  private final static Logger logger = LoggerFactory.getLogger(CheckAvailabilityServiceImpl.class);

  /**
   * Check if a get petition to the url returns 200
   * @param url URL to check
   * @return True if GET petition returns 200, false otherwise
   */
  @Override
  public boolean check (String url) {
    try {
      HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
      //logger.info(connection.getURL().toString());
      connection.setConnectTimeout(10000);
      connection.setRequestMethod("GET");
      int responseCode = connection.getResponseCode();
      return 200 == responseCode;
    } catch (IOException e) {
      logger.error("Connection failed for URL {}. Error: {}.", url, e.getMessage());
      return false;
    }
  }

}
