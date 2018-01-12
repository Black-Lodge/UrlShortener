package urlshortener.blacklodge.scheduler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import urlshortener.blacklodge.model.UrlShortenerModelImpl;
import urlshortener.blacklodge.repository.ShortURLRepo;
import urlshortener.blacklodge.services.SafeBrowsingService;
import urlshortener.common.domain.ShortURL;

public class SafeBrowsingScheduler {
    private final static Logger logger = LoggerFactory.getLogger(SafeBrowsingScheduler.class);
    
    @Autowired
    SafeBrowsingService safeBrowsingService;
    
    @Autowired
    ShortURLRepo shortUrlRepository;
    
    @Scheduled(fixedDelay = 100000L)
    public void checkSafe() {
        List<ShortURL> shorturls = shortUrlRepository.findAll();

        boolean before;
        boolean after;
        
        for (ShortURL shorturl : shorturls) {
                before = shorturl.getSafe();
                after = safeBrowsingService.checkSafetyUrl(shorturl.getTarget());
                if (before != after) {
                    shorturl.setSafe(after);
                    shortUrlRepository.update(shorturl);
                }
        }
        
    }
}
