package urlshortener.blacklodge.web;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import urlshortener.blacklodge.ads.AdsResponse;
import urlshortener.blacklodge.repository.ShortURLRepo;

/**
 * Class that implements the controller for the ads system
 */
@Controller
public class AdsController {
  private static final Logger logger = LoggerFactory.getLogger(AdsController.class);
  private Map<String,Integer> countdowns = Collections.synchronizedMap(new HashMap<String,Integer>());
  public final static Integer MAXSECONDS = 12; // +2 seconds to let time to play video of 10 seconds

  ShortURLRepo sr;

  @Autowired
  public AdsController(ShortURLRepo sr) {
    this.sr = sr;
  }

  @Autowired
  private SimpMessagingTemplate template;

  /**
   * Redirect petition to the ad page
   * @param id hash
   * @param model model
   * @return name of the ad view
   */
  @RequestMapping(value = "/ads/{id}")
  public String redirectWithAds(@PathVariable String id, Model model) {
    model.addAttribute("msg", id);
    //generate random video 0-10
    model.addAttribute("video",(int)(Math.random()*10));
    logger.info("ads petition");
    return "ads";
  }

  /**
   * Check if the petitions should be redirected to the original page or not yet
   */
  @Scheduled(fixedRate = 1000)
  public void updateCountdown() {
    //Update people waiting for ads
    Set<String> codes = countdowns.keySet();
    for (String code : codes) {
      String id = code.split("/")[0];
      Integer result = countdowns.get(code) -1;
      if (result == 0) {
        logger.info("Sending real url to client "+code);
        this.template.convertAndSend("/topic/ads/"+code+"/",
                new AdsResponse(result,sr.findByKey(id).getTarget().toString()));
        countdowns.remove(code);
      }else {
        this.template.convertAndSend("/topic/ads/"+code+"/",  new AdsResponse(result,null));
        countdowns.put(code, result);
      }
    }
  }

  /**
   * Check if a new connection to the websocket channel was opened
   * @param id Hash of the url
   * @param userkey Generated userKey
   */
  @SubscribeMapping("/topic/ads/{id}/{userkey}/")
  public void connectionOpened(@DestinationVariable String id,@DestinationVariable String userkey) {
    logger.info("Detected subscription to ads "+id+" with key "+userkey);
    countdowns.put(id+"/"+userkey,MAXSECONDS);
  }
}
