package urlshortener.blacklodge.routing;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.ProducerTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import urlshortener.blacklodge.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class routerTest {

    @Autowired
    ProducerTemplate producertemplate;
    @Test
    public void routinhTest() throws Exception {
        String filecontent = "https://google.com\nhttp://moodle.unizar.com\n";
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "multipart/form-data", filecontent.getBytes());
        
        Map<String, Object> headers = new HashMap<String,Object>(); 

        headers.put("sponsor", "q"); 
        headers.put("owner", "e"); 
        headers.put("ip", "e"); 

        producertemplate.sendBodyAndHeaders("direct:processCSV", multipartFile.getInputStream(), headers);
    }
    

}