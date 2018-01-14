package urlshortener.blacklodge.model;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import urlshortener.blacklodge.csv.CsvResponse;
import urlshortener.common.domain.ShortURL;

public interface CsvUploadModel {
    
    String csvUpload(MultipartFile file, String sponsor, String owner, String ip);
    
    List<CsvResponse> getResult(String owner);
    
}
