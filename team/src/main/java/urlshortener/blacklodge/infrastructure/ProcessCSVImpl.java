package urlshortener.blacklodge.infrastructure;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Process CSV multipartfile
 *
 */
@Component
public class ProcessCSVImpl implements ProcessCSV {
    
    private final static Logger logger = LoggerFactory.getLogger(ProcessCSVImpl.class);
    
    @Override
    public List<String> processCSV(MultipartFile file) {
        
        List<String> urls = new ArrayList<String>();
        
        try {
            // Get an InputStream to read the contents of the file from.
            InputStream is = file.getInputStream();
            Scanner s = new Scanner(is);
            while (s.hasNext()) {
                urls.add(s.next());
            }
                      
            s.close();
            is.close();
            return urls;
        } catch (IOException e) {
            logger.error("Process CSV failed for CSV file {}. Error: {}", file.getName(), e.getMessage());
            return null;
        } 
    }
}
