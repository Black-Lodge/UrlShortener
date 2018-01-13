package urlshortener.blacklodge.domain;

/**
 * Class that represents a response from the Imgflip API
 */
public class MemeImageResponse {
    private Boolean success;
    private Data data;
    /** Get success
     * @return the success
     */
    public Boolean getSuccess() {
        return success;
    }
    /** Set success
     * @param success the success to set
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }
    /** Get data
     * @return the data
     */
    public Data getData() {
        return data;
    }
    /** Set data
     * @param data the data to set
     */
    public void setData(Data data) {
        this.data = data;
    }
    
    
}
