package urlshortener.blacklodge.MemeGenerator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import urlshortener.blacklodge.Application;
import urlshortener.blacklodge.services.MemeImageGeneratorService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class MemeImageGeneratorServiceTest {
    
    @Autowired
    MemeImageGeneratorService memeImageGeneratorService;
    
    @Test
    public void generateMemeImage() {
        String image_url = memeImageGeneratorService.generateImage("lodge", "black");
        
        // Service 
        assertEquals("https://i.imgflip.com/20tzw3.jpg", image_url);
    }
}
