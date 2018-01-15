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
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import urlshortener.blacklodge.domain.MemeImageRequest;
import urlshortener.blacklodge.domain.MemeImageResponse;

/**
 * Service that generated meme images using ImgFlip API
 */
@Service
public class ImgFlipImageGeneratorService implements MemeImageGeneratorService{

  private final static Logger logger = LoggerFactory.getLogger(ImgFlipImageGeneratorService.class);

  private final static String IMGFLIP_URL = "https://api.imgflip.com/caption_image?";

  private final static String TEMPLATE_ID = "8072285";

  @Value("${ImgFlip.username}")
  private String username;

  @Value("${ImgFlip.password}")
  private String password;

  /**
   * Generate a meme image
   * @param noun First word of the meme
   * @param adj Second word of the meme
   * @return URL of the meme image generated using ImgFlip API
   */
  @Override
  public String generateImage(String noun, String adj) {

    MemeImageRequest mrequest = new MemeImageRequest();
    mrequest.setTemplate_id(TEMPLATE_ID);
    mrequest.setUsername(username);
    mrequest.setPassword(password);
    mrequest.setText0("such "+noun);
    mrequest.setText1("so "+adj);

    RestTemplate restTemplate = new RestTemplate();

    try {
      restTemplate.setRequestFactory(new SimpleClientHttpRequestFactory());
      SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory) restTemplate
              .getRequestFactory();
      rf.setReadTimeout(10*1000);
      rf.setConnectTimeout(10*1000);

      HttpHeaders headers = new HttpHeaders();
      headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
      headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
      HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

      ResponseEntity<MemeImageResponse> response = restTemplate.exchange(IMGFLIP_URL+mrequest.toString(), HttpMethod.GET,entity,MemeImageResponse.class);

      return response.getBody().getData().getUrl();
    } catch (Exception e) {
      logger.error("Generate meme image failed for Noun: {}, Adj: {}. Error: {}", noun, adj, e.getMessage());
      return "https://i.imgflip.com/20tzw3.jpg";
    }
  }

}
