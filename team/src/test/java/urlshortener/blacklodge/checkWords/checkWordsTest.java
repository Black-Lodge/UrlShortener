package urlshortener.blacklodge.checkWords;

import static org.junit.Assert.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import urlshortener.blacklodge.services.CheckWordsService;

/**
 * Tests the check words service
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= RANDOM_PORT)
public class checkWordsTest {

  @Autowired
  CheckWordsService checkWordsService;

  /**
   * Checks that given a url that contains an offensive word, detects that it is offensive
   */
  @Test
  public void checkBadURL() {
    assertEquals(true,checkWordsService.check("https://mydomain.com/fuck"));
  }

  /**
   * Checks that given a non offensive url, no offensive words are detected
   */
  @Test
  public void checkSafeURL() {
    assertEquals(false,checkWordsService.check("https://mydomain.com/hi/test"));
  }

  /**
   * Checks that given a non offensive word, is detected as so
   */
  @Test
  public void checkRealSafeWord() {
    assertEquals(false,checkWordsService.check("you"));
  }

  /**
   * Checks that given an offensive word, is detected as so
   */
  @Test
  public void checkRealWrongWord() {
    assertEquals(true,checkWordsService.check("fuck"));
  }

}
