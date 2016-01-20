package cn.globalph.b2c.payment.domain.secure;

import cn.globalph.b2c.payment.service.SecureOrderPaymentService;
import cn.globalph.common.encryption.EncryptionModule;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "NPH_BANK_ACCOUNT_PAYMENT")
public class BankAccountPaymentImpl implements BankAccountPayment {

    private static final long serialVersionUID = 1L;

    /**
     * Rather than constructing directly, use {@link SecureOrderPaymentService#create( cn.globalph.b2c.payment.service.type.PaymentType)}
     * so that the appropriate {@link EncryptionModule} can be hooked up to this entity
     */
    protected BankAccountPaymentImpl() {
        //do not allow direct instantiation -- must at least be package private for bytecode instrumentation
        //this complies with JPA specification requirements for entity construction
    }

    @Transient
    protected EncryptionModule encryptionModule;

    @Id
    @GeneratedValue(generator = "BankPaymentId")
    @GenericGenerator(
            name="BankPaymentId",
            strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
            parameters = {
                @Parameter(name="segment_value", value="BankAccountPaymentImpl"),
                @Parameter(name="entity_name", value=" cn.globalph.b2c.payment.domain.BankAccountPaymentInfoImpl")
            }
        )
    @Column(name = "PAYMENT_ID")
    protected Long id;

    @Column(name = "REFERENCE_NUMBER", nullable=false)
    @Index(name="BANKACCOUNT_INDEX", columnNames={"REFERENCE_NUMBER"})
    protected String referenceNumber;

    @Column(name = "ACCOUNT_NUMBER", nullable=false)
    protected String accountNumber;

    @Column(name = "ROUTING_NUMBER", nullable=false)
    protected String routingNumber;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getReferenceNumber() {
        return referenceNumber;
    }

    @Override
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    @Override
    public String getAccountNumber() {
        return encryptionModule.decrypt(accountNumber);
    }

    @Override
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = encryptionModule.encrypt(accountNumber);
    }

    @Override
    public String getRoutingNumber() {
        return encryptionModule.decrypt(routingNumber);
    }

    @Override
    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = encryptionModule.encrypt(routingNumber);
    }

    @Override
    public EncryptionModule getEncryptionModule() {
        return encryptionModule;
    }

    @Override
    public void setEncryptionModule(EncryptionModule encryptionModule) {
        this.encryptionModule = encryptionModule;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accountNumber == null) ? 0 : accountNumber.hashCode());
        result = prime * result + ((referenceNumber == null) ? 0 : referenceNumber.hashCode());
        result = prime * result + ((routingNumber == null) ? 0 : routingNumber.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BankAccountPaymentImpl other = (BankAccountPaymentImpl) obj;

        if (id != null && other.id != null) {
            return id.equals(other.id);
        }

        if (accountNumber == null) {
            if (other.accountNumber != null)
                return false;
        } else if (!accountNumber.equals(other.accountNumber))
            return false;
        if (referenceNumber == null) {
            if (other.referenceNumber != null)
                return false;
        } else if (!referenceNumber.equals(other.referenceNumber))
            return false;
        if (routingNumber == null) {
            if (other.routingNumber != null)
                return false;
        } else if (!routingNumber.equals(other.routingNumber))
            return false;
        return true;
    }

}
