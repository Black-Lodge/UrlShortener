package urlshortener.blacklodge.csv;

import static org.junit.Assert.assertEquals;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import urlshortener.blacklodge.Application;
import urlshortener.blacklodge.infrastructure.StoreCSV;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class StoreCSVTest {

    @Autowired
    StoreCSV storeCSV;
    @Test
    public void storeCSVTest() throws Exception {
        String filecontent = "https://google.com\nhttp://moodle.unizar.com\n";
        
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "multipart/form-data", filecontent.getBytes());
        String dir = storeCSV.store(multipartFile);
        
        byte[] encoded = Files.readAllBytes(Paths.get(dir));
        String filecontent2 = new String(encoded, Charset.defaultCharset());
        
        assertEquals(filecontent, filecontent2);
        
        Files.delete(Paths.get(dir));
    }
    
}