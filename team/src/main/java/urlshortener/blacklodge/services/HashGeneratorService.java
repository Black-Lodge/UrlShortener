package urlshortener.blacklodge.services;

import java.util.List;

public interface HashGeneratorService {
    List<Integer> hash(String url);
}
