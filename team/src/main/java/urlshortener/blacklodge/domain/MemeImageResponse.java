package urlshortener.blacklodge.domain;

public class MemeImageResponse {
    private Boolean success;
    private Data data;
    /**
     * @return the success
     */
    public Boolean getSuccess() {
        return success;
    }
    /**
     * @param success the success to set
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }
    /**
     * @return the data
     */
    public Data getData() {
        return data;
    }
    /**
     * @param data the data to set
     */
    public void setData(Data data) {
        this.data = data;
    }
    
    
}
