package cn.globalph.b2c.offer.dao;

import cn.globalph.b2c.offer.domain.OfferAudit;
import cn.globalph.b2c.offer.service.OfferService;
import cn.globalph.b2c.offer.service.workflow.RecordOfferUsageActivity;
import cn.globalph.b2c.offer.service.workflow.VerifyCustomerMaxOfferUsesActivity;

/**
 * DAO for auditing what went on with offers being added to an order
 *
 * @author Phillip Verheyden (phillipuniverse)
 * @see {@link VerifyCustomerMaxOfferUsesActivity}, {@link RecordOfferUsageActivity},
 * {@link OfferService#verifyMaxCustomerUsageThreshold(cn.globalph.passport.domain.Customer, cn.globalph.b2c.offer.domain.OfferCode)}
 */
public interface OfferAuditDao {
    
    public OfferAudit readAuditById(Long offerAuditId);
    
    /**
     * Persists an audit record to the database
     */
    public OfferAudit save(OfferAudit offerAudit);
    
    public void delete(OfferAudit offerAudit);

    /**
     * Creates a new offer audit
     */
    public OfferAudit create();
    
    /**
     * Counts how many times the an offer has been used by a customer
     * 
     * @param customerId
     * @param offerId
     * @return
     */
    public Long countUsesByCustomer(Long customerId, Long offerId);
}
