package cn.globalph.b2c.payment.domain.secure;

import cn.globalph.b2c.payment.service.SecureOrderPaymentService;


/**
 * Entity associated with sensitive, secured credit card data. This data is stored specifically in the blSecurePU persistence.
 * All fetches and creates should go through {@link SecureOrderPaymentService} in order to properly decrypt/encrypt the data
 * from/to the database.
 *
 * @see {@link Referenced}
 * @author Phillip Verheyden (phillipuniverse)
 */
public interface CreditCardPayment extends Referenced {

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
     * @return the expirationMonth
     */
    public Integer getExpirationMonth();

    /**
     * @param expirationMonth the expirationMonth to set
     */
    public void setExpirationMonth(Integer expirationMonth);

    /**
     * @return the expirationYear
     */
    public Integer getExpirationYear();

    /**
     * @param expirationYear the expirationYear to set
     */
    public void setExpirationYear(Integer expirationYear);

    /**
     * @return the nameOnCard
     */
    public String getNameOnCard();

    /**
     * @param nameOnCard the name on the card to set
     */
    public void setNameOnCard(String nameOnCard);

    public String getCvvCode();

    public void setCvvCode(String cvvCode);
}
