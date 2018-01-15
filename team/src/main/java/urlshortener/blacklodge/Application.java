package urlshortener.blacklodge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * Main class from the project. It launchs the application
 * Annotations
 * SpringBootApplication: As seen on the first lab
 * EnableFeignClients, EnableCircuitBreaker: To enable Feign clients and its functionality
 */
@EnableFeignClients
@EnableCircuitBreaker
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

  /**
   * Main method from the project. It starts all services
   * @param args Args for the program
   */
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(Application.class);
  }

}