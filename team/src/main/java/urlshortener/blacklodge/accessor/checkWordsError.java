package urlshortener.blacklodge.accessor;

import org.springframework.stereotype.Component;

/**
 * Class that implements how it should act feign in cased of a failed petition
 */
@Component
public class checkWordsError implements CheckWordsAccesor {

  @Override
  public Boolean check(String word) {
    return true;
  }
  
}
