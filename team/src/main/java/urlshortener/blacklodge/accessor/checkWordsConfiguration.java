package urlshortener.blacklodge.accessor;

import feign.codec.Decoder;
import org.springframework.context.annotation.Bean;

/**
 * Class that contains a feign decoder for the offensive words API
 */
public class checkWordsConfiguration {

  @Bean
  Decoder feignDecoder() {
    return new checkWordsDecoder();
  }

}
