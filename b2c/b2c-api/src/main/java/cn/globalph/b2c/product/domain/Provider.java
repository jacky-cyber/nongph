package cn.globalph.b2c.product.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author steven
 * @since 6/25/15
 */
public interface Provider extends Serializable {

    Long getId();

    void setId(Long id);

    String getName();

    void setEnglishName(String englishName);
    
    String getEnglishName();

    void setEmail(String email);
    
    String getEmail();

    void setName(String name);

    Boolean isSupportPickup();

    void setSupportPickup(Boolean isSupportPickup);

    List<PickupAddress> getPickupAddresses();

    void setPickupAddresses(List<PickupAddress> pickupAddresses);
}
