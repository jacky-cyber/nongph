package cn.globalph.b2c.order.domain;

import cn.globalph.b2c.product.domain.PickupAddress;
import cn.globalph.b2c.product.domain.PickupAddressImpl;
import cn.globalph.common.audit.Auditable;
import cn.globalph.common.audit.AuditableListener;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.PopulateToOneFieldsEnum;
import cn.globalph.common.presentation.client.VisibilityEnum;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;

@Entity
@EntityListeners(value = {AuditableListener.class})
@Table(name = "NPH_ORDER_ADDRESS")
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "OrderImpl_Address")
public class OrderAddressImpl implements OrderAddress {

    private static final long serialVersionUID = 1L;

    @Embedded
    protected Auditable auditable = new Auditable();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @AdminPresentation(friendlyName = "OrderAddressImpl_Id", group = "OrderImpl_Order_Address",
            visibility = VisibilityEnum.HIDDEN_ALL)
    protected Long id;

    @Column(name = "PROVINCE", nullable = false)
    @AdminPresentation(friendlyName = "OrderAddressImpl_Province", order = 3000,
            tab = "OrderImpl_Order_Address_Tab",
            group = "OrderImpl_Order_Address")
    protected String province;

    @Column(name = "CITY", nullable = false)
    @AdminPresentation(friendlyName = "OrderAddressImpl_City", order = 4000,
            tab = "OrderImpl_Order_Address_Tab", group = "OrderImpl_Order_Address")
    protected String city;

    @Column(name = "DISTRICT", nullable = false)
    @AdminPresentation(friendlyName = "OrderAddressImpl_District", order = 5000,
            tab = "OrderImpl_Order_Address_Tab", group = "OrderImpl_Order_Address")
    protected String district;

    @Column(name = "COMMUNITY", nullable = true)
    @AdminPresentation(friendlyName = "OrderAddressImpl_Community", order = 5001,
            tab = "OrderImpl_Order_Address_Tab", group = "OrderImpl_Order_Address")
    protected String community;

    @Column(name = "ADDRESS", nullable = false)
    @AdminPresentation(friendlyName = "OrderAddressImpl_Address", order = 6000,
            tab = "OrderImpl_Order_Address_Tab", group = "OrderImpl_Order_Address")
    protected String address;

    @Column(name = "POSTAL_CODE")
    @AdminPresentation(friendlyName = "OrderAddressImpl_Postal_Code", order = 7000,
            tab = "OrderImpl_Order_Address_Tab", group = "OrderImpl_Order_Address")
    protected String postalCode;

    @Column(name = "RECEIVER", nullable = false)
    @AdminPresentation(friendlyName = "OrderAddressImpl_Receiver", order = 1000,
            tab = "OrderImpl_Order_Address_Tab", group = "OrderImpl_Order_Address")
    protected String receiver;

    @Column(name = "PHONE", nullable = false)
    @AdminPresentation(friendlyName = "OrderAddressImpl_Phone", order = 2000,
            tab = "OrderImpl_Order_Address_Tab", group = "OrderImpl_Order_Address")
    protected String phone;

    @Column(name = "EX_NAME")
    @AdminPresentation(friendlyName = "OrderAddressImpl_Ex_Name", order = 8000,
            tab = "OrderImpl_Order_Address_Tab", group = "OrderImpl_Express")
    protected String exName;

    @Column(name = "EX_NO")
    @AdminPresentation(friendlyName = "OrderAddressImpl_Ex_No", order = 9000,
            tab = "OrderImpl_Order_Address_Tab", group = "OrderImpl_Express")
    protected String exNo;

    @Column(name = "delivery_type", nullable = true)
    @AdminPresentation(excluded = true)
    protected String deliveryType;

    @ManyToOne(targetEntity = PickupAddressImpl.class, optional = true)
    @JoinColumn(name = "pickup_address_id", nullable = true)
    @AdminPresentation(excluded = true)
    protected PickupAddress pickupAddress;

    @OneToOne(targetEntity = OrderImpl.class)
    @JoinColumn(name = "ORDER_ID")
    @AdminPresentation(excluded = true)
    protected Order order;

    public OrderAddressImpl() {
    }

    public OrderAddressImpl(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getProvince() {
        return this.province;
    }

    @Override
    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public String getCity() {
        return this.city;
    }

    @Override
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String getDistrict() {
        return this.district;
    }

    @Override
    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public String getCommunity() {
        return community;
    }

    @Override
    public void setCommunity(String community) {
        this.community = community;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getPostalCode() {
        return postalCode;
    }

    @Override
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getExName() {
        return exName;
    }

    public void setExName(String exName) {
        this.exName = exName;
    }

    public String getExNo() {
        return exNo;
    }

    public void setExNo(String exNo) {
        this.exNo = exNo;
    }

    public Auditable getAuditable() {
        return auditable;
    }

    public void setAuditable(Auditable auditable) {
        this.auditable = auditable;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public PickupAddress getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(PickupAddress pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    @Override
    public String getFullAddress() {
        return this.province + this.city + this.district + (StringUtils.isEmpty(this.community) ? "" : this.community) + this.address;
    }
}
