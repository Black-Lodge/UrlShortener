package urlshortener.blacklodge.scheduler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import urlshortener.blacklodge.Application;
import urlshortener.blacklodge.repository.ShortURLRepo;
import urlshortener.common.domain.ShortURL;

/**
 * Tests Safe Browsing Scheduled Task
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SafeBrowsingSchedulerTest {

  @Autowired
  SafeBrowsingScheduler safeBrowsingScheduler;

  @Autowired
  ShortURLRepo shortUrlRepository;

  /**
   * Checks that the safe browsing tasks actually detects if a URL state has changed
   * @throws URISyntaxException URI creation exception
   */
  @Test
  public void checkSafeFalseToTrue() throws URISyntaxException {
    ShortURL su = new ShortURL("hash", "http://google.com", new URI("http://localhost:8080/such_1_so_2"), "q", new Date(System.currentTimeMillis()) ,
            "test", HttpStatus.TEMPORARY_REDIRECT.value(), false, "test", null, "test");
    assertFalse(su.getSafe().booleanValue());
    shortUrlRepository.save(su);

    safeBrowsingScheduler.checkSafe();
    //o Thread.sleep(60000);

    ShortURL result = shortUrlRepository.findByKey("hash");
    assertTrue(result.getSafe().booleanValue());
    shortUrlRepository.delete("hash");
  }

}
