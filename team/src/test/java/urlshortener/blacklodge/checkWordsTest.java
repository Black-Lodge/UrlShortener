package urlshortener.blacklodge;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import urlshortener.blacklodge.checkWords.checkWords;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class checkWordsTest {

    @Test
    public void checkBadWord() {
        checkWords test = new checkWordsForTesting();
        assertEquals(true,test.check("fuck"));
    }

    @Test
    public void checkSafeWord() {
        checkWords test = new checkWordsForTesting();
        assertEquals(false,test.check("you"));
    }

    @Test
    public void checkBadURL() {
        checkWords test = new checkWordsForTesting();
        assertEquals(true,test.check("https://mydomain.com/fuck"));
    }

    @Test
    public void checkSafeURL() {
        checkWords test = new checkWordsForTesting();
        assertEquals(false,test.check("https://mydomain.com/hi/test"));
    }

    private static class checkWordsForTesting implements checkWords {
        public boolean check(String query) {
            return query.contains("fuck");
        }
    }
}
