package cn.globalph.b2c.payment.domain.secure;

import cn.globalph.b2c.payment.domain.OrderPayment;
import cn.globalph.b2c.payment.service.SecureOrderPaymentService;
import cn.globalph.common.encryption.EncryptionModule;

import java.io.Serializable;

import javax.annotation.Nonnull;

/**
 * <p>The main interface used to store extra-secure data such as credit card, bank accounts and gift card data. All entities
 * that implement this interface should be stored in a completely separate database under strict PCI compliance. Broadleaf
 * provides the ability for this in the blSecurePU persistence unit, which all implementing entities are members of.</p>
 *
 * <p>Entities that implement this {@link Referenced} interface should not be instantiated directly but rather be instaniated
 * via {@link SecureOrderPaymentService#create( cn.globalph.b2c.payment.service.type.PaymentType)}</p>
 * 
 * <p>In the common case, this is rarely used as most implementors will NOT want to deal with the liability and extra PCI
 * requirements associated with storing sensitive payment data. Consider integrating with a payment provider that takes
 * care of PCI-sensitive data instead.</p>
 *
 * @see {@link CreditCardPayment}
 * @see {@link GiftCardPayment}
 * @see {@link BankAccountPayment}
 * @author Phillip Verheyden (phillipuniverse)
 */
public interface Referenced extends Serializable {

    public Long getId();
    
    public void setId(Long id);
    
    /**
     * <p>The indirect link between non-secure data and the secure data represented here. Since implementing entities
     * should be in a separate persistence unit (blSecurePU), this is the only avenue to show a relationship between the two.</p>
     * 
     * <p>From the {@link Order} side of the domain, this is linked by {@link OrderPayment#getReferenceNumber()} on the
     * {@link OrderPayment} entity.</p>
     * 
     * @see {@link OrderPayment#getReferenceNumber()}
     */
    public String getReferenceNumber();

    /**
     * Set the link between this secure entity and the {@link OrderPayment}. This should not be null as this is required
     * @param referenceNumber
     */
    public void setReferenceNumber(@Nonnull String referenceNumber);

    /**
     * @return the {@link EncryptionModule} used to encrypt and decrypt this secure information back and forth
     */
    public EncryptionModule getEncryptionModule();

    /**
     * Sets the encryption module used by to encrypt and decrypt the data saved in the blSecurePU persistence unit. This
     * normally corresponds to the blEncryptionModule Spring bean which should be automatically set after invoking
     * {@link SecureOrderPaymentService#findSecurePaymentInfo(String, cn.globalph.b2c.payment.service.type.PaymentType)}
     * and {@link SecureOrderPaymentService#create( cn.globalph.b2c.payment.service.type.PaymentType)}.
     * 
     * @see {@link SecureOrderPaymentService#findSecurePaymentInfo(String, cn.globalph.b2c.payment.service.type.PaymentType)}
     * @see {@link SecureOrderPaymentService#create( cn.globalph.b2c.payment.service.type.PaymentType)}
     */
    public void setEncryptionModule(EncryptionModule encryptionModule);
    
}
