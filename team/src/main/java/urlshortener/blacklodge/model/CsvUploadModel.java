package urlshortener.blacklodge.model;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import urlshortener.blacklodge.csv.CsvResponse;

/**
 * Interface for the csv management classes
 */
public interface CsvUploadModel {

  /**
   * Shortens the URLs on the file
   * @param file File where the URLs are stored
   * @param sponsor Ads
   * @param owner Owner of the request
   * @param ip IP from the request
   * @return Identifier of the shortened URLs
   */
  String csvUpload(MultipartFile file, String sponsor, String owner, String ip);

  /**
   * Returns the shortened URLs of a given owner
   * @param owner Owner of the shortened URLs
   * @return CSV with the shortened URLs
   */
  List<CsvResponse> getResult(String owner);

}
