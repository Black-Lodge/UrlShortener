package urlshortener.blacklodge.web;


import java.util.Collections;
import java.util.HashMap;

import java.util.Map;
import java.util.Set;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import urlshortener.blacklodge.ads.AdsResponse;


@Controller
public class AdsController {
	private static final Logger logger = LoggerFactory.getLogger(AdsController.class);
	
	private Map<String,Integer> countdowns = Collections.synchronizedMap(new HashMap<String,Integer>());
	private Integer MAXSECONDS = 10;
	@Autowired
    private SimpMessagingTemplate template;
	
	@RequestMapping(value = "/ads/{id}", method = RequestMethod.GET)
	public String redirectWithAds(@PathVariable String id, Model model) {
		logger.info("sending to ads view");
		model.addAttribute("msg", id);
		return "ads";
	}
	
	@Scheduled(fixedRate = 1000)
    public void updateCountdown() {
		//Update people waiting for ads
		Set<String> ks = countdowns.keySet();
		for (String key : ks) {
			Integer result = countdowns.get(key) -1;
			if (result == 0) {
				this.template.convertAndSend("/topic/"+key, new AdsResponse(result,"uri real"));
				countdowns.remove(key);
			}else {
				this.template.convertAndSend("/topic/"+key,  new AdsResponse(result,null));
				countdowns.put(key, result);
			}
			
		}
       
    }

    @SubscribeMapping(value = "/topic/ads/{id}")
    public AdsResponse connectionOpened(@PathVariable String id) {
        logger.info("Detected subscription to ads");
        countdowns.put(id,MAXSECONDS);
        return  new AdsResponse(MAXSECONDS,null);
    }
}
