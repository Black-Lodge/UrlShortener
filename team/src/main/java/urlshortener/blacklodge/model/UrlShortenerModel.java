package urlshortener.blacklodge.model;

import urlshortener.common.domain.ShortURL;

public interface UrlShortenerModel {
    ShortURL shorten(String url);
}
