package cn.globalph.b2c.offer.service.workflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.b2c.checkout.service.workflow.CheckoutSeed;
import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.b2c.offer.domain.OfferAudit;
import cn.globalph.b2c.offer.service.OfferAuditService;
import cn.globalph.b2c.offer.service.OfferService;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;
import cn.globalph.b2c.workflow.state.ActivityStateManagerImpl;
import cn.globalph.common.time.SystemTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

/**
 * Saves an instance of OfferAudit for each offer in the passed in order.
 * 
 * @author Phillip Verheyden (phillipuniverse)
 * @see {@link RecordOfferUsageRollbackHandler}
 */
public class RecordOfferUsageActivity extends BaseActivity<ProcessContext<CheckoutSeed>> {
    
    /**
     * Key to retrieve the audits that were persisted
     */
    public static final String SAVED_AUDITS = "savedAudits";
    
    protected static final Log LOG = LogFactory.getLog(RecordOfferUsageActivity.class);

    @Resource(name="blOfferAuditService")
    protected OfferAuditService offerAuditService;
    
    @Resource(name = "blOfferService")
    protected OfferService offerService;

    @Override
    public ProcessContext<CheckoutSeed> execute(ProcessContext<CheckoutSeed> context) throws Exception {
        Order order = context.getSeedData().getOrder();
        Set<Offer> appliedOffers = offerService.getUniqueOffersFromOrder(order);
        
        List<OfferAudit> audits = saveOfferIds(appliedOffers, order);
        
        Map<String, Object> state = new HashMap<String, Object>();
        state.put(SAVED_AUDITS, audits);
        
        ActivityStateManagerImpl.getStateManager().registerState(this, context, getRollbackHandler(), state);

        return context;
    }
    
    /**
     * Persists each of the offers to the database as {@link OfferAudit}s.
     * 
     * @return the {@link OfferAudit}s that were persisted
     */
    protected List<OfferAudit> saveOfferIds(Set<Offer> offers, Order order) {
        List<OfferAudit> audits = new ArrayList<OfferAudit>(offers.size());
        for (Offer offer : offers) {
            OfferAudit audit = offerAuditService.create();
            audit.setCustomerId(order.getCustomer().getId());
            audit.setOfferId(offer.getId());
            audit.setOrderId(order.getId());
            audit.setRedeemedDate(SystemTime.asDate());
            audit = offerAuditService.save(audit);
            audits.add(audit);
           }
        return audits;
    }
        
}
