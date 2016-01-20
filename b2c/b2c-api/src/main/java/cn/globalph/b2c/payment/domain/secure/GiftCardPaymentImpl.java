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
@Table(name = "NPH_GIFT_CARD_PAYMENT")
public class GiftCardPaymentImpl implements GiftCardPayment {

    private static final long serialVersionUID = 1L;

    /**
     * Rather than constructing directly, use {@link SecureOrderPaymentService#create( cn.globalph.b2c.payment.service.type.PaymentType)}
     * so that the appropriate {@link EncryptionModule} can be hooked up to this entity
     */
    protected GiftCardPaymentImpl() {
        // do not allow direct instantiation -- must at least be package private
        // for bytecode instrumentation
        // this complies with JPA specification requirements for entity
        // construction
    }

    @Transient
    protected EncryptionModule encryptionModule;

    @Id
    @GeneratedValue(generator = "GiftCardPaymentId")
    @GenericGenerator(
            name="GiftCardPaymentId",
            strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
            parameters = {
                @Parameter(name="segment_value", value="GiftCardPaymentImpl"),
                @Parameter(name="entity_name", value=" cn.globalph.b2c.payment.domain.GiftCardPaymentInfoImpl")
            }
        )
    @Column(name = "PAYMENT_ID")
    protected Long id;

    @Column(name = "REFERENCE_NUMBER", nullable = false)
    @Index(name="GIFTCARD_INDEX", columnNames={"REFERENCE_NUMBER"})
    protected String referenceNumber;

    @Column(name = "PAN", nullable = false)
    protected String pan;

    @Column(name = "PIN")
    protected String pin;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getPan() {
        return encryptionModule.decrypt(pan);
    }

    @Override
    public String getPin() {
        return encryptionModule.decrypt(pin);
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setPan(String pan) {
        this.pan = encryptionModule.encrypt(pan);
    }

    @Override
    public void setPin(String pin) {
        this.pin = encryptionModule.encrypt(pin);
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
        result = prime * result + ((pan == null) ? 0 : pan.hashCode());
        result = prime * result + ((pin == null) ? 0 : pin.hashCode());
        result = prime * result + ((referenceNumber == null) ? 0 : referenceNumber.hashCode());
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
        GiftCardPaymentImpl other = (GiftCardPaymentImpl) obj;

        if (id != null && other.id != null) {
            return id.equals(other.id);
        }

        if (pan == null) {
            if (other.pan != null)
                return false;
        } else if (!pan.equals(other.pan))
            return false;
        if (pin == null) {
            if (other.pin != null)
                return false;
        } else if (!pin.equals(other.pin))
            return false;
        if (referenceNumber == null) {
            if (other.referenceNumber != null)
                return false;
        } else if (!referenceNumber.equals(other.referenceNumber))
            return false;
        return true;
    }

}
