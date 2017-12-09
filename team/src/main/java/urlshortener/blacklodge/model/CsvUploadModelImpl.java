package urlshortener.blacklodge.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import urlshortener.blacklodge.infrastructure.ProcessCSV;

@Component
public class CsvUploadModelImpl implements CsvUploadModel {
    
    @Autowired
    ProcessCSV processCSV;
    
    @Autowired
    UrlShortenerModel urlShortenerModel;
    
    public List<String> csvUpload(MultipartFile file) {
        List<String> urls = processCSV.processCSV(file);
        
        List<String> urlsShortened = new ArrayList<String>();
        
        for (int i = 0; i < urls.size(); i++) {
            urlsShortened.add(urlShortenerModel.shorten(urls.get(i)));
        }
        return urlsShortened;
    }
    

}
