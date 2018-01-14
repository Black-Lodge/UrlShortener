package urlshortener.blacklodge.checkAvailability;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import urlshortener.blacklodge.Application;
import urlshortener.blacklodge.services.CheckAvailabilityService;

/**
 * Tests the Availability Service
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class checkAvailabilityServiceTest {

    @Autowired
    CheckAvailabilityService checkAvailabilityService;

    /**
     * Checks if it works as intended with websites that are known to be online, both http and https
     */
    @Test
    public void checkSuccess() {
        boolean checkResult = checkAvailabilityService.check("https://moodle2.unizar.es/add");
        assertEquals(true,checkResult);
        checkResult = checkAvailabilityService.check("http://mashable.com/ ");
        assertEquals(true,checkResult);
    }

    /**
     * Checks if it works as intended with websites that are known to be offline, both http and https
     */
    @Test
    public void checkFailure() {
        boolean checkResult = checkAvailabilityService.check("http://examplefail.com");
        assertEquals(false,checkResult);
        checkResult = checkAvailabilityService.check("https://examplefailsecure.com");
        assertEquals(false,checkResult);
    }
}
