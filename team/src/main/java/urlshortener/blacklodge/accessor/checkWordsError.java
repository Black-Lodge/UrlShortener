package urlshortener.blacklodge.accessor;

import org.springframework.stereotype.Component;

@Component
public class checkWordsError implements CheckWordsAccesor {
    @Override
    public Boolean check(String word) {
        return true;
    }
}
