package cn.globalph.passport.domain;

import javax.persistence.*;

/**
 * @author steven
 * @date 6/12/15
 */
@Entity
@Table(name = "NPH_WECHAT_CUSTOMER")
public class WechatCustomerImpl implements WechatCustomer {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "OPEN_ID")
    private String openId;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "is_from_rrm")
    private boolean isFromRRM = false;

    @Column(name = "IS_ACTIVE")
    private boolean isActive = true;

    @Column(name = "IS_PROPERTIES_LOADED")
    private boolean isPropertiesLoaded = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getOpenId() {
        return openId;
    }

    @Override
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public boolean isFromRRM() {
        return isFromRRM;
    }

    public void setFromRRM(boolean isFromRRM) {
        this.isFromRRM = isFromRRM;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isPropertiesLoaded() {
        return isPropertiesLoaded;
    }

    public void setPropertiesLoaded(boolean isPropertiesLoaded) {
        this.isPropertiesLoaded = isPropertiesLoaded;
    }
}
