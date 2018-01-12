package urlshortener.blacklodge.web;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.net.URI;
import java.nio.charset.Charset;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= RANDOM_PORT)
@DirtiesContext
public class csvUploadControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Ignore	
    @Test
    public void testCsvUpload() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "multipart/form-data", "https://google.com\nhttp://moodle.unizar.com".getBytes());
        ResponseEntity<String> entity = postLink(multipartFile);

        assertThat(entity.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(entity.getHeaders().getContentType(), is(new MediaType("application", "json", Charset.forName("UTF-8"))));
        ReadContext rc = JsonPath.parse(entity.getBody());
        assertThat(rc.read("$.shortURL[0].hash"), is("such_sole_so_unregretful"));
        assertThat(rc.read("$.shortURL[0].uri"), is("http://localhost:"+ this.port+"/such_sole_so_unregretful"));
        assertThat(rc.read("$.shortURL[0].target"), is("https://google.com"));
        assertThat(rc.read("$.shortURL[0].sponsor"), is(nullValue()));
    }

   /* @Test
    public void testCsvRedirection() throws Exception {
        postLink("http://example.com/");

        ResponseEntity<String> entity = restTemplate.getForEntity( "/f684a3c4", String.class);
        assertThat(entity.getStatusCode(), is(HttpStatus.TEMPORARY_REDIRECT));
        assertThat(entity.getHeaders().getLocation(), is(new URI("http://example.com/")));
    }*/

    private ResponseEntity<String> postLink(MockMultipartFile multipartFile) {
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("file", multipartFile);
        return restTemplate.postForEntity("/link", parts, String.class);
    }

}

