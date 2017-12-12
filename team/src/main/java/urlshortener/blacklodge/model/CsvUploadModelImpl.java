package urlshortener.blacklodge.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import urlshortener.blacklodge.infrastructure.ProcessCSV;
import urlshortener.common.domain.ShortURL;

/**
 * Logic Model for uploaded CSV file processing
 *
 */
@Component
public class CsvUploadModelImpl implements CsvUploadModel {
    
    @Autowired
    ProcessCSV processCSV;
    
    @Autowired
    UrlShortenerModel urlShortenerModel;
    
    public List<ShortURL> csvUpload(MultipartFile file, String sponsor, String owner, String ip) {
        
        List<String> urls = processCSV.processCSV(file);

        List<ShortURL> urlsShortened = new ArrayList<>();
        
        ShortURL shortened;
        
        for (int i = 0; i < urls.size(); i++) {
            shortened = urlShortenerModel.shorten(urls.get(i), sponsor, owner, ip);
            
            if (shortened != null) urlsShortened.add(shortened);
        }
        return urlsShortened;
    }
    

}
