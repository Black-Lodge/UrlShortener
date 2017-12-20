package urlshortener.blacklodge.accessor;

import feign.Response;
import feign.codec.Decoder;
import org.springframework.context.annotation.Bean;

public class checkWordsConfiguration {
    @Bean
    Decoder feignDecoder() {
        return new checkWordsDecoder();
    }
}
