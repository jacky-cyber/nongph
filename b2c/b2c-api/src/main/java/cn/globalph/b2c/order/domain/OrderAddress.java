package cn.globalph.b2c.order.domain;

import cn.globalph.b2c.product.domain.PickupAddress;

import java.io.Serializable;

/**
 * @author steven.wang
 */
public interface OrderAddress extends Serializable {

    Long getId();

    void setId(Long id);

    String getProvince();

    void setProvince(String province);

    String getCity();

    void setCity(String city);

    String getDistrict();

    void setDistrict(String district);

    String getCommunity();

    void setCommunity(String community);

    String getAddress();

    void setAddress(String address);

    String getPostalCode();

    void setPostalCode(String postalCode);

    String getReceiver();

    void setReceiver(String receiverName);

    String getPhone();

    void setPhone(String phone);

    Order getOrder();

    void setOrder(Order order);

    String getExName();

    void setExName(String exName);

    String getExNo();

    void setExNo(String exNo);

    String getDeliveryType();

    void setDeliveryType(String deliveryType);

    PickupAddress getPickupAddress();

    void setPickupAddress(PickupAddress pickupAddress);

    String getFullAddress();
}
