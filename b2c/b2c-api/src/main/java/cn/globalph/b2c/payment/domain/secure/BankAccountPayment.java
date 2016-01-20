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
public interface BankAccountPayment extends Referenced {

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
     * @return the account number
     */
    public String getAccountNumber();

    /**
     * @param account number the account number to set
     */
    public void setAccountNumber(String accountNumber);

    /**
     * @return the routing number
     */
    public String getRoutingNumber();

    /**
     * @param routing number the routing number to set
     */
    public void setRoutingNumber(String routingNumber);

}
