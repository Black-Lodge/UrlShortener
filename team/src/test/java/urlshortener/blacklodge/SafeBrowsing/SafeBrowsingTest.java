package urlshortener.blacklodge.SafeBrowsing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SafeBrowsingTest {

    @Autowired
    SafeBrowsing safeBrowsing;
    @Test
    public void lookupTest() throws Exception {
        boolean lookupResult = safeBrowsing.lookupURL("https://www.google.es/");
        assertEquals(true,lookupResult);
    }

}