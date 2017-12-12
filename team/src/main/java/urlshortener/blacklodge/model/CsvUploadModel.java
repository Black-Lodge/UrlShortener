package urlshortener.blacklodge.model;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import urlshortener.common.domain.ShortURL;

public interface CsvUploadModel {
    
    List<ShortURL> csvUpload(MultipartFile file, String sponsor, String owner, String ip);
    
}
