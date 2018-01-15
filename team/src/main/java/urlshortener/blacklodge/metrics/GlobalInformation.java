package urlshortener.blacklodge.metrics;

/**
 * Simple class that contains 
 * the data that needs to be send to 
 * the client in the metrics.
 *
 */
public class GlobalInformation {

  private int time;
  private int users;
  private int uris;
  private int clicks;
  private int lastRedirection;
  private int lastPetition;
  private int used;
  private int available;

  public GlobalInformation(int time, int users, int uris, int clicks, int lastRedirection, int lastPetition, int used,
                           int avaible) {

    this.time = time;
    this.users = users;
    this.uris = uris;
    this.clicks = clicks;
    this.lastRedirection = lastRedirection;
    this.lastPetition = lastPetition;
    this.used = used;
    this.available = avaible;
  }

  /**
   * Get time online
   * @return time online
   */
  public int getTime() {
    return time;
  }

  /**
   * Set time online
   * @param time time online
   */
  public void setTime(int time) {
    this.time = time;
  }

  /**
   * Get number of users
   * @return number of users
   */
  public int getUsers() {
    return users;
  }

  /**
   * Set number of users
   * @param users number of users
   */
  public void setUsers(int users) {
    this.users = users;
  }

  /**
   * Get number of shorten uris
   * @return number of shorten uris
   */
  public int getUris() {
    return uris;
  }

  /**
   * Set number of shorten Uris
   * @param uris number of shorten Uris
   */
  public void setUris(int uris) {
    this.uris = uris;
  }

  /**
   * Get number of clicks
   * @return number of clicks
   */
  public int getClicks() {
    return clicks;
  }

  /**
   * Set number of clicks
   * @param clicks number of clicks
   */
  public void setClicks(int clicks) {
    this.clicks = clicks;
  }

  /**
   * Get the the number of milliseconds that took the last redirection
   * @return milliseconds of the last redirection
   */
  public int getLastRedirection() {
    return lastRedirection;
  }

  /**
   * Set milliseconds of the last redirection
   * @param lastRedirection milliseconds of the last redirection
   */
  public void setLastRedirection(int lastRedirection) {
    this.lastRedirection = lastRedirection;
  }

  /**
   * Get the last petition
   * @return the number of the last petition
   */
  public int getLastPetition() {
    return lastPetition;
  }

  /**
   * Set the last petition
   * @param lastPetition last petition
   */
  public void setLastPetition(int lastPetition) {
    this.lastPetition = lastPetition;
  }

  /**
   * Get hte memory that it is used
   * @return memory used
   */
  public int getUsed() {
    return used;
  }

  /**
   * Set the memory that it is used
   * @param used memory used
   */
  public void setUsed(int used) {
    this.used = used;
  }

  /**
   * Get free memory
   * @return free memory
   */
  public int getAvailable() {
    return available;
  }

  /**
   * Set free memory
   * @param available free memory
   */
  public void setAvailable(int available) {
    this.available = available;
  }

  /**
   * Get metrics in JSON format
   * @return metris in JSON
   */
  public String getJSON() {
    String json ="{"
            +"\"time\":"+getTime()
            +",\"users\":"+getUsers()
            +",\"uris\":"+getUris()
            +",\"clicks\":"+getClicks()
            +",\"lastRedirection\":"+getLastRedirection()
            +",\"lastPetition\":"+getLastPetition()
            +",\"used\":"+getUsed()
            +",\"avaible\":"+getAvailable()

            +"}"
            ;

    return json;
  }

}
