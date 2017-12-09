package urlshortener.blacklodge.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import urlshortener.blacklodge.services.SafeBrowsingService;

/**
 * Logic Model for unique Url shortening
 *
 */
@Component
public class UrlShortenerModelImpl implements UrlShortenerModel {
    
    //@Autowired
    //ValidateUrlService validateUrlService;

    @Autowired
    SafeBrowsingService safeBrowsingService;
    
    //@Autowired
    //CheckWordsService checkWordsService;
    
    //@Autowired
    //HashGeneratorService hashGeneratorService;
    
    //@Autowired
    //MemeImageGeneratorService memeImageGeneratorService;
    
    public String shorten(String url) {
        /*
        // Validate URL, Safe url, No bad words
        if (validateUrlService.validate(url) && 
                safeBrowisngService.checkSafetyUrl(url) &&
                checkWordsService.checkWords(url)) {
            
            String hash = hashGeneratorService.generate(url);
            String imageUrl = memeImageGeneratorService(hash);
            
         // No se devuelve realmente un string, sino una clase
            return hash+imageUrl;
        } else {
            return null;
        }
        */
        return null;
    }

}
