package urlshortener.blacklodge.web;

import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeoutException;
import javax.websocket.DeploymentException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import urlshortener.blacklodge.Application;

/**
 * Tests that the ads controller works as expected
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@DirtiesContext
public class AdsControllerTest {
  private static final Logger logger = LoggerFactory.getLogger(AdsControllerTest.class);

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private WebApplicationContext wac;

  private MockMvc mockMvc;

  @LocalServerPort
  private int port;

  static final String WEBSOCKET_TOPIC = "/topic/ads/";


  BlockingQueue<String> blockingQueue;
  WebSocketStompClient stompClient;

  /**
   * Prepares the setup required for testing
   */
  @Before
  public void setup() {
    blockingQueue = new LinkedBlockingDeque<>();
    stompClient = new WebSocketStompClient(new SockJsClient(asList(
            new WebSocketTransport(new StandardWebSocketClient()))));
    DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
    this.mockMvc = builder.build();
  }

  /**
   * Checks that after creating a short URL, the ad page can be reached
   * @throws Exception Exception from MockMvc
   */
  @Test
  public void testRedirectWithAds() throws Exception {
    ResponseEntity<String> entity2 = postLink("http://www.google.es/");
    ReadContext rc = JsonPath.parse(entity2.getBody());
    String id = rc.read("$.hash").toString();

    ResultMatcher ok = MockMvcResultMatchers.status().isOk();

    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/ads/"+id+"/");
    this.mockMvc.perform(builder)
            .andExpect(ok);
  }

  /**
   * Checks that the original URL is sent after 10 seconds
   * @throws InterruptedException Blocking Queue exception
   * @throws ExecutionException Another blocking queue exception
   * @throws TimeoutException Another blocking queue exception
   */
  @Test
  public void testCountDown() throws InterruptedException, ExecutionException, TimeoutException {
    ResponseEntity<String> entity2 = postLink("http://www.google.es/");
    ReadContext rc = JsonPath.parse(entity2.getBody());
    String id = rc.read("$.hash").toString();

    StompSession session = stompClient.connect("http://localhost:"+port+"/websockets",
            new StompSessionHandlerAdapter() {})
            .get(10,SECONDS);
    session.subscribe("/topic/ads/"+id+"/12345678/", new DefaultStompFrameHandler());

    for (int i = AdsController.MAXSECONDS -1 ; i>0 ; i-- ) {
      JSONObject p = new JSONObject(blockingQueue.poll(5,SECONDS));
      Assert.assertEquals(i,p.get("countdown") );
      Assert.assertEquals(org.json.JSONObject.NULL,p.get("url") );
    }
    JSONObject p = new JSONObject(blockingQueue.poll(5,SECONDS));
    Assert.assertEquals(0,p.get("countdown") );
    Assert.assertEquals("http://www.google.es/",p.get("url") );

  }

  /**
   * Sends a POST message with the url given to /link
   * @param url URL to be sent
   * @return Server answers after sending a POST message to /link
   */
  private ResponseEntity<String> postLink(String url) {
    MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
    parts.add("url", url);
    return restTemplate.postForEntity("/link", parts, String.class);
  }

  /**
   * Class used to test websockets operations
   */
  class DefaultStompFrameHandler implements StompFrameHandler {
    @Override
    public Type getPayloadType(StompHeaders stompHeaders) {
      return byte[].class;
    }

    @Override
    public void handleFrame(StompHeaders stompHeaders, Object o) {
      blockingQueue.offer(new String((byte[]) o));
    }
  }
}
