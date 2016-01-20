package cn.globalph.b2c.product.domain;

import cn.globalph.common.audit.Auditable;
import cn.globalph.common.audit.AuditableListener;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.PopulateToOneFieldsEnum;
import cn.globalph.common.presentation.client.VisibilityEnum;

import javax.persistence.*;

/**
 * @author steven
 * @since 7/16/15
 */
@Entity
@EntityListeners(value = {AuditableListener.class})
@Table(name = "ph_pickup_address")
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "ProductImpl_PickupAddress")
public class PickupAddressImpl implements PickupAddress {
    private static final long serialVersionUID = 1L;

    @Embedded
    protected Auditable auditable = new Auditable();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pickup_address_id")
    @AdminPresentation(friendlyName = "PickupAddressImpl_Id",
            visibility = VisibilityEnum.HIDDEN_ALL)
    protected Long id;

    @Column(name = "name", nullable = false)
    @AdminPresentation(friendlyName = "PickupAddressImpl_Name", prominent = true, order = 10)
    protected String name;

    @Column(name = "sender", nullable = false)
    @AdminPresentation(friendlyName = "PickupAddressImpl_Sender", prominent = true, order = 20)
    protected String sender;

    @Column(name = "phone", nullable = false)
    @AdminPresentation(friendlyName = "PickupAddressImpl_Phone", prominent = true, order = 30)
    protected String phone;

    @Column(name = "address", nullable = false)
    @AdminPresentation(friendlyName = "PickupAddressImpl_Address", prominent = true, order = 40)
    protected String address;

    @Column(name = "remark", nullable = false)
    @AdminPresentation(friendlyName = "PickupAddressImpl_Remark", prominent = true, order = 50)
    protected String remark;

    @Column(name = "is_default", nullable = false)
    @AdminPresentation(friendlyName = "PickupAddressImpl_IsDefault", prominent = true, order = 60)
    protected Boolean isDefault;

    @ManyToOne(targetEntity = ProviderImpl.class)
    @JoinColumn(name = "provider_id")
    @AdminPresentation(excluded = true)
    protected Provider provider;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean isDefault() {
        return isDefault;
    }

    public void setDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }
}
