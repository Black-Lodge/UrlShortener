package urlshortener.blacklodge.domain;

/**
 * Class that represents data for a request to the Imgflip API
 */
public class MemeImageRequest {
    private String template_id;
    private String username;
    private String password;
    private String text0;
    private String text1;
    /** Get template_id
     * @return the template_id
     */
    public String getTemplate_id() {
        return template_id;
    }
    /** Set template_id
     * @param template_id the template_id to set
     */
    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }
    /** Get username
     * @return the username
     */
    public String getUsername() {
        return username;
    }
    /** Set username
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /** Get password
     * @return the password
     */
    public String getPassword() {
        return password;
    }
    /** Set password
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /** Get text0
     * @return the text0
     */
    public String getText0() {
        return text0;
    }
    /** Set text0
     * @param text0 the text0 to set
     */
    public void setText0(String text0) {
        this.text0 = text0;
    }
    /** Get text1
     * @return the text1
     */
    public String getText1() {
        return text1;
    }
    /** Set text1
     * @param text1 the text1 to set
     */
    public void setText1(String text1) {
        this.text1 = text1;
    }

    /**
     * Transform object to string
     * @return Data as string
     */
    @Override
    public String toString() {
        return "template_id=" + template_id + "&username=" + username + "&password=" + password
                + "&text0=" + text0 + "&text1=" + text1;
    }
    
    
}
