package cn.globalph.b2c.order.service.call;


/**
 * ActivityMessageDTO will hold messages that will be  passed between activities/workflows.  As opposed to 
 * 
 * Created by priyesh patel.
 */
public class ActivityMessageDTO {


    protected String message;
    protected Integer priority;
    protected String type;
    protected String errorCode;

    public ActivityMessageDTO(String type, Integer priority, String message) {
        this.message = message;
        this.type = type;
        this.priority = priority;

    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}
