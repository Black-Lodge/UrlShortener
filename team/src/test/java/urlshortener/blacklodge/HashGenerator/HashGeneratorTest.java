package urlshortener.blacklodge.HashGenerator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import urlshortener.blacklodge.services.HashGeneratorService;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class HashGeneratorTest {
    private static HashGeneratorService function = new HashGeneratorforTesting();

    @Test
    public void checkHash() {
        Long list = function.hash("https://www.google.es");
        assertEquals(40,list.intValue());
    }



    private static class HashGeneratorforTesting implements HashGeneratorService {
        public Long hash (String url) {
            return (long) 40;
        }
    }

}
