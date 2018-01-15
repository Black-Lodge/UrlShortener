package urlshortener.blacklodge.accessor;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Class that represnets a FeignClient for the checkWords API
 */
@FeignClient(name="CheckWordsAccesor",url="http://www.wdylike.appspot.com",
        configuration = checkWordsConfiguration.class,fallback = checkWordsError.class)
public interface CheckWordsAccesor {

  /**
   * Get check method
   * @param word Sentence to be check
   * @return True if contains an offensive word, false otherwise
   */
  @RequestMapping(method = RequestMethod.GET, path="/?q={word}",produces = "text/plain")
  Boolean check(@PathVariable("word") String word);

  static Boolean checkFailed(String q) { return true;}

}
