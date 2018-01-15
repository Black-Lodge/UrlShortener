package urlshortener.blacklodge.ads;

/**
 * Class that presents a response related to an ad
 */
public class AdsResponse {

  int countdown;
  String url;

  public AdsResponse(int countdown,String url) {
    this.countdown = countdown;
    this.url = url;
  }

  /**
   * Get countdown
   * @return countdown
   */
  public int getCountdown() {
    return countdown;
  }

  /**
   * Set countdown
   * @param countdown countdown
   */
  public void setCountdown(int countdown) {
    this.countdown = countdown;
  }

  /**
   * Get Url
   * @return URL
   */
  public String getUrl() {
    return url;
  }

  /**
   * Set Url
   * @param url URL
   */
  public void setUrl(String url) {
    this.url = url;
  }

}
