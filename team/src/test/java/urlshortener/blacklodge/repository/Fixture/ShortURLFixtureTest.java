package urlshortener.blacklodge.repository.Fixture;

import urlshortener.common.domain.ShortURL;

/**
 * Class used to create some static URLs for testing purposes
 */
public class ShortURLFixtureTest{
    public static ShortURL url1() {
        return new ShortURL("1", "http://www.unizar.es/", null, null, null, null, null, false,
                null, null,"https://imgflip.com");
    }
    public static ShortURL url2() {
        return new ShortURL("2", "http://www.unizar.es/", null, null, null, null, null, false,
                null, null,"https://imgflip.com");
    }
}
