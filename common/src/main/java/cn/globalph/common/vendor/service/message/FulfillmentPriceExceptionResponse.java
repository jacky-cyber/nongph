package cn.globalph.common.vendor.service.message;

import java.io.Serializable;

public interface FulfillmentPriceExceptionResponse extends Serializable {

    public boolean isErrorDetected();

    public void setErrorDetected(boolean isErrorDetected);

    public String getErrorCode();

    public void setErrorCode(String errorCode);

    public String getErrorText();

    public void setErrorText(String errorText);

}
