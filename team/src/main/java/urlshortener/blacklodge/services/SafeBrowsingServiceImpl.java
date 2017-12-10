package urlshortener.blacklodge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import urlshortener.blacklodge.infrastructure.SafeBrowsing;

@Service
public class SafeBrowsingServiceImpl implements SafeBrowsingService {

    @Autowired
    SafeBrowsing safebrowsing;
    
    @Override
    public boolean checkSafetyUrl(String url) {
        return safebrowsing.lookupURL(url);
    }

}
