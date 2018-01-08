package urlshortener.blacklodge.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import urlshortener.blacklodge.model.UrlShortenerModel;
import urlshortener.common.domain.ShortURL;
import urlshortener.common.web.UrlShortenerController;

@RestController
public class UrlShortenerControllerWithLogs extends UrlShortenerController {

	private static final Logger logger = LoggerFactory.getLogger(UrlShortenerControllerWithLogs.class);
	
	private static Set<String> ips = new HashSet<String>() ;
	
	private final GaugeService gaugeService;
	
	@Autowired
	UrlShortenerModel urlShortenerModel;
	
	@Autowired
    public UrlShortenerControllerWithLogs(final GaugeService gaugeService) {
        this.gaugeService = gaugeService;
     
	}	
	
	
	@Override
	@RequestMapping(value = "/{id:(?!link|index).*}", method = RequestMethod.GET)
	public ResponseEntity<?> redirectTo(@PathVariable String id, HttpServletRequest request) {
		//Time to respond the last redirect
		long start = System.currentTimeMillis();
		
		logger.info("Requested redirection with hash " + id);
		ResponseEntity<?> a = super.redirectTo(id, request);
		
		//Update actuator with the click counts
		this.gaugeService.submit("clicks",clickRepository.count().intValue());
		//Update actuator with total users with diferent ip
		ips.add(request.getRemoteAddr());
		this.gaugeService.submit("users", ips.size());
		
		long end = System.currentTimeMillis()-start;
		this.gaugeService.submit("lastRedirection", end);
		
		//Comprobar si tiene anuncios para que se encarge el controlador de anuncios
		if (true) { //Actualizar metodo para que lea si contiene realmente anuncios
			//tiene anunciones
			HttpHeaders headers = new HttpHeaders();
			headers.add("Location", "/ads/"+id);
			return new ResponseEntity<String>(headers,HttpStatus.FOUND);
		}else {
			return a;
		}
		
		
	}
	
	private String extractIP(HttpServletRequest request) {
	        return request.getRemoteAddr();
	    }
	   
	@Override
	public ResponseEntity<ShortURL> shortener(@RequestParam("url") String url,
											  @RequestParam(value = "sponsor", required = false) String sponsor,
											  HttpServletRequest request) {
		
		//Time to respond the last petition 
		long start = System.currentTimeMillis();
		
		logger.info("Requested new short for uri " + url);
		//ResponseEntity<ShortURL> a = super.shortener(url, sponsor, request);
		
		
		ShortURL su = urlShortenerModel.shorten(url, sponsor, UUID
                .randomUUID().toString(), extractIP(request));
		
	    //Update actuator with the total urls saved
        this.gaugeService.submit("uris", shortURLRepository.count().intValue());
        //Update actuator with total users with diferent ip
        ips.add(request.getRemoteAddr());
        this.gaugeService.submit("users", ips.size());
        
        long end = System.currentTimeMillis()-start;
        this.gaugeService.submit("lastPetition", end);
        
        if (su != null) {
            HttpHeaders h = new HttpHeaders();
            h.setLocation(su.getUri());
            return new ResponseEntity<>(su, h, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        

	}
}
