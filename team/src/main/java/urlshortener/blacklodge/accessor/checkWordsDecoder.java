package urlshortener.blacklodge.accessor;

import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

/**
 * Class that implements the feign decoder
 */
public class checkWordsDecoder implements Decoder {

  @Override
  public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
    String aux = new BufferedReader(new InputStreamReader(response.body().asInputStream())).readLine();
    return Boolean.valueOf(aux);
  }

}
