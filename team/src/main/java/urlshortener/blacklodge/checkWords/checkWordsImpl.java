package urlshortener.blacklodge.checkWords;

import org.glassfish.grizzly.Grizzly;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.logging.Logger;

public class checkWordsImpl implements checkWords {
    private static final String URL = "http://www.wdylike.appspot.com/?q=";
    private static final Logger LOGGER = Grizzly.logger(checkWordsImpl.class);

    public boolean check(String query) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response= restTemplate.getForEntity(URL + query, String.class);
        return Boolean.valueOf(response.getBody());
    }
}
