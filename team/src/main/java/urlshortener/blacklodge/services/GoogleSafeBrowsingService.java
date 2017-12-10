package urlshortener.blacklodge.services;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.safebrowsing.Safebrowsing;
import com.google.api.services.safebrowsing.model.*;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * Wrapper for Google's safe browsing lookup API.
 *
 * 
 */
@Service
public class GoogleSafeBrowsingService implements SafeBrowsingService {


    private final static Logger LOGGER = LoggerFactory.getLogger(GoogleSafeBrowsingService.class);
    
    @Value("${GSB.api_key}")
    private String API_KEY;
    

    /**
     * Lookup an URL
     *
     * @param url the URL to check.
     * @return true if the URL is trusted; otherwise false.
     */
    @Override
    public boolean checkSafetyUrl(String url) {
        
        FindThreatMatchesRequest findThreatMatchesRequest = new FindThreatMatchesRequest();
        
        ThreatEntry threatEntry = new ThreatEntry();
        threatEntry.setUrl(url);
        
        ThreatInfo threatInfo = new ThreatInfo();
        threatInfo.setPlatformTypes(Arrays.asList("ALL_PLATFORMS"));    
        threatInfo.setThreatEntries(Arrays.asList(threatEntry));
        threatInfo.setThreatEntryTypes(Arrays.asList("URL"));
        threatInfo.setThreatTypes(Arrays.asList(
                "THREAT_TYPE_UNSPECIFIED",
                "MALWARE",
                "SOCIAL_ENGINEERING",
                "UNWANTED_SOFTWARE",
                "POTENTIALLY_HARMFUL_APPLICATION"));
        
        findThreatMatchesRequest.setThreatInfo(threatInfo);


        try {
            Safebrowsing.Builder safebrowsingBuilder = new Safebrowsing.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(), new JacksonFactory(), null);

            Safebrowsing safebrowsing = safebrowsingBuilder.build();
            Safebrowsing.ThreatMatches.Find find = safebrowsing.threatMatches().find(findThreatMatchesRequest);
            find.setKey(API_KEY);
            
            List<ThreatMatch> matches = find.execute().getMatches();
            
            if (matches != null && matches.size() > 0) {
                return false;
            } else {
                return true;
            }
            

        } catch (Exception e) {
            LOGGER.error("LookUp failed for url {}. Error: {}", url, e.getMessage());
            return false;
        }
    
        
        
    }

}