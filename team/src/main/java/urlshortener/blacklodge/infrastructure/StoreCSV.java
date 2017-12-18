package urlshortener.blacklodge.infrastructure;

import org.springframework.web.multipart.MultipartFile;

public interface StoreCSV {

    String store(MultipartFile file);
}
