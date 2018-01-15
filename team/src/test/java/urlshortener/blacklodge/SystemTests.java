package urlshortener.blacklodge;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.nio.charset.Charset;


/**
 * Tests that the views are correctly used
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= RANDOM_PORT)
@DirtiesContext
public class SystemTests {

  @Autowired
  private TestRestTemplate restTemplate;

  @LocalServerPort
  private int port;

  /**
   * Tests that the home view is used correctly
   */
  @Test
  public void testHome(){
    ResponseEntity<String> entity = restTemplate.getForEntity("/", String.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
    assertTrue(entity.getHeaders().getContentType().isCompatibleWith(new MediaType("text", "html")));
    assertThat(entity.getBody(), containsString("<title>URL"));
  }

  /**
   * Tests that the CSS is sent correctly
   */
  @Test
  public void testCss() {
    ResponseEntity<String> entity = restTemplate.getForEntity("/webjars/bootstrap/3.3.5/css/bootstrap.min.css", String.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
    assertThat(entity.getHeaders().getContentType(), is(MediaType.valueOf("text/css")));
    assertThat(entity.getBody(), containsString("body"));
  }

  /**
   * Tests that creating a short URL works as expected
   */
  @Test
  public void testCreateLink() {
    ResponseEntity<String> entity = postLink("http://example.com/");
    assertThat(entity.getStatusCode(), is(HttpStatus.CREATED));
    assertTrue(entity.getHeaders().getLocation().toString().contains("http://localhost:"+ this.port+"/such_"));
    assertThat(entity.getHeaders().getContentType(), is(new MediaType("application", "json", Charset.forName("UTF-8"))));
    ReadContext rc = JsonPath.parse(entity.getBody());
    assertTrue(rc.read("$.hash").toString().contains("such_"));
    assertTrue(rc.read("$.uri").toString().contains("http://localhost:"+ this.port+"/such_"));
    assertThat(rc.read("$.target"), is("http://example.com/"));
    assertThat(rc.read("$.sponsor"), is(nullValue()));
  }

  /**
   * Tests that the redirection to the ad page works as expected
   */
  @Test
  public void testRedirection() {
    ResponseEntity<String> entity2 = postLink("http://example.com/");
    ReadContext rc = JsonPath.parse(entity2.getBody());
    ResponseEntity<String> entity = restTemplate.getForEntity( "/"+rc.read("$.hash"), String.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.TEMPORARY_REDIRECT));
    assertTrue(entity.getHeaders().getLocation().toString().contains("/ads/such_"));

  }

  /**
   * Sends a post petition to /link
   * @param url URL to send
   * @return What is returned after a POST message has been sent
   */
  private ResponseEntity<String> postLink(String url) {
    MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
    parts.add("url", url);
    return restTemplate.postForEntity("/link", parts, String.class);
  }

}