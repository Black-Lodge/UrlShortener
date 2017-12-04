package urlshortener.blacklodge.safeBrowsing;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;


/**
 * Wrapper for Google's safe browsing lookup API.
 *
 * 
 */
public class SafeBrowsing {

    
    //@Value("${GSB.api_key}")
    private String API_KEY;
    
    private String urlGSB;

    /**
     *
     * @param apiKey Google Safe Browsing API key
     */
    public SafeBrowsing(String apiKey_) {
        urlGSB = "https://safebrowsing.googleapis.com/v4/threatMatches:find?key="+API_KEY;
    }


    /**
     * Lookup an URL
     *
     * @param url the URL to check.
     * @return true if the URL is trusted; otherwise false.
     * @throws IOException if any general IO problems occur.
     */
    public boolean lookupURL(String url) throws IOException {
        
        LookupURL lkurl = new LookupURL();
        lkurl.setUrl(url);
        Client client = ClientBuilder.newClient();
        Response response = client.target(urlGSB)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(lkurl, MediaType.APPLICATION_JSON));
        LookupResult result = response.readEntity(LookupResult.class);
        
        
        return result.isTrusted();
    }

}