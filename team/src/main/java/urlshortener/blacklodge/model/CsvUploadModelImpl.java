package urlshortener.blacklodge.model;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.io.IOException;
import java.util.ArrayList;
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

import urlshortener.blacklodge.csv.CsvResponse;
import urlshortener.blacklodge.repository.ShortURLRepo;
import urlshortener.blacklodge.web.AdsController;
import urlshortener.blacklodge.web.UrlShortenerControllerWithLogs;
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
    /**
    * Shortens the URLs on the file
    * @param file File where the URLs are stored
    * @param sponsor Ads
    * @param owner Owner of the request
    * @param ip IP from the request
    * @return The identifier to find the shortened URLs
    */
	public String csvUpload(MultipartFile file, String sponsor, String owner, String ip) {
        
        Map<String, Object> headers = new HashMap<String,Object>(); 

        headers.put("sponsor", sponsor); 
        headers.put("owner", owner); 
        headers.put("ip", ip); 
        logger.info("ip on csvUpload: "+ip);
        try {
            producertemplate.sendBodyAndHeaders("direct:processCSV", file.getInputStream(), headers);
        } catch (Exception e) {
            logger.error("Failed to process csv {}. Error: {}", file.getOriginalFilename(), e.getMessage());
        }
        
        return owner;
    }
    /**
     * Returns the shortened URLs of a given owner
     * @param owner Owner of the shortened URLs
     * @return CSV with the shortened URLs
     */
    public List<CsvResponse> getResult(String owner) {
       List<ShortURL> results = shortUrlRepository.findByOwner(owner);
       //logger.info("Peticion de owner "+owner);
       ArrayList<CsvResponse> list = new ArrayList<CsvResponse>();
        for (ShortURL result:results) {
        	if (result.getTarget() != null && result.getHash() != null) {
        		list.add(new CsvResponse(result.getTarget(),linkTo(
                        methodOn(UrlShortenerControllerWithLogs.class).redirectTo(result.getHash(), null)
                        ).toUri().toString(),true,null));
        		
        	}else {
        		if (result.getTarget() == null)
        		logger.error("Null pointer on target" + result);
        		if (result.getHash() == null)
            	logger.error("Null pointer on Hash" + result);
        		list.add(new CsvResponse(result.getTarget(),null,false,"Bad format"));
        	}
        	
         
        }
        return list;
                
      
    }
	
}
