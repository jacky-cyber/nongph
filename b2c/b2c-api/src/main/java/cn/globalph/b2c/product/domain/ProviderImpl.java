package cn.globalph.b2c.product.domain;

import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.AdminPresentationCollection;
import cn.globalph.common.presentation.PopulateToOneFieldsEnum;
import cn.globalph.common.presentation.client.VisibilityEnum;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Table;

import javax.persistence.*;
import javax.persistence.Entity;

import java.util.List;

/**
 * @author steven
 * @see {@link Product}
 */
@Entity
@javax.persistence.Table(name = "NPH_PROVIDER")
@org.hibernate.annotations.Table(appliesTo = "NPH_PROVIDER")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blProviders")
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "baseProvider")
//@SQLDelete(sql = "UPDATE NPH_PROVIDER SET ARCHIVED = 'Y' WHERE PROVIDER_ID = ?")
public class ProviderImpl implements Provider {

    private static final Log LOG = LogFactory.getLog(ProviderImpl.class);

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "ProviderId")
    @GenericGenerator(
            name = "ProviderId",
            strategy = "cn.globalph.common.persistence.IdOverrideTableGenerator",
            parameters = {
                    @Parameter(name = "segment_value", value = "ProviderImpl"),
                    @Parameter(name = "entity_name", value = "cn.globalph.b2c.product.domain.ProviderImpl")
            }
    )
    @Column(name = "PROVIDER_ID")
    @AdminPresentation(friendlyName = "ProviderImpl_Provider_ID",
            visibility = VisibilityEnum.HIDDEN_ALL)
    protected Long id;

    @Column(name = "PROVIDER_NAME")
    @AdminPresentation(friendlyName = "供应商名称",
            prominent = true,
            gridOrder = 1,
            columnWidth = "260px",
            translatable = true)
    protected String name;
    
    @Column(name = "PROVIDER_EN_NAME")
    @AdminPresentation(friendlyName = "供应商英文名称",
            prominent = true,
            gridOrder = 1,
            columnWidth = "260px",
            translatable = true)
    protected String englishName;
    
    @Column(name = "PROVIDER_EMAIL")
    @AdminPresentation(friendlyName = "供应商邮箱地址",
            prominent = true,
            gridOrder = 1,
            columnWidth = "260px",
            translatable = true)
    protected String email;

    @Column(name = "is_support_pickup", nullable = true)
    @AdminPresentation(friendlyName = "是否支持自提", prominent = true, order = 60)
    protected Boolean isSupportPickup;

    @OneToMany(mappedBy = "provider", targetEntity = PickupAddressImpl.class)
    @AdminPresentationCollection(friendlyName = "自提点")
    protected List<PickupAddress> pickupAddresses;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Boolean isSupportPickup() {
        return isSupportPickup;
    }

    @Override
    public void setSupportPickup(Boolean isSupportPickup) {
        this.isSupportPickup = isSupportPickup;
    }

    @Override
    public List<PickupAddress> getPickupAddresses() {
        return pickupAddresses;
    }

    @Override
    public void setPickupAddresses(List<PickupAddress> pickupAddresses) {
        this.pickupAddresses = pickupAddresses;
    }
  
    @Override
	public void setEnglishName(String englishName) {
		this.englishName = englishName;	
	}

	@Override
	public String getEnglishName() {
		return this.englishName;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getEmail() {
		return this.email;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProviderImpl provider = (ProviderImpl) o;

        return !(id != null ? !id.equals(provider.id) : provider.id != null)
                && !(name != null ? !name.equals(provider.name) : provider.name != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}