package cn.globalph.passport.domain;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.PopulateToOneFieldsEnum;
import cn.globalph.common.presentation.override.AdminPresentationMergeEntry;
import cn.globalph.common.presentation.override.AdminPresentationMergeOverride;
import cn.globalph.common.presentation.override.AdminPresentationMergeOverrides;
import cn.globalph.common.presentation.override.PropertyType;
import cn.globalph.common.time.domain.TemporalTimestampListener;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@EntityListeners(value = { TemporalTimestampListener.class })
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "NPH_CUSTOMER_PAYMENT", uniqueConstraints = @UniqueConstraint(name="CSTMR_PAY_UNIQUE_CNSTRNT", columnNames = {"CUSTOMER_ID", "PAYMENT_TOKEN"}))
@AdminPresentationMergeOverrides(
    {
        @AdminPresentationMergeOverride(name = "billingAddress.addressLine1", mergeEntries =
            @AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.PROMINENT, booleanOverrideValue = true))
    }
)
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE)
public class CustomerPaymentImpl implements CustomerPayment {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "CustomerPaymentId")
    @GenericGenerator(
        name="CustomerPaymentId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="CustomerPaymentImpl"),
            @Parameter(name="entity_name", value="cn.globalph.passport.domain.CustomerPaymentImpl")
        }
    )
    @Column(name = "CUSTOMER_PAYMENT_ID")
    protected Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = CustomerImpl.class, optional=false)
    @JoinColumn(name = "CUSTOMER_ID")
    @AdminPresentation(excluded = true)
    protected Customer customer;

    @Column(name = "PAYMENT_TOKEN")
    protected String paymentToken;

    @Column(name = "IS_DEFAULT")
    protected boolean isDefault = false;

    @ElementCollection
    @CollectionTable(name = "NPH_CUSTOMER_PAYMENT_FIELDS", joinColumns=@JoinColumn(name="CUSTOMER_PAYMENT_ID"))
    @MapKeyColumn(name = "FIELD_NAME", nullable = false)
    @Column(name="FIELD_VALUE")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blStandardElements")
    protected Map<String, String> additionalFields = new HashMap<String, String>();

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Customer getCustomer() {
        return customer;
    }

    @Override
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String getPaymentToken() {
        return paymentToken;
    }

    @Override
    public void setPaymentToken(String paymentToken) {
        this.paymentToken = paymentToken;
    }

    @Override
    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public void setDefault(boolean aDefault) {
        this.isDefault = aDefault;
    }

    @Override
    public Map<String, String> getAdditionalFields() {
        return additionalFields;
    }

    @Override
    public void setAdditionalFields(Map<String, String> additionalFields) {
        this.additionalFields = additionalFields;
    }
}
