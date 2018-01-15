package urlshortener.blacklodge.csv;

/**
 * Class that represents a CSV response
 */
public class CsvResponse {

  String from;
  String to;
  boolean correct;
  String cause;

  /**
   * Full constructor
   * @param from original URL
   * @param to shortened URL
   * @param correct If everything went fine
   * @param cause Possible error
   */
  public CsvResponse(String from, String to, boolean correct, String cause) {
    super();
    this.from = from;
    this.to = to;
    this.correct = correct;
    this.cause = cause;
  }

  /**
   * Get from
   * @return URL from
   */
  public String getFrom() {
    return from;
  }

  /**
   * Set from
   * @param from URL from
   */
  public void setFrom(String from) {
    this.from = from;
  }

  /**
   * Get to
   * @return URL to
   */
  public String getTo() {
    return to;
  }

  /**
   * Set to
   * @param to URL to
   */
  public void setTo(String to) {
    this.to = to;
  }

  /**
   * Get correct
   * @return correct
   */
  public boolean isCorrect() {
    return correct;
  }

  /**
   * Set correct
   * @param correct correct
   */
  public void setCorrect(boolean correct) {
    this.correct = correct;
  }

  /**
   * Get cause
   * @return cause
   */
  public String getCause() {
    return cause;
  }

  /**
   * Set cause
   * @param cause cause
   */
  public void setCause(String cause) {
    this.cause = cause;
  }

}
