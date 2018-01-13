package urlshortener.blacklodge.infrastructure;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import urlshortener.blacklodge.model.UrlShortenerModel;
import urlshortener.common.domain.ShortURL;

@Component
public class CSVprocessor implements Processor {
    
    private static final Logger logger = LoggerFactory.getLogger(CSVprocessor.class);
    
    @Autowired
    UrlShortenerModel urlShortenermodel;
    
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
      } catch (Exception e){
          logger.error("Failed for csv line. Error: {}", e.getMessage());
      }
   }
}