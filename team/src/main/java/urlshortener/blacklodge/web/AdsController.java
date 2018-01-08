package urlshortener.blacklodge.web;


import java.util.Collections;
import java.util.HashMap;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import urlshortener.blacklodge.ads.AdsResponse;
import urlshortener.blacklodge.model.UrlShortenerModel;
import urlshortener.blacklodge.repository.ShortURLRepo;
import urlshortener.common.domain.ShortURL;


@Controller
public class AdsController {
	private static final Logger logger = LoggerFactory.getLogger(AdsController.class);
	private Map<String,Integer> countdowns = Collections.synchronizedMap(new HashMap<String,Integer>());
	private Map<String,String> uris = Collections.synchronizedMap(new HashMap<String,String>());
	
	private Integer MAXSECONDS = 10;
	
	
    ShortURLRepo sr;
    
    @Autowired
	public AdsController(ShortURLRepo sr) {
		this.sr = sr;
	}
    
	@Autowired
    private SimpMessagingTemplate template;
	
	@RequestMapping(value = "/ads/{id}")
	public String redirectWithAds(@PathVariable String id, Model model) {
		model.addAttribute("msg", id);
		ShortURL s = sr.findByKey(id);
		String path = s.getTarget();
		logger.info("sending to ads view "+ path);
		uris.put(id, path);
		return "ads";
	}
	
	@Scheduled(fixedRate = 1000)
    public void updateCountdown() {
		//Update people waiting for ads
		Set<String> ks = countdowns.keySet();
		for (String key : ks) {
			Integer result = countdowns.get(key) -1;
			if (result == 0) {
				this.template.convertAndSend("/topic/ads/"+key, new AdsResponse(result,uris.get(key)));
				countdowns.remove(key);
			}else {
				this.template.convertAndSend("/topic/ads/"+key,  new AdsResponse(result,null));
				countdowns.put(key, result);
			}
		}
    }

    @SubscribeMapping("/topic/ads/{id}")
    public void connectionOpened(@DestinationVariable String id) {
        logger.info("Detected subscription to ads "+id);
        countdowns.put(id,MAXSECONDS);
    }
}
