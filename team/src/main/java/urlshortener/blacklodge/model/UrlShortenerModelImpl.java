package urlshortener.blacklodge.model;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import urlshortener.blacklodge.repository.AdjRepository;
import urlshortener.blacklodge.repository.NounRepository;
import urlshortener.blacklodge.services.CheckWordsService;
import urlshortener.blacklodge.services.HashGeneratorService;
import urlshortener.blacklodge.services.MemeImageGeneratorService;
import urlshortener.blacklodge.services.SafeBrowsingService;
import urlshortener.common.domain.ShortURL;
import urlshortener.common.web.UrlShortenerController;

import static java.lang.Math.pow;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Logic Model for unique Url shortening
 *
 */
@Component
public class UrlShortenerModelImpl implements UrlShortenerModel {

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
                checkWordsService.check(url)) {
            
            Long hash = hashGeneratorService.hash(url);
            
            int div = (int)pow(10,String.valueOf(hash).length()/2);
            
            String noun = nounRepository.get((int)(hash/div)%nounRepository.number());
            String adj = adjRepository.get((int)(hash%div)%adjRepository.number());
            
            URI imageUrl = null;
            try {
                imageUrl = new URI(memeImageGeneratorService.generateImage(noun, adj));
            } catch (URISyntaxException e) {
                return null;
            }
            
            URI uri = linkTo(
                    methodOn(UrlShortenerController.class).redirectTo(String.valueOf(hash), null)
                            ).toUri();
                    
            Date created = new Date(System.currentTimeMillis());
            
            ShortURL su = new ShortURL(String.valueOf(hash), url, uri, sponsor, created , 
                    owner, HttpStatus.TEMPORARY_REDIRECT.value(), true, ip, null, imageUrl);
            
            return su; //shortUrlRepository.save(su);
        } else {
            return null;
        }   
    }

}
