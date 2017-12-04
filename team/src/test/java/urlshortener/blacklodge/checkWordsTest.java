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
        checkWords test = new checkWords("fuck");
        assertEquals(true,test.check());
    }



    @Test
    public void checkBadURL() {
        checkWords test = new checkWords("https://mydomain.com/fuck");
        assertEquals(true,test.check());
    }


}
