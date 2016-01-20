package cn.globalph.b2c.payment.domain.secure;

import cn.globalph.b2c.payment.service.SecureOrderPaymentService;


/**
 * Entity associated with sensitive, secured bank account data. This data is stored specifically in the blSecurePU persistence.
 * All fetches and creates should go through {@link SecureOrderPaymentService} in order to properly decrypt/encrypt the data
 * from/to the database.
 *
 * @see {@link Referenced}
 * @author Phillip Verheyden (phillipuniverse)
 */
public interface GiftCardPayment extends Referenced {

    /**
     * @return the id
     */
    @Override
    public Long getId();

    /**
     * @param id the id to set
     */
    @Override
    public void setId(Long id);

    /**
     * @return the pan
     */
    public String getPan();

    /**
     * @param pan the pan to set
     */
    public void setPan(String pan);

    /**
     * @return the pin
     */
    public String getPin();

    /**
     * @param pin the pin to set
     */
    public void setPin(String pin);
}
