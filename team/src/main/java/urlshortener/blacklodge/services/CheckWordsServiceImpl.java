package urlshortener.blacklodge.services;

import org.glassfish.grizzly.Grizzly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import urlshortener.blacklodge.accessor.CheckWordsAccesor;

import java.util.logging.Logger;

/**
 * Class that represents a service that check if a sentence contains offensive words against the wdylike API
 */
@Service
public class CheckWordsServiceImpl implements CheckWordsService {
  private static final String URL = "http://www.wdylike.appspot.com/?q=";
  private static final Logger logger = Grizzly.logger(CheckWordsServiceImpl.class);
  /**
   * FeignClient used for queries
   */
  @Autowired
  private CheckWordsAccesor accesor;

  /**
   * It checks against the wdylike API the provided sentence
   * @param query Sentence to check
   * @return True if sentence contains offensive words, false otherwise
   */
  public boolean check(String query) {
    Boolean a = accesor.check(query);
    return a;
  }

}
