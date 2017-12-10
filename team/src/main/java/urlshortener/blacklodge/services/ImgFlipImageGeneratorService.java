package urlshortener.blacklodge.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import urlshortener.blacklodge.domain.MemeImageRequest;
import urlshortener.blacklodge.domain.MemeImageResponse;

@Service
public class ImgFlipImageGeneratorService implements MemeImageGeneratorService{
    
    private final static Logger LOGGER = LoggerFactory.getLogger(ImgFlipImageGeneratorService.class);
    
    private final static String IMGFLIP_POST = "https://api.imgflip.com/caption_image";
    
    @Override
    public String generateImage(String noun, String adj) {
        
        MemeImageRequest mrequest = new MemeImageRequest();
        mrequest.setTemplate_id((long) 8072285);
        mrequest.setUsername("blacklodge");
        mrequest.setPassword("blacklodge");
        mrequest.setText0(noun);
        mrequest.setText1(adj);
        
        RestTemplate restTemplate = new RestTemplate();
        
        try {
            ResponseEntity<MemeImageResponse> response = restTemplate.postForEntity(IMGFLIP_POST, mrequest, MemeImageResponse.class);
            
            return response.getBody().getData().getUrl();
        } catch (Exception e) {
            LOGGER.error("Generate meme image failed for Noun: {}, Adj: {}. Error: {}", noun, adj, e.getMessage());
            // TODO: Quiza lanzar una excepcion para que nuestro servicio entre en modo 404.
            return "https://i.imgflip.com/20tzw3.jpg";
        }
    }

}
