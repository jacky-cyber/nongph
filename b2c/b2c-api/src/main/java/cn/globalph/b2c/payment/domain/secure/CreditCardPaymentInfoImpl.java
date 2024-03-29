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
@Table(name = "NPH_CREDIT_CARD_PAYMENT")
public class CreditCardPaymentInfoImpl implements CreditCardPayment {

    private static final long serialVersionUID = 1L;

    /**
     * Rather than constructing directly, use {@link SecureOrderPaymentService#create( cn.globalph.b2c.payment.service.type.PaymentType)}
     * so that the appropriate {@link EncryptionModule} can be hooked up to this entity
     */
    protected CreditCardPaymentInfoImpl() {
        //do not allow direct instantiation -- must at least be package private for bytecode instrumentation
        //this complies with JPA specification requirements for entity construction
    }

    @Transient
    protected EncryptionModule encryptionModule;

    @Id
    @GeneratedValue(generator = "CreditCardPaymentId")
    @GenericGenerator(
            name="CreditCardPaymentId",
            strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
            parameters = {
                @Parameter(name="segment_value", value="CreditCardPaymentInfoImpl"),
                @Parameter(name="entity_name", value=" cn.globalph.b2c.payment.domain.CreditCardPaymentInfoImpl")
            }
        )
    @Column(name = "PAYMENT_ID")
    protected Long id;

    @Column(name = "REFERENCE_NUMBER", nullable=false)
    @Index(name="CREDITCARD_INDEX", columnNames={"REFERENCE_NUMBER"})
    protected String referenceNumber;

    @Column(name = "PAN", nullable=false)
    protected String pan;

    @Column(name = "EXPIRATION_MONTH", nullable=false)
    protected Integer expirationMonth;

    @Column(name = "EXPIRATION_YEAR", nullable=false)
    protected Integer expirationYear;

    @Column(name = "NAME_ON_CARD", nullable=false)
    protected String nameOnCard;

    @Transient
    protected String cvvCode;

    /* (non-Javadoc)
     * @see cn.globalph.profile.payment.secure.domain.CreditCardPaymentInfo#getId()
     */
    @Override
    public Long getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see cn.globalph.profile.payment.secure.domain.CreditCardPaymentInfo#setId(long)
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /* (non-Javadoc)
     * @see cn.globalph.profile.payment.secure.domain.CreditCardPaymentInfo#getReferenceNumber()
     */
    @Override
    public String getReferenceNumber() {
        return referenceNumber;
    }

    /* (non-Javadoc)
     * @see cn.globalph.profile.payment.secure.domain.CreditCardPaymentInfo#setReferenceNumber(java.lang.String)
     */
    @Override
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    /* (non-Javadoc)
     * @see cn.globalph.profile.payment.secure.domain.CreditCardPaymentInfo#getPan()
     */
    @Override
    public String getPan() {
        return encryptionModule.decrypt(pan);
    }

    /* (non-Javadoc)
     * @see cn.globalph.profile.payment.secure.domain.CreditCardPaymentInfo#setPan(java.lang.Long)
     */
    @Override
    public void setPan(String pan) {
        this.pan = encryptionModule.encrypt(pan);
    }

    /* (non-Javadoc)
     * @see cn.globalph.profile.payment.secure.domain.CreditCardPaymentInfo#getExpirationMonth()
     */
    @Override
    public Integer getExpirationMonth() {
        return expirationMonth;
    }

    /* (non-Javadoc)
     * @see cn.globalph.profile.payment.secure.domain.CreditCardPaymentInfo#setExpirationMonth(java.lang.Integer)
     */
    @Override
    public void setExpirationMonth(Integer expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    /* (non-Javadoc)
     * @see cn.globalph.profile.payment.secure.domain.CreditCardPaymentInfo#getExpirationYear()
     */
    @Override
    public Integer getExpirationYear() {
        return expirationYear;
    }

    /* (non-Javadoc)
     * @see cn.globalph.profile.payment.secure.domain.CreditCardPaymentInfo#setExpirationYear(java.lang.Integer)
     */
    @Override
    public void setExpirationYear(Integer expirationYear) {
        this.expirationYear = expirationYear;
    }

    /* (non-Javadoc)
    * @see cn.globalph.profile.payment.secure.domain.CreditCardPaymentInfo#getNameOnCard()
    */
    @Override
    public String getNameOnCard() {
        return nameOnCard;
    }

    /* (non-Javadoc)
    * @see cn.globalph.profile.payment.secure.domain.CreditCardPaymentInfo#setNameOnCard(java.lang.String)
    */
    @Override
    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    @Override
    public String getCvvCode() {
        return cvvCode;
    }

    @Override
    public void setCvvCode(String cvvCode) {
        this.cvvCode = cvvCode;
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
        result = prime * result + ((expirationMonth == null) ? 0 : expirationMonth.hashCode());
        result = prime * result + ((expirationYear == null) ? 0 : expirationYear.hashCode());
        result = prime * result + ((pan == null) ? 0 : pan.hashCode());
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
        CreditCardPaymentInfoImpl other = (CreditCardPaymentInfoImpl) obj;

        if (id != null && other.id != null) {
            return id.equals(other.id);
        }

        if (expirationMonth == null) {
            if (other.expirationMonth != null)
                return false;
        } else if (!expirationMonth.equals(other.expirationMonth))
            return false;
        if (expirationYear == null) {
            if (other.expirationYear != null)
                return false;
        } else if (!expirationYear.equals(other.expirationYear))
            return false;
        if (pan == null) {
            if (other.pan != null)
                return false;
        } else if (!pan.equals(other.pan))
            return false;
        if (referenceNumber == null) {
            if (other.referenceNumber != null)
                return false;
        } else if (!referenceNumber.equals(other.referenceNumber))
            return false;
        return true;
    }

}
