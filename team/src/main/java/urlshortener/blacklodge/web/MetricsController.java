package urlshortener.blacklodge.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import urlshortener.blacklodge.metrics.InfoCollector;

@EnableScheduling
@Controller
public class MetricsController {

    @Autowired
    private SimpMessagingTemplate template;

    private InfoCollector metrics = new InfoCollector();

    @Scheduled(fixedRate = 120000)
    public void realStats() {
        this.template.convertAndSend("/topic/stats", metrics.realStats());
    }

    @Scheduled(fixedRate = 120000)
    public void testStats() {
        this.template.convertAndSend("/topic/test", metrics.fakeStats());
    }



}
