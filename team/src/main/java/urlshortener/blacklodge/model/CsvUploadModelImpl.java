package urlshortener.blacklodge.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


import urlshortener.blacklodge.repository.ShortURLRepo;
import urlshortener.blacklodge.web.AdsController;
import urlshortener.common.domain.ShortURL;


/**
 * Logic Model for uploaded CSV file processing
 *
 */
@Component
public class CsvUploadModelImpl implements CsvUploadModel {
	private static final Logger logger = LoggerFactory.getLogger(CsvUploadModelImpl.class);
	
    @Autowired
    ProducerTemplate producertemplate;
    
    
    ShortURLRepo shortUrlRepository;
    
    @Autowired
    public CsvUploadModelImpl(ShortURLRepo shortUrlRepository) {
		this.shortUrlRepository = shortUrlRepository;
	}

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
    
    public String getResult(String owner) {
       List<ShortURL> results = shortUrlRepository.findByOwner(owner);
       logger.info("Peticion de owner "+owner);
        String s = "";
        for (ShortURL result:results) {
        	logger.info(result.toString());
        	if (result.getTarget() != null && result.getUri() != null) {
        		s = s + result.getTarget() + "\t" + result.getUri().toString() + "\n";
        	}else {
        		if (result.getTarget() == null)
        		logger.error("Null pointer on target" + result);
        		if (result.getUri() == null)
            	logger.error("Null pointer on uri" + result);
        	}
        	
         
        }
        return s;
                
      
    }
	
}
