package urlshortener.blacklodge.csv;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import urlshortener.blacklodge.Application;
import urlshortener.blacklodge.infrastructure.ProcessCSV;

import static org.junit.Assert.assertEquals;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ProcessCSVTest {

    @Autowired
    ProcessCSV processCSV;
    @Test
    public void processCSVTest() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "multipart/form-data", "https://google.com\nhttp://moodle.unizar.com".getBytes());
        List<String> urls = processCSV.processCSV(multipartFile);
        assertEquals(2, urls.size());
        assertEquals("https://google.com", urls.get(0));
        assertEquals("http://moodle.unizar.com", urls.get(1));
    }
    
}