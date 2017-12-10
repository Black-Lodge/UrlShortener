package urlshortener.blacklodge.SafeBrowsing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import urlshortener.blacklodge.Application;
import urlshortener.blacklodge.services.SafeBrowsingService;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SafeBrowsingServiceTest {

    @Autowired
    SafeBrowsingService safeBrowsingService;
    @Test
    public void lookupTest() throws Exception {
        boolean lookupResult = safeBrowsingService.checkSafetyUrl("https://www.google.es/");
        assertEquals(true,lookupResult);
    }
    
    @Test
    public void lookupFalseTest() throws Exception {
        boolean lookupResult = safeBrowsingService.checkSafetyUrl("http://malware.testing.google.test/testing/malware/");
        assertEquals(false,lookupResult);
    }
}