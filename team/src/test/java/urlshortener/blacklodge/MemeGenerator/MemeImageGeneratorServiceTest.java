package urlshortener.blacklodge.MemeGenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import urlshortener.blacklodge.Application;
import urlshortener.blacklodge.services.MemeImageGeneratorService;

/**
 * Tests that the Imgflip API works as expected
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class MemeImageGeneratorServiceTest {
    
    @Autowired
    MemeImageGeneratorService memeImageGeneratorService;

    /**
     * Checks that a new image is generated, given a new order of the words
     */
    @Test
    public void generateMemeImage() {
        String image_url = memeImageGeneratorService.generateImage("lodge", "black");
        assertNotNull(image_url);
        assertNotEquals("https://i.imgflip.com/20tzw3.jpg", image_url);
    }
}
