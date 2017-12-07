package urlshortener.blacklodge;

import java.util.HashMap;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import urlshortener.blacklodge.web.MetricsServerEndpoint;
import org.glassfish.tyrus.server.Server;


@SpringBootApplication
public class Application extends SpringBootServletInitializer {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
		
		//Enable Global Information Metrics 
		runServerWebsockets();
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}
	
	/**
	 * Method that runs the Websockets service for the Metrics Global Information
	 */
	private static void runServerWebsockets() {
		Server server = new Server("localhost", 8025, "/globalinformation", new HashMap<>(),
				MetricsServerEndpoint.class);

		try (Scanner s = new Scanner(System.in)) {
			server.start();
			logger.info("Press 's' to shutdown now the websocketserver...");
			while (!s.hasNext("s"));
		} catch (Exception e) {
			logger.info("Server stopped"+ e.getMessage());
		} finally {
			server.stop();
			logger.info("Server stopped");
		}
	}

}