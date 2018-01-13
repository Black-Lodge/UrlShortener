package urlshortener.blacklodge.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import urlshortener.blacklodge.metrics.GlobalInformation;
import urlshortener.blacklodge.metrics.InfoCollector;

@EnableScheduling
@Controller
public class MetricsController {
    private static final Logger logger = LoggerFactory.getLogger(MetricsController.class);
    @Autowired
    private SimpMessagingTemplate template;

    private InfoCollector metrics = new InfoCollector();

    @Scheduled(fixedRate = 120000,initialDelay = 5000)
    public void realStats() {
        logger.info("Real stats sent");
        this.template.convertAndSend("/topic/stats", metrics.realStats());
    }

    @Scheduled(fixedRate = 120000,initialDelay = 5000)
    public void testStats() {
        this.template.convertAndSend("/topic/test", metrics.fakeStats());
    }

    @SubscribeMapping("/topic/stats")
    public GlobalInformation connectionOpened() {
        logger.info("Detected subscription");
        return metrics.realStats();
    }

    @SubscribeMapping("/tests")
    public String connectionOpenedTest() {
        logger.info("Detected subscription");
        return "hi";
    }

}
