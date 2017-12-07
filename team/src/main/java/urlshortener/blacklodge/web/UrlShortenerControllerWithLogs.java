package urlshortener.blacklodge.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import urlshortener.common.domain.ShortURL;
import urlshortener.common.web.UrlShortenerController;

@RestController
public class UrlShortenerControllerWithLogs extends UrlShortenerController {

	private static final Logger logger = LoggerFactory.getLogger(UrlShortenerControllerWithLogs.class);

	
	private final GaugeService gaugeService;
	
	@Autowired
    public UrlShortenerControllerWithLogs(final GaugeService gaugeService) {
        this.gaugeService = gaugeService;
     
}	
	
	
	@Override
	@RequestMapping(value = "/{id:(?!link|index).*}", method = RequestMethod.GET)
	public ResponseEntity<?> redirectTo(@PathVariable String id, HttpServletRequest request) {
		logger.info("Requested redirection with hash " + id);
		ResponseEntity<?> a = super.redirectTo(id, request);
		
		//Update actuator with the click counts
		this.gaugeService.submit("clicks",clickRepository.count().intValue());
		
		
		return a;
	}

	@Override
	public ResponseEntity<ShortURL> shortener(@RequestParam("url") String url,
											  @RequestParam(value = "sponsor", required = false) String sponsor,
											  HttpServletRequest request) {
		logger.info("Requested new short for uri " + url);
		ResponseEntity<ShortURL> a = super.shortener(url, sponsor, request);
		
		//Update actuator with the total urls saved
		this.gaugeService.submit("uris", shortURLRepository.count().intValue());
		return a;
	}
}
