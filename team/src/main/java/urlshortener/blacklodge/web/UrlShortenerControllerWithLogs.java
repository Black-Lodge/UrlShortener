package urlshortener.blacklodge.web;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import urlshortener.blacklodge.metrics.DiferentUsers;
import urlshortener.blacklodge.model.UrlShortenerModel;
import urlshortener.common.domain.ShortURL;
import urlshortener.common.web.UrlShortenerController;

/**
 * Class that implements the main controller, a rest controller
 */
@RestController
public class UrlShortenerControllerWithLogs extends UrlShortenerController {

  private static final Logger logger = LoggerFactory.getLogger(UrlShortenerControllerWithLogs.class);


  private DiferentUsers ips;

  private final GaugeService gaugeService;

  @Autowired
  UrlShortenerModel urlShortenerModel;

  /**
   * Default constructor
   * @param gaugeService Metric service
   * @param ips Different ips used
   */
  @Autowired
  public UrlShortenerControllerWithLogs(final GaugeService gaugeService, DiferentUsers ips) {
    this.gaugeService = gaugeService;
    this.ips = ips;

  }

  /**
   * Class that returns the original URL given its hash
   * @param id hash
   * @param request request
   * @return Original URL
   */
  @Override
  @RequestMapping(value = "/{id:(?!link|index).*}", method = RequestMethod.GET)
  public ResponseEntity<?> redirectTo(@PathVariable String id, HttpServletRequest request) {
    //Time to respond the last redirect
    long start = System.currentTimeMillis();

    logger.info("Requested redirection with hash " + id);

    // chequea si el url almacenada es safe en el momento o no
    ResponseEntity<?> a = super.redirectTo(id, request);

    //Update actuator with the click counts
    this.gaugeService.submit("clicks",clickRepository.count().intValue());
    //Update actuator with total users with diferent ip
    ips.add(extractIP(request));
    logger.info("Ip de redirectoto: "+extractIP(request));
    this.gaugeService.submit("users", ips.getNumber());

    long end = System.currentTimeMillis()-start;
    this.gaugeService.submit("lastRedirection", end);
    switch (a.getStatusCode()) {
      case TEMPORARY_REDIRECT:
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/ads/"+id);
        return new ResponseEntity<String>(headers,HttpStatus.TEMPORARY_REDIRECT);
      default:
        return a;
    }
  }

  /**
   * Extracts IP from request
   * @param request
   * @return IP
   */
  private String extractIP(HttpServletRequest request) {
    return request.getRemoteAddr();
  }

  /**
   * Method that shortens an URL
   * @param url url to shorten
   * @param sponsor ads
   * @param request request
   * @return shortened URL
   */
  @Override
  public ResponseEntity<ShortURL> shortener(@RequestParam("url") String url,
                                            @RequestParam(value = "sponsor", required = false) String sponsor,
                                            HttpServletRequest request) {

    //Time to respond the last petition
    long start = System.currentTimeMillis();

    logger.info("Requested new short for uri " + url);
    //ResponseEntity<ShortURL> a = super.shortener(url, sponsor, request);


    ShortURL su = urlShortenerModel.shorten(url, sponsor, UUID
            .randomUUID().toString(), extractIP(request));

    //Update actuator with the total urls saved
    this.gaugeService.submit("uris", shortURLRepository.count().intValue());
    //Update actuator with total users with diferent ip
    ips.add(extractIP(request));
    this.gaugeService.submit("users", ips.getNumber());

    long end = System.currentTimeMillis()-start;
    this.gaugeService.submit("lastPetition", end);

    if (su != null) {
      HttpHeaders h = new HttpHeaders();
      h.setLocation(su.getUri());
      return new ResponseEntity<>(su, h, HttpStatus.CREATED);
    } else {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

}
