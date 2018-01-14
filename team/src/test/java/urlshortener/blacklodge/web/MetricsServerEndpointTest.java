package urlshortener.blacklodge.web;


import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;


import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
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
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

import urlshortener.blacklodge.Application;
import urlshortener.blacklodge.metrics.InfoCollector;

import java.lang.reflect.Type;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@DirtiesContext
public class MetricsServerEndpointTest {
    private static final Logger logger = LoggerFactory.getLogger(MetricsServerEndpointTest.class);
		
    @Autowired
    private TestRestTemplate restTemplate;
		
    @LocalServerPort
    private int port;
		
    static final String WEBSOCKET_TOPIC = "/topic/tests";
    BlockingQueue<String> blockingQueue;
    WebSocketStompClient stompClient;

    @Before
    public void setup() {
        blockingQueue = new LinkedBlockingDeque<>();
        stompClient = new WebSocketStompClient(new SockJsClient(asList(
	                                                    new WebSocketTransport(new StandardWebSocketClient()))));
    }
    /**
     * Checks that metrics can be retrieved
     */
    @Test(timeout = 120000)
    public void testGetMetrics() {
        ResponseEntity<String> entity = restTemplate. getForEntity("/metrics", String.class);
        String json = entity.getBody();
        assertTrue(json.length()>0);
    }

    /**
     * Checks that metrics are updated after shortening a URL and after using the shortened URL
     */
    @Test(timeout = 120000)
    public void testGetSpecialMetrics() {
        String hash = JsonPath.parse(postLink("http://www.google.es/").getBody()).read("$.hash");
        ResponseEntity<String> entity = restTemplate.getForEntity("/metrics", String.class);
        String json = entity.getBody();
        JSONObject p = null;
        try {
            p = new JSONObject(json);
        }
        catch (JSONException e) {
            logger.error("Error transforming JSON");
            e.printStackTrace();
        }
        assertTrue(p!= null);
        int uris = 0;
        try {
            uris = p.getInt("gauge.servo.uris");
        } catch (JSONException e) {
            logger.error("Error after trying to get info from JSON");
        }
		    
        assertTrue(uris == 1);
        logger.info("HASH: "+ hash);
		ResponseEntity<String> entity2 = restTemplate.getForEntity( "/"+hash, String.class);
		assertThat(entity2.getStatusCode(), is(HttpStatus.TEMPORARY_REDIRECT));
		entity = restTemplate.getForEntity("/metrics", String.class);
			
		json = entity.getBody();
		p = null;
		try {
		    p = new JSONObject(json);
		} catch (JSONException e) {
		    logger.error("Error transforming JSON");
		    e.printStackTrace();
		}
		assertTrue(p!= null);
		int clicks = 0;
		try {
		    clicks = p.getInt("gauge.servo.clicks");
		    logger.info("Test p "+p);
		} catch (JSONException e) {
		    logger.error("Error after trying to get info from JSON");
        }
        assertTrue(clicks == 1);
    }

	/**
	 * Checks if websockets connections work as expected
	 * @throws InterruptedException stomp exception
	 * @throws TimeoutException stomp exception
	 * @throws ExecutionException stomp exception
	 */
	@Test(timeout = 150000)
    public void testWebsocket() throws InterruptedException, TimeoutException, ExecutionException {
	    StompSession session = stompClient.connect("http://localhost:"+port+"/websockets", new StompSessionHandlerAdapter() {})
                                              .get(10,SECONDS);
	    session.subscribe(WEBSOCKET_TOPIC, new DefaultStompFrameHandler());
	    Assert.assertEquals("hi",blockingQueue.poll(1,SECONDS));
	}

	private ResponseEntity<String> postLink(String url) {
	    MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
	    parts.add("url", url);
	    return restTemplate.postForEntity("/link", parts, String.class);
	}

	private class MyHandler extends StompSessionHandlerAdapter {
			 	
	    public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
	        logger.info("Now connected");
	    }

	}
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
