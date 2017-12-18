package urlshortener.blacklodge.infrastructure;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class StoreCSVImpl implements StoreCSV {
    
    private static final Logger logger = LoggerFactory.getLogger(StoreCSVImpl.class);
    
    @Override
    public String store(MultipartFile file) {
        
        String dir = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + 
                + System.currentTimeMillis();
        try {
            File tmpFile = new File(dir);
            file.transferTo(tmpFile);
            return dir;
        } catch (IllegalStateException | IOException e) {
            logger.error("StoreCSV failed for file {}. Error {}.", file.getOriginalFilename(), e.getMessage());
            return null;
        }
    }
    
}
