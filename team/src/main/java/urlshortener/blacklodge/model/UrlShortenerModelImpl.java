package urlshortener.blacklodge.model;

import java.net.URI;
import java.sql.Date;

import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import urlshortener.blacklodge.repository.AdjRepository;
import urlshortener.blacklodge.repository.NounRepository;
import urlshortener.blacklodge.repository.ShortURLRepo;
import urlshortener.blacklodge.services.CheckAvailabilityService;
import urlshortener.blacklodge.services.CheckWordsService;
import urlshortener.blacklodge.services.HashGeneratorService;
import urlshortener.blacklodge.services.MemeImageGeneratorService;
import urlshortener.blacklodge.services.SafeBrowsingService;
import urlshortener.blacklodge.web.UrlShortenerControllerWithLogs;
import urlshortener.common.domain.ShortURL;


import static java.lang.Math.pow;
import static java.lang.Math.abs;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Logic Model for unique Url shortening
 *
 */
@Component
public class UrlShortenerModelImpl implements UrlShortenerModel {
    
    private final static Logger logger = LoggerFactory.getLogger(UrlShortenerModelImpl.class);
    
    @Autowired
    SafeBrowsingService safeBrowsingService;
    
    @Autowired
    CheckWordsService checkWordsService;
    
    @Autowired
    CheckAvailabilityService checkAvailabilityService;
    
    @Autowired
    HashGeneratorService hashGeneratorService;
    
    @Autowired
    MemeImageGeneratorService memeImageGeneratorService;
    
    @Autowired
    AdjRepository adjRepository;
    
    @Autowired
    NounRepository nounRepository;
    
    @Autowired
    ShortURLRepo shortUrlRepository;
    
    public ShortURL shorten(String url, String sponsor, String owner, String ip) {

        
        UrlValidator urlValidator = new UrlValidator(new String[] { "http",
                "https" });

        // Validate URL, Safe url, No bad words
        if (urlValidator.isValid(url) && 
                safeBrowsingService.checkSafetyUrl(url) &&
                checkAvailabilityService.check(url) &&
                !checkWordsService.check(url)) {
            
            ShortURL result = null;
            ShortURL su;
            Long hash;
            int div;
            String noun, adj, hashf, imageUrl;
            Date created;
            
            URI uri;
            
            while (result == null) {        
                created = new Date(System.currentTimeMillis());
                logger.info("Generating hash for {}",url+System.currentTimeMillis());
                
                hash = hashGeneratorService.hash(url+System.currentTimeMillis());
                
                div = (int)pow(10,String.valueOf(hash).length()/2);
                
                noun = nounRepository.get(abs((int)(hash/div)%nounRepository.number()));
                adj = adjRepository.get(abs((int)(hash%div)%adjRepository.number()));
                
                hashf = "such_"+noun+"_so_"+adj;
                
                imageUrl = memeImageGeneratorService.generateImage(noun, adj);
                           
                uri = linkTo(
                        methodOn(UrlShortenerControllerWithLogs.class).redirectTo(hashf, null)
                                ).toUri();
                                         
                su = new ShortURL(hashf, url, uri, sponsor, created , 
                        owner, HttpStatus.TEMPORARY_REDIRECT.value(), true, ip, null, imageUrl);
                
                result = shortUrlRepository.save(su);
            }
            return result;
        } else {
            logger.error("Shorten URL failed for url: {}. Error: {}", url, "failed");
            return null;
        }   
    }

}
