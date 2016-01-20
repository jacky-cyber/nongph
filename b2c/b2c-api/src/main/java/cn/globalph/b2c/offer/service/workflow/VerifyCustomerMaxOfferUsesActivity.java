package cn.globalph.b2c.offer.service.workflow;

import cn.globalph.b2c.checkout.service.workflow.CheckoutSeed;
import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.b2c.offer.service.OfferAuditService;
import cn.globalph.b2c.offer.service.OfferService;
import cn.globalph.b2c.offer.service.exception.OfferMaxUseExceededException;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;

import java.util.Set;

import javax.annotation.Resource;

/**
 * <p>Checks the offers being used in the order to make sure that the customer
 * has not exceeded the max uses for the {@link Offer}.</p>
 * 
 * This will also verify that max uses for any {@link OfferCode}s that were used to retrieve the {@link Offer}s.
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
public class VerifyCustomerMaxOfferUsesActivity extends BaseActivity<ProcessContext<CheckoutSeed>> {

    @Resource(name="blOfferAuditService")
    protected OfferAuditService offerAuditService;
    
    @Resource(name = "blOfferService")
    protected OfferService offerService;

    @Override
    public ProcessContext<CheckoutSeed> execute(ProcessContext<CheckoutSeed> context) throws Exception {
        Order order = context.getSeedData().getOrder();
        Set<Offer> appliedOffers = offerService.getUniqueOffersFromOrder(order);
        
        for (Offer offer : appliedOffers) {
            if (offer.isLimitedUsePerCustomer()) {
                Long currentUses = offerAuditService.countUsesByCustomer(order.getCustomer().getId(), offer.getId());
                if (currentUses >= offer.getMaxUsesPerCustomer()) {
                    throw new OfferMaxUseExceededException("The customer has used this offer more than the maximum allowed number of times.");
                	 }
            	}
           }
        return context;
    }
}
