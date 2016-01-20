package cn.globalph.b2c.order.service.exception;

/**
 * This runtime exception will be thrown when an attempt to add to cart without specifying
 * all required product options has been made.
 * 
 * @author apazzolini
 */
public class RequiredAttributeNotProvidedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    protected String attributeName;

    public RequiredAttributeNotProvidedException(String message, String attributeName) {
        super(message);
        setAttributeName(attributeName);
    }

    public RequiredAttributeNotProvidedException(String message, String attributeName, Throwable cause) {
        super(message, cause);
        setAttributeName(attributeName);
    }

    public RequiredAttributeNotProvidedException(String attributeName) {
        super("The attribute " + attributeName + " was not provided");
        setAttributeName(attributeName);
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }


}
