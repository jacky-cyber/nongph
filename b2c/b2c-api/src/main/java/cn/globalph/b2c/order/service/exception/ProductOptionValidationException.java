package cn.globalph.b2c.order.service.exception;

public class ProductOptionValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    protected String attributeName;
    protected String attributeValue;
    protected String validationString;
    protected String errorMessage;
    protected String errorCode;

    public ProductOptionValidationException() {
        super();
    }

    public ProductOptionValidationException(String message, String errorCode, String attributeName, String attributeValue, String validationString, String errorMessage, Throwable cause) {
        super(message, cause);
        setAttributeName(attributeName);
        setAttributeValue(attributeValue);
        setErrorMessage(errorMessage);
        setValidationString(validationString);
        setErrorCode(errorCode);
    }

    public ProductOptionValidationException(String message, String errorCode, String attributeName, String attributeValue, String validationString, String errorMessage) {
        super(message);
        setAttributeName(attributeName);
        setAttributeValue(attributeValue);
        setErrorMessage(errorMessage);
        setValidationString(validationString);
        setErrorCode(errorCode);
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }


    public String getValidationString() {
        return validationString;
    }

    public void setValidationString(String validationString) {
        this.validationString = validationString;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}
