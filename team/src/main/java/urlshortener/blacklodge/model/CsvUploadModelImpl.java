package urlshortener.blacklodge.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import urlshortener.blacklodge.repository.ShortURLRepo;
import urlshortener.common.domain.ShortURL;


/**
 * Logic Model for uploaded CSV file processing
 *
 */
@Component
public class CsvUploadModelImpl implements CsvUploadModel {
    
    @Autowired
    ProducerTemplate producertemplate;
    
    @Autowired
    ShortURLRepo shortUrlRepository;

    /**
     * Shortens the URLs on the file
     * @param file File where the URLs are stored
     * @param sponsor Ads
     * @param owner Owner of the request
     * @param ip IP from the request
     * @return The identifier to find the shortened URLs
     */
    public String csvUpload(MultipartFile file, String sponsor, String owner, String ip) {
        /*
        List<String> urls = processCSV.processCSV(file);

        List<ShortURL> urlsShortened = new ArrayList<>();
        
        ShortURL shortened;
        
        for (int i = 0; i < urls.size(); i++) {
            shortened = urlShortenerModel.shorten(urls.get(i), sponsor, owner, ip);
            
            if (shortened != null) urlsShortened.add(shortened);
        }*/
        
        Map<String, Object> headers = new HashMap<String,Object>(); 

        headers.put("sponsor", sponsor); 
        headers.put("owner", owner); 
        headers.put("ip", ip); 

        try {
            producertemplate.sendBodyAndHeaders("direct:processCSV", file.getInputStream(), headers);
        } catch (CamelExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        // TODO: Retorna una direccion
        return null;
    }

    /**
     * Returns the shortened URLs of a given owner
     * @param owner Owner of the shortened URLs
     * @return CSV with the shortened URLs
     */
    public MultipartFile getResult(String owner) {
        List<ShortURL> results = shortUrlRepository.findByOwner(owner);
        String s = "";
        for (ShortURL result:results) {
            s = s + result.getTarget() + "\t" + result.getUri().toString() + "\n";
        }
        
        return new MockMultipartFile("file", "test.txt",
                "multipart/form-data", s.getBytes());
    }

}
