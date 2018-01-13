package urlshortener.blacklodge.scheduler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import urlshortener.blacklodge.model.UrlShortenerModelImpl;
import urlshortener.blacklodge.repository.ShortURLRepo;
import urlshortener.blacklodge.services.SafeBrowsingService;
import urlshortener.common.domain.ShortURL;

/**
 * Class that acts a scheduled task that checks if the URLs on the db are still safe
 */
@Configuration
@EnableScheduling
public class SafeBrowsingScheduler {
    private final static Logger logger = LoggerFactory.getLogger(SafeBrowsingScheduler.class);
    
    @Autowired
    SafeBrowsingService safeBrowsingService;
    
    @Autowired
    ShortURLRepo shortUrlRepository;

    /**
     * Checks periodically that every URL is still safe
     */
    @Scheduled(fixedDelay = 50000)
    public void checkSafe() {
        logger.info("Check safe scheduler");
        List<ShortURL> shorturls = shortUrlRepository.findAll();

        boolean before;
        boolean after;
        
        for (ShortURL shorturl : shorturls) {
                //logger.info(shorturl.getTarget());
                before = shorturl.getSafe();
                after = safeBrowsingService.checkSafetyUrl(shorturl.getTarget());
                //logger.info(String.valueOf(after));
                if (before != after) {
                    shorturl.setSafe(after);
                    shortUrlRepository.update(shorturl);
                }
        }
        
    }
}
