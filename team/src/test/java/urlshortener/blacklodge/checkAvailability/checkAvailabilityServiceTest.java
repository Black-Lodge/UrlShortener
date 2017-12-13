package urlshortener.blacklodge.checkAvailability;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import urlshortener.blacklodge.Application;
import urlshortener.blacklodge.services.CheckAvailabilityService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class checkAvailabilityServiceTest {

    @Autowired
    CheckAvailabilityService checkAvailabilityService;
    
    @Test
    public void checkSuccess() throws Exception {
        boolean checkResult = checkAvailabilityService.check("https://moodle2.unizar.es/add");
        assertEquals(true,checkResult);
        checkResult = checkAvailabilityService.check("http://mashable.com/ ");
        assertEquals(true,checkResult);
    }
    
    @Test
    public void checkFailure() throws Exception {
        boolean checkResult = checkAvailabilityService.check("http://examplefail.com");
        assertEquals(false,checkResult);
        checkResult = checkAvailabilityService.check("https://examplefailsecure.com");
        assertEquals(false,checkResult);
    }
}
