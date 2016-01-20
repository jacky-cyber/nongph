package cn.globalph.passport.web.core.form;

import java.io.Serializable;
import java.util.Date;

public class BindPhoneForm implements Serializable {
    protected static final long serialVersionUID = 1L;

    protected String phone;
    protected String bindCode;
    protected Date genTime;
    protected String redirectUrl;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBindCode() {
        return bindCode;
    }

    public void setBindCode(String bindCode) {
        this.bindCode = bindCode;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

}
