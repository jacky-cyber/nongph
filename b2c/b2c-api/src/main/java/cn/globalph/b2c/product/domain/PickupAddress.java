package cn.globalph.b2c.product.domain;

import java.io.Serializable;

/**
 * @author steven
 * @since 7/16/15
 */
public interface PickupAddress extends Serializable {
    Long getId();

    void setId(Long id);

    String getName();

    void setName(String name);

    String getAddress();

    void setAddress(String address);

    String getSender();

    void setSender(String sender);

    String getPhone();

    void setPhone(String phone);

    String getRemark();

    void setRemark(String remark);

    Boolean isDefault();

    void setDefault(Boolean isDefault);

    Provider getProvider();

    void setProvider(Provider provider);
}
