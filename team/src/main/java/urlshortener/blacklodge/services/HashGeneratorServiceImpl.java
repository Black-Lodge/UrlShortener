package urlshortener.blacklodge.services;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;

/**
 * Hashing service that uses the murmur3_128 algorithm to hash
 */
@Service
public class HashGeneratorServiceImpl implements HashGeneratorService {
  /**
   * Hash function to use
   */
  private static HashFunction function = Hashing.murmur3_128();

  /**
   * Hashes the given function
   * @param url URL to hash
   * @return A long number that it is the hash of the given URL
   */
  public Long hash (String url) {
    return function.hashString(url, StandardCharsets.UTF_8).asLong();
        /*int div = (int)pow(10,String.valueOf(num).length()/2);
        return new LinkedList<>(Arrays.asList((int)num, (int)(num/div),(int)(num%div)));*/
  }

}
