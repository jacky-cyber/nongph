package cn.globalph.passport.domain;

import java.io.Serializable;

/**
 * @author steven
 * @date 6/12/15
 */
public interface WechatCustomer extends Serializable {
    Long getId();

    void setId(Long id);

    Long getCustomerId();

    void setCustomerId(Long customerId);

    String getOpenId();

    void setOpenId(String openId);

    boolean isFromRRM();

    void setFromRRM(boolean isFromRRM);

    boolean isActive();

    void setActive(boolean isActive);

    boolean isPropertiesLoaded();

    void setPropertiesLoaded(boolean isPropertiesLoaded);
}
