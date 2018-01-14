package urlshortener.blacklodge.infrastructure;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import urlshortener.blacklodge.ads.AdsResponse;
import urlshortener.blacklodge.csv.CsvResponse;
import urlshortener.blacklodge.model.UrlShortenerModel;
import urlshortener.blacklodge.web.UrlShortenerControllerWithLogs;
import urlshortener.common.domain.ShortURL;

@Component
public class CSVprocessor implements Processor {
    
    private static final Logger logger = LoggerFactory.getLogger(CSVprocessor.class);
    
    @Autowired
    UrlShortenerModel urlShortenermodel;
    
    @Autowired
    private SimpMessagingTemplate template;
    
    @Override
    public void process(Exchange exchange) {
      try {
          List<String> url = exchange.getIn().getBody(List.class);
          
          // if url.size == 1 and pattern.match("http://")
          String sponsor = (String) exchange.getIn().getHeader("sponsor");
          String owner = (String) exchange.getIn().getHeader("owner");
          String ip = (String) exchange.getIn().getHeader("ip");
          
          ShortURL shortUrl = urlShortenermodel.shorten(url.get(0), sponsor, owner, ip);
          
          logger.info(shortUrl.getHash());
    
          exchange.getIn().setBody(shortUrl);
          CsvResponse cr = new CsvResponse(url.get(0),
        		  linkTo(methodOn(UrlShortenerControllerWithLogs.class).redirectTo(shortUrl.getHash(), null)).toString()
        		  ,true
        		  ,null);
          template.convertAndSend("/topic/uploadFile/"+owner+"/",cr);
      } catch (Exception e){
          logger.error("Failed for csv line. Error: {}", e.getMessage());
      }
   }
}