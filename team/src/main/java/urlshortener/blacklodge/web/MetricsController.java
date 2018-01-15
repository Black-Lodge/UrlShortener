package urlshortener.blacklodge.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import urlshortener.blacklodge.metrics.GlobalInformation;
import urlshortener.blacklodge.metrics.InfoCollector;

/**
 * Class that implements the controller for the metrics
 */
@EnableScheduling
@Controller
public class MetricsController {
  private static final Logger logger = LoggerFactory.getLogger(MetricsController.class);
  @Autowired
  private SimpMessagingTemplate template;

  private InfoCollector metrics = new InfoCollector();

  /**
   * Returns the real stats every 2 minutes
   */
  @Scheduled(fixedRate = 120000,initialDelay = 60000)
  public void realStats() {
    logger.info("Real stats sent");
    this.template.convertAndSend("/topic/stats", metrics.realStats());
  }

  /**
   * Returns some fake stats every 2 minutes
   */
  @Scheduled(fixedRate = 120000,initialDelay = 60000)
  public void testStats() {
    this.template.convertAndSend("/topic/test", metrics.fakeStats());
  }

  /**
   * Returns the real stats after a new connection was established
   * @return metrics
   */
  @SubscribeMapping("/topic/stats")
  public GlobalInformation connectionOpened() {
    logger.info("Detected subscription");
    return metrics.realStats();
  }

  /**
   * Returns fake stats after a new connection was established
   * @return fake metrics
   */
  @SubscribeMapping("/topic/tests")
  public String connectionOpenedTest() {
    logger.info("Detected subscription");
    return "hi";
  }

}
