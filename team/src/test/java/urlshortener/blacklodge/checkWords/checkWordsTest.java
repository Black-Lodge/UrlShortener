package urlshortener.blacklodge.checkWords;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import urlshortener.blacklodge.services.CheckWordsService;
import urlshortener.blacklodge.services.CheckWordsServiceImpl;

import static org.junit.Assert.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= RANDOM_PORT)
public class checkWordsTest {

    @Autowired
    CheckWordsService checkWordsService;

    @Test
    public void checkBadWord() {
        CheckWordsService test = new checkWordsForTesting();
        assertEquals(true,test.check("fuck"));
    }

    @Test
    public void checkSafeWord() {
        CheckWordsService test = new checkWordsForTesting();
        assertEquals(false,test.check("you"));
    }

    @Test
    public void checkBadURL() {
        CheckWordsService test = new checkWordsForTesting();
        assertEquals(true,test.check("https://mydomain.com/fuck"));
    }

    @Test
    public void checkSafeURL() {
        CheckWordsService test = new checkWordsForTesting();
        assertEquals(false,test.check("https://mydomain.com/hi/test"));
    }

    @Test
    public void checkRealSafe() {
        assertEquals(false,checkWordsService.check("you"));
    }

    @Test
    public void checkRealWrong() {
        assertEquals(true,checkWordsService.check("fuck"));
    }


    private static class checkWordsForTesting implements CheckWordsService {
        public boolean check(String query) {
            return query.contains("fuck");
        }
    }
}
