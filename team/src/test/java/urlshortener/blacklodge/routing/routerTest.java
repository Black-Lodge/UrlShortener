package urlshortener.blacklodge.routing;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.camel.ProducerTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import urlshortener.blacklodge.Application;
import urlshortener.blacklodge.repository.ShortURLRepo;


/**
 * Tests Apache Camel Route
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class routerTest {

  @Autowired
  ProducerTemplate producertemplate;

  @Autowired
  ShortURLRepo repo;

  /**
   * Checks that urls from csv are stored correctly on the database
   */
  @Test
  public void routingTest() {
    String filecontent = "https://google.com\nhttp://moodle.unizar.com\n";
    MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
            "multipart/form-data", filecontent.getBytes());

    Map<String, Object> headers = new HashMap<String,Object>();

    headers.put("sponsor", "q");
    headers.put("owner", "e");
    headers.put("ip", "e");

    try {
      producertemplate.sendBodyAndHeaders("direct:processCSV", multipartFile.getInputStream(), headers);
    } catch (IOException e) {
      e.printStackTrace();
    }

    assertEquals(2L,(long)repo.count());

  }


}