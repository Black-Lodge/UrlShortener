package urlshortener.blacklodge.HashGenerator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import urlshortener.blacklodge.Application;
import urlshortener.blacklodge.services.HashGeneratorService;

/**
 * Tests hash generator
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class HashGeneratorTest {

  @Autowired
  HashGeneratorService function;

  /**
   * Check that the hashing function is consistent
   */
  @Test
  public void checkHash() {
    Long list = function.hash("https://www.google.es");
    Long list2 = function.hash("https://www.google.es");
    assertEquals(list2.intValue(),list.intValue());
  }

}
