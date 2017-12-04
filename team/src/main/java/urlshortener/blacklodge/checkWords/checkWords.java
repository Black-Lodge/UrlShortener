package urlshortener.blacklodge.checkWords;

import org.glassfish.grizzly.Grizzly;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.logging.Logger;

public class checkWords {
    private String query;
    private static final String URL = "http://www.wdylike.appspot.com/?q=";
    private static final Logger LOGGER = Grizzly.logger(checkWords.class);

    public checkWords(String query_) {
        query = query_;
    }

    public boolean check() {
        return true;
    }
}
