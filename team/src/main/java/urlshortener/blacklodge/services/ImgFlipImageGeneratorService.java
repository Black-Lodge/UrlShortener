package urlshortener.blacklodge.services;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import urlshortener.blacklodge.domain.MemeImageRequest;
import urlshortener.blacklodge.domain.MemeImageResponse;

@Service
public class ImgFlipImageGeneratorService implements MemeImageGeneratorService{
    
    private final static Logger LOGGER = LoggerFactory.getLogger(ImgFlipImageGeneratorService.class);
    
    private final static String IMGFLIP_URL = "https://api.imgflip.com/caption_image?";
    
    private final static String TEMPLATE_ID = "8072285";
    
    @Value("${ImgFlip.username}")
    private String username;
    
    @Value("${ImgFlip.password}")
    private String password;
    
    @Override
    public String generateImage(String noun, String adj) {
        
        MemeImageRequest mrequest = new MemeImageRequest();
        mrequest.setTemplate_id(TEMPLATE_ID);
        mrequest.setUsername(username);
        mrequest.setPassword(password);
        mrequest.setText0(noun);
        mrequest.setText1(adj);
        
        RestTemplate restTemplate = new RestTemplate();
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

            ResponseEntity<MemeImageResponse> response = restTemplate.exchange(IMGFLIP_URL+mrequest.toString(), HttpMethod.GET,entity,MemeImageResponse.class);

            return response.getBody().getData().getUrl();
        } catch (Exception e) {
            LOGGER.error("Generate meme image failed for Noun: {}, Adj: {}. Error: {}", noun, adj, e.getMessage());
            // TODO: Quiza lanzar una excepcion para que nuestro servicio entre en modo 404.
            return "https://i.imgflip.com/20tzw3.jpg";
        }
    }

}
