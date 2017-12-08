package urlshortener.blacklodge.web;


import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



import javax.websocket.ClientEndpointConfig;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;


import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.server.Server;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import urlshortener.blacklodge.metrics.EventSubscriber;









@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= DEFINED_PORT)
@DirtiesContext
public class MetricsServerEndpointTest {
		private static final Logger logger = LoggerFactory.getLogger(MetricsServerEndpointTest.class);

		private Server server;

		@Autowired
		private TestRestTemplate restTemplate;
		
		
		@LocalServerPort
		private int port;
		
		@Before
		public void setup() throws DeploymentException {
			server = new Server("localhost", 8025, "/globalinformation", new HashMap<String, Object>(), MetricsServerEndpoint.class);
			server.start();
		}

		
		
		@Test(timeout = 120000)
		public void testGetMetrics() throws DeploymentException, IOException, URISyntaxException, InterruptedException {
		   ResponseEntity<String> entity = restTemplate.getForEntity("/metrics", String.class);
		   String json = entity.getBody();
		   assertTrue(json.length()>0);
		}
		
		@Test(timeout = 120000)
		public void testGetSpecialMetrics() throws DeploymentException, IOException, URISyntaxException, InterruptedException {
		   postLink("http://example.com/");
		  
		   ResponseEntity<String> entity = restTemplate.getForEntity("/metrics", String.class);
		   String json = entity.getBody();
		    JSONObject p = null;
			try {
				p = new JSONObject(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    assertTrue(p!= null);
		    int uris = 0;
		    try {
				uris = p.getInt("gauge.uris");
			} catch (JSONException e) {
				
			}
		    
		   assertTrue(uris == 1);
		  

			ResponseEntity<String> entity2 = restTemplate.getForEntity( "/f684a3c4", String.class);
			assertThat(entity2.getStatusCode(), is(HttpStatus.TEMPORARY_REDIRECT));
			assertThat(entity2.getHeaders().getLocation(), is(new URI("http://example.com/")));
			
			entity = restTemplate.getForEntity("/metrics", String.class);
			
			 json = entity.getBody();
			  p = null;
				try {
					p = new JSONObject(json);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    assertTrue(p!= null);
			    int clicks = 0;
			    try {
			    	clicks = p.getInt("gauge.clicks");
				} catch (JSONException e) {
					
				}
			    
			   assertTrue(clicks == 1);
		}
		
		@Test(timeout = 150000)
		public void testWebsocket() throws DeploymentException, IOException, URISyntaxException, InterruptedException {
		   postLink("http://example.com/");
		  
		   ResponseEntity<String> entity = restTemplate.getForEntity("/metrics", String.class);
		   String json = entity.getBody();
		    JSONObject p = null;
			try {
				p = new JSONObject(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    assertTrue(p!= null);
		    int uris = 0;
		    try {
				uris = p.getInt("gauge.uris");
			} catch (JSONException e) {
				
			}
		    
		   assertTrue(uris == 1);
		
		List<String> list = new ArrayList<>();
		ClientEndpointConfig configuration = ClientEndpointConfig.Builder.create().build();
		ClientManager client = ClientManager.createClient();
		client.connectToServer(new MetricsEndpoint(list), configuration, new URI("ws://localhost:8025/globalinformation/globalinformation"));
		synchronized (list) {list.wait();}
		
		 json = list.get(0);
		 p = null;
			try {
				p = new JSONObject(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  assertTrue(p!= null);
		  uris = 0;
		    try {
				uris = p.getInt("uris");
			} catch (JSONException e) {
				
			}
		   assertTrue(uris == 1);
		  
		}
		
		@After
		public void close() {
			server.stop();
		}
		
		private ResponseEntity<String> postLink(String url) {
			MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
			parts.add("url", url);
			return restTemplate.postForEntity("/link", parts, String.class);
		}
		
		 private static class MetricsEndpoint extends Endpoint {

		        private final List<String> list;

		        MetricsEndpoint(List<String> list) {
		            this.list = list;
		        }

		        @Override
		        public void onOpen(Session session, EndpointConfig config) {

		            session.addMessageHandler(new MetricsMessageHandler());
		           
		        }

		        private class MetricsMessageHandler implements MessageHandler.Whole<String> {

		            @Override
		            public void onMessage(String message) {
		            	logger.debug("Test client gets message");
		                list.add(message);
		                synchronized (list) {list.notifyAll();} 
		            }
		        }
		    }
}
