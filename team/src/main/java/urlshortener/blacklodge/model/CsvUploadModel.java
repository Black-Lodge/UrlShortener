package urlshortener.blacklodge.model;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface CsvUploadModel {
    
    List<String> csvUpload(MultipartFile file);
    
}
