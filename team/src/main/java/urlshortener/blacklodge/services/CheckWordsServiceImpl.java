package urlshortener.blacklodge.services;

import org.glassfish.grizzly.Grizzly;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.logging.Logger;

@Service
public class CheckWordsServiceImpl implements CheckWordsService {
    private static final String URL = "http://www.wdylike.appspot.com/?q=";
    private static final Logger logger = Grizzly.logger(CheckWordsServiceImpl.class);

    public boolean check(String query) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response= restTemplate.getForEntity(URL + query, String.class);
        return Boolean.valueOf(response.getBody());
    }
}
