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
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

}