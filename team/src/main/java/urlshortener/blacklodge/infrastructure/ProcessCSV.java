package urlshortener.blacklodge.infrastructure;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ProcessCSV {
    
    List<String> processCSV(MultipartFile file);

}
