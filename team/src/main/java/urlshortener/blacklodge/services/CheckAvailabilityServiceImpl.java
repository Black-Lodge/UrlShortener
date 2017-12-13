package urlshortener.blacklodge.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class CheckAvailabilityServiceImpl implements CheckAvailabilityService {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(CheckAvailabilityServiceImpl.class);
    
    @Override
    public boolean check (String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(3000);
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            return 200 == responseCode;
        } catch (IOException e) {
            LOGGER.error("Check availability failed for url {}. Error: {}.", url, e.getMessage());
            return false;
        }
    }

}
