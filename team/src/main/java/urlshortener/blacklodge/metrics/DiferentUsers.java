package urlshortener.blacklodge.metrics;

import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * Class that represents the different users of the system
 */
@Component
public class DiferentUsers {

  private static Set<String> ips;

  /**
   * Default constructor
   */
  public DiferentUsers() {
    ips = new HashSet<String>() ;
  }

  /**
   * Get the number of different users
   * @return number of users
   */
  public int getNumber() {
    return ips.size();
  }

  /**
   * Add a user in case it does not exists in the system
   * @param word The user to be added
   */
  public void add(String word) {
    ips.add(word);
  }

}
