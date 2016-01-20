package cn.globalph.b2c.offer.service;

import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.b2c.offer.domain.OfferAudit;
import cn.globalph.passport.domain.Customer;


/**
 * Service for managing {@link OfferAudit}s. An {@link OfferAudit} is used to track usage of an offer and offer code
 * for a particular {@link Order} and {@link Customer}. This provides easy and fast tracking of verifying max uses on
 * particular {@link Offer}s.
 *
 * @author Phillip Verheyden (phillipuniverse)
 */
public interface OfferAuditService {

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
