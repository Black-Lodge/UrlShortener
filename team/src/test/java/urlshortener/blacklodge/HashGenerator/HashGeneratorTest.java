package urlshortener.blacklodge.HashGenerator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import urlshortener.blacklodge.Application;
import urlshortener.blacklodge.services.HashGeneratorService;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests hash generator
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class HashGeneratorTest {
    //private static HashGeneratorService function = new HashGeneratorforTesting();
    @Autowired
    HashGeneratorService function;
    /**
     * Check that the hashing function is consistent
     */
    @Test
    public void checkHash() {
        Long list = function.hash("https://www.google.es");
        Long list2 = function.hash("https://www.google.es");
        assertEquals(list2.intValue(),list.intValue());
    }



    private static class HashGeneratorforTesting implements HashGeneratorService {
        public Long hash (String url) {
            return (long) 40;
        }
    }

}
