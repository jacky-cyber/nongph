package cn.globalph.b2c.payment.domain;

import cn.globalph.common.money.Money;
import cn.globalph.common.payment.PaymentTransactionType;
import cn.globalph.common.persistence.ArchiveStatus;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationMap;
import cn.globalph.common.presentation.AdminPresentationToOneLookup;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.common.presentation.override.AdminPresentationMergeEntry;
import cn.globalph.common.presentation.override.AdminPresentationMergeOverride;
import cn.globalph.common.presentation.override.AdminPresentationMergeOverrides;
import cn.globalph.common.presentation.override.PropertyType;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.MapKeyType;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "NPH_ORDER_PAYMENT_TRANSACTION")
@SQLDelete(sql="UPDATE NPH_ORDER_PAYMENT_TRANSACTION SET ARCHIVED = 'Y' WHERE PAYMENT_TRANSACTION_ID = ?")
@AdminPresentationMergeOverrides(
    {
        @AdminPresentationMergeOverride(name = "", mergeEntries =
            @AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.READONLY,
                                            booleanOverrideValue = true))
    }
)
public class PaymentTransactionImpl implements PaymentTransaction {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "PaymentTransactionId")
    @GenericGenerator(
        name="PaymentTransactionId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="PaymentTransactionImpl"),
            @Parameter(name="entity_name", value=" cn.globalph.b2c.payment.domain.PaymentTransactionImpl")
        }
    )
    @Column(name = "PAYMENT_TRANSACTION_ID")
    protected Long id;

    @Column(name = "TRANSACTION_TYPE")
    @AdminPresentation(friendlyName = "PaymentTransactionImpl_Type",
            fieldType = SupportedFieldType.BROADLEAF_ENUMERATION,
            broadleafEnumeration = "cn.globalph.common.payment.PaymentTransactionType",
            prominent = true, gridOrder = 1000)
    protected String type;

    @Column(name = "TRANSACTION_AMOUNT")
    @AdminPresentation(friendlyName = "PaymentTransactionImpl_Amount", fieldType = SupportedFieldType.MONEY,
        prominent = true, gridOrder = 2000)
    protected BigDecimal amount;

    @Column(name = "DATE_RECORDED")
    @AdminPresentation(friendlyName = "PaymentTransactionImpl_Date", prominent = true, gridOrder = 3000)
    protected Date date;
    
    @Column(name = "CUSTOMER_IP_ADDRESS", nullable = true)
    @AdminPresentation(friendlyName = "PaymentTransactionImpl_Payment_IP_Address", order=4000)
    protected String customerIpAddress;
    
    @Column(name = "RAW_RESPONSE", length = Integer.MAX_VALUE - 1)
    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    @AdminPresentation(friendlyName = "PaymentTransactionImpl_Raw_Response")
    protected String rawResponse;
    
    @Column(name = "SUCCESS")
    @AdminPresentation(friendlyName = "PaymentTransactionImpl_Success")
    protected Boolean success = true;
    
    @Embedded
    protected ArchiveStatus archiveStatus = new ArchiveStatus();
    
    @ManyToOne(targetEntity = OrderPaymentImpl.class, optional = false)
    @JoinColumn(name = "ORDER_PAYMENT")
    @AdminPresentation(excluded = true)
    protected OrderPayment orderPayment;
    
    /**
     * Necessary for operations on a payment that require something to have happened beforehand. For instance, an AUTHORIZE
     * would not have a parent but a CAPTURE must have an AUTHORIZE parent and a REFUND must have a CAPTURE parent
     */
    @ManyToOne(targetEntity = PaymentTransactionImpl.class)
    @JoinColumn(name = "PARENT_TRANSACTION")
    @AdminPresentation(friendlyName = "Parent Transaction")
    @AdminPresentationToOneLookup()
    protected PaymentTransaction parentTransaction;

    @ElementCollection
    @MapKeyColumn(name="FIELD_NAME")
    @MapKeyType(@Type(type = "java.lang.String"))
    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    @Column(name="FIELD_VALUE", length = Integer.MAX_VALUE - 1)
    @CollectionTable(name="NPH_TRANS_ADDITNL_FIELDS", joinColumns=@JoinColumn(name="PAYMENT_TRANSACTION_ID"))
    @BatchSize(size = 50)
    @AdminPresentationMap(friendlyName = "PaymentTransactionImpl_Additional_Fields",
        forceFreeFormKeys = true, keyPropertyFriendlyName = "PaymentTransactionImpl_Additional_Fields_Name"
    )
    protected Map<String, String> additionalFields = new HashMap<String, String>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public OrderPayment getOrderPayment() {
        if (orderPayment == null && parentTransaction != null) {
            return parentTransaction.getOrderPayment();
        }
        return orderPayment;
    }

    @Override
    public void setOrderPayment(OrderPayment orderPayment) {
        this.orderPayment = orderPayment;
    }
    
    @Override
    public PaymentTransaction getParentTransaction() {
        return parentTransaction;
    }

    @Override
    public void setParentTransaction(PaymentTransaction parentTransaction) {
        this.parentTransaction = parentTransaction;
    }

    @Override
    public PaymentTransactionType getType() {
        return PaymentTransactionType.getInstance(type);
    }

    @Override
    public void setType(PaymentTransactionType type) {
        this.type = (type == null) ? null : type.getType();
    }

    @Override
    public Money getAmount() {
        return amount == null ? Money.ZERO : new Money(amount);
    }

    @Override
    public void setAmount(Money amount) {
        if (amount != null) {
            this.amount = amount.getAmount();
        }
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
    }
    
    @Override
    public String getCustomerIpAddress() {
        return customerIpAddress;
    }

    @Override
    public void setCustomerIpAddress(String customerIpAddress) {
        this.customerIpAddress = customerIpAddress;
    }
    
    @Override
    public String getRawResponse() {
        return rawResponse;
    }
    
    @Override
    public void setRawResponse(String rawResponse) {
        this.rawResponse = rawResponse;
    }
    
    @Override
    public Boolean getSuccess() {
        return success;
    }

    @Override
    public void setSuccess(Boolean success) {
        this.success = success;
    }
    
    @Override
    public Map<String, String> getAdditionalFields() {
        return additionalFields;
    }

    @Override
    public void setAdditionalFields(Map<String, String> additionalFields) {
        this.additionalFields = additionalFields;
    }

    @Override
    public Character getArchived() {
        if (archiveStatus == null) {
            archiveStatus = new ArchiveStatus();
        }
        return archiveStatus.getArchived();
    }

    @Override
    public void setArchived(Character archived) {
        if (archiveStatus == null) {
            archiveStatus = new ArchiveStatus();
        }
        archiveStatus.setArchived(archived);
    }

    @Override
    public boolean isActive() {
        return 'Y' != getArchived();
    }


}
