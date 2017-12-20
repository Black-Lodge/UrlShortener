package urlshortener.blacklodge.services;

import org.glassfish.grizzly.Grizzly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import urlshortener.blacklodge.accessor.CheckWordsAccesor;

import java.util.logging.Logger;

@Service
public class CheckWordsServiceImpl implements CheckWordsService {
    private static final String URL = "http://www.wdylike.appspot.com/?q=";
    private static final Logger LOGGER = Grizzly.logger(CheckWordsServiceImpl.class);

    @Autowired
    private CheckWordsAccesor accesor;

    public boolean check(String query) {
        Boolean a = accesor.check(query);
       return a;
    }
}
