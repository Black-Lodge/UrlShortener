package urlshortener.blacklodge.model;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import urlshortener.blacklodge.services.CheckWordsService;
import urlshortener.blacklodge.services.MemeImageGeneratorService;
import urlshortener.blacklodge.services.SafeBrowsingService;

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
    
    //@Autowired
    //HashGeneratorService hashGeneratorService;
    
    @Autowired
    MemeImageGeneratorService memeImageGeneratorService;
    
    public String shorten(String url) {

        /*
        UrlValidator urlValidator = new UrlValidator(new String[] { "http",
                "https" });
        // Validate URL, Safe url, No bad words
        if (urlValidator.isValid(url) && 
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
