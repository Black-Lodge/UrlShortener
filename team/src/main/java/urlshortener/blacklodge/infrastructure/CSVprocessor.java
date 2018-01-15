package urlshortener.blacklodge.infrastructure;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import urlshortener.blacklodge.csv.CsvResponse;
import urlshortener.blacklodge.metrics.DiferentUsers;
import urlshortener.blacklodge.model.UrlShortenerModel;
import urlshortener.blacklodge.repository.ShortURLRepo;
import urlshortener.blacklodge.web.UrlShortenerControllerWithLogs;
import urlshortener.common.domain.ShortURL;

/**
 * Class that implements how the CSV files are processed
 */
@Component
public class CSVprocessor implements Processor {
  private static final Logger logger = LoggerFactory.getLogger(CSVprocessor.class);

  @Autowired
  UrlShortenerModel urlShortenermodel;

  @Autowired
  private SimpMessagingTemplate template;

  private final GaugeService gaugeService;
  ShortURLRepo sr;
  private DiferentUsers ips;

  @Autowired
  public CSVprocessor(final GaugeService gaugeService,ShortURLRepo sr, DiferentUsers ips) {
    this.sr = sr;
    this.gaugeService = gaugeService;
    this.ips = ips;
  }

  @Override
  /**
   * Processes a line of the CSV file
   */
  public void process(Exchange exchange) {
    List<String> url = exchange.getIn().getBody(List.class);
    String sponsor = (String) exchange.getIn().getHeader("sponsor");
    String owner = (String) exchange.getIn().getHeader("owner");
    String ip = (String) exchange.getIn().getHeader("ip");

    try {
      if (url.size() == 1 && url.get(0).matches("^(http|https)://[^\\s\\t]*$")) {
        //Time to respond the last petition
        long start = System.currentTimeMillis();
        ShortURL shortUrl = urlShortenermodel.shorten(url.get(0), sponsor, owner, ip);

        //logger.info(shortUrl.getHash());

        exchange.getIn().setBody(shortUrl);
        CsvResponse cr = new CsvResponse(url.get(0),
                linkTo(methodOn(UrlShortenerControllerWithLogs.class).redirectTo(shortUrl.getHash(),
                        null)).toString(),true,null);
        template.convertAndSend("/topic/uploadFile/"+owner+"/",cr);
        //Update actuator with the total urls saved
        this.gaugeService.submit("uris", sr.count().intValue());
        long end = System.currentTimeMillis()-start;
        this.gaugeService.submit("lastPetition", end);
        ips.add(ip);
        this.gaugeService.submit("users", ips.getNumber());
        logger.info("Ip de process: "+ ip);
        logger.info("Requested new short for uri " + url);

      } else {
        String ss = "";
        for (String s:url) {
          if (ss == "") {
            ss = s;
          } else {
            ss = ss + "," + s;
          }
        }

        CsvResponse cr = new CsvResponse(ss,
                null
                ,false
                ,"Bad format");
        template.convertAndSend("/topic/uploadFile/"+owner+"/",cr);
      }

    } catch (Exception e){
      logger.error("Failed for csv line. Error: {}", e.getMessage());
      CsvResponse cr = new CsvResponse(url.get(0),
              null
              ,false
              ,"URL not valid, safe or cannot connect");
      template.convertAndSend("/topic/uploadFile/"+owner+"/",cr);
    }
  }
  
}