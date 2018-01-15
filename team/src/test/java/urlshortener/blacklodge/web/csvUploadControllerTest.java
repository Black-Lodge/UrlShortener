package urlshortener.blacklodge.web;

import static org.junit.Assert.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import urlshortener.blacklodge.Application;
import urlshortener.blacklodge.repository.ShortURLRepo;


/**
 * Tests that the CSV controller works as expected
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= RANDOM_PORT, classes = Application.class)
@DirtiesContext
public class csvUploadControllerTest {


  @Autowired
  private WebApplicationContext webApplicationContext;

  @LocalServerPort
  private int port;

  @Autowired
  ShortURLRepo shortUrlRepository;

  /**
   * Integration test to verify that uploading a CSV with 2 URLs shortens those URLs
   * @throws Exception Exception from mockMvc
   */
  @Test
  public void testCsvUpload() throws Exception {
    String filecontent = "https://google.com\nhttp://moodle.unizar.com\n";
    MockMultipartFile multipartFile = new MockMultipartFile("csv", "test.txt",
            "multipart/form-data", filecontent.getBytes());

    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    mockMvc.perform(MockMvcRequestBuilders.fileUpload("/uploadFile/123")
            .file(multipartFile))
            .andExpect(status().is(201));

    assertEquals(2L,(long)shortUrlRepository.count());
  }


}

