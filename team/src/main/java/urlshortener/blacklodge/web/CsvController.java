package urlshortener.blacklodge.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import urlshortener.blacklodge.csv.CsvResponse;
import urlshortener.blacklodge.model.CsvUploadModel;


@RestController
public class CsvController {
  private static final Logger logger = LoggerFactory.getLogger(CsvController.class);

  @Autowired
  CsvUploadModel csv;

  @RequestMapping(value = "/uploadFile/{userkey}" , method = RequestMethod.POST)
  public ResponseEntity<String> submit(@PathVariable String userkey,@RequestParam("csv") MultipartFile file, HttpServletRequest request) {
    logger.info("Detected upload file userkey: "+userkey );
    csv.csvUpload(file, "Random", userkey,request.getRemoteAddr());
    logger.debug("CsvController ip: "+request.getRemoteAddr() );
    return new ResponseEntity<>("http://localhost:8080/csv/"+userkey+"/", HttpStatus.CREATED);
  }

  @RequestMapping(value = "/csv/{userkey}" , method = RequestMethod.GET)
  public ResponseEntity<List<CsvResponse>> get(@PathVariable String userkey, ModelMap modelMap) {
    logger.info("Detected upload file userkey: "+userkey );
    List<CsvResponse> f = csv.getResult(userkey);
    if (f != null) {
      return new ResponseEntity<>(f, HttpStatus.FOUND);
    }else {
      return new ResponseEntity<>((List<CsvResponse>) null, HttpStatus.BAD_REQUEST);
    }

  }

  @SubscribeMapping("/topic/uploadFile/{userkey}/")
  public void connectionOpened(@DestinationVariable String userkey) {
    logger.info("Detected subscription to csv with user "+userkey);

  }

}
