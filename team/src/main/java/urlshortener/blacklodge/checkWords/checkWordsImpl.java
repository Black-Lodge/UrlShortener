package urlshortener.blacklodge.checkWords;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class checkWordsImpl implements checkWords {
    private static final String URL = "http://www.wdylike.appspot.com/?q=";

    public boolean check(String query) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response= restTemplate.getForEntity(URL + query, String.class);
        return Boolean.valueOf(response.getBody());
    }
}
