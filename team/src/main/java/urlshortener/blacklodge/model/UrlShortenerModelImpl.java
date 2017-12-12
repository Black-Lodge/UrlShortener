package urlshortener.blacklodge.model;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;

import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import urlshortener.blacklodge.repository.AdjRepository;
import urlshortener.blacklodge.repository.NounRepository;
import urlshortener.blacklodge.services.CheckWordsService;
import urlshortener.blacklodge.services.GoogleSafeBrowsingService;
import urlshortener.blacklodge.services.HashGeneratorService;
import urlshortener.blacklodge.services.MemeImageGeneratorService;
import urlshortener.blacklodge.services.SafeBrowsingService;
import urlshortener.common.domain.ShortURL;
import urlshortener.common.web.UrlShortenerController;

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
    
    private final static Logger LOGGER = LoggerFactory.getLogger(UrlShortenerModelImpl.class);
    
    @Autowired
    SafeBrowsingService safeBrowsingService;
    
    @Autowired
    CheckWordsService checkWordsService;
    
    @Autowired
    HashGeneratorService hashGeneratorService;
    
    @Autowired
    MemeImageGeneratorService memeImageGeneratorService;
    
    @Autowired
    AdjRepository adjRepository;
    
    @Autowired
    NounRepository nounRepository;
    
    public ShortURL shorten(String url, String sponsor, String owner, String ip) {

        
        UrlValidator urlValidator = new UrlValidator(new String[] { "http",
                "https" });

        // Validate URL, Safe url, No bad words
        if (urlValidator.isValid(url) && 
                safeBrowsingService.checkSafetyUrl(url) &&
                !checkWordsService.check(url)) {
            
            Long hash = hashGeneratorService.hash(url);
            
            int div = (int)pow(10,String.valueOf(hash).length()/2);
            
            String noun = nounRepository.get(abs((int)(hash/div)%nounRepository.number()));
            String adj = adjRepository.get(abs((int)(hash%div)%adjRepository.number()));
            
            String hashf = "such_"+noun+"_so_"+adj;
            
            String imageUrl = memeImageGeneratorService.generateImage(noun, adj);

            
            URI uri = linkTo(
                    methodOn(UrlShortenerController.class).redirectTo(hashf, null)
                            ).toUri();
                    
            Date created = new Date(System.currentTimeMillis());
            

            ShortURL su = new ShortURL(hashf, url, uri, sponsor, created , 
                    owner, HttpStatus.TEMPORARY_REDIRECT.value(), true, ip, null, imageUrl);

            
            return su; //shortUrlRepository.save(su);
        } else {
            LOGGER.error("Shorten URL failed for url: {}. Error: {}", url, "failed");
            return null;
        }   
    }

}
