package cn.globalph.b2c.offer.service.workflow;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.globalph.b2c.checkout.service.workflow.CheckoutSeed;
import cn.globalph.b2c.offer.domain.OfferAudit;
import cn.globalph.b2c.offer.service.OfferAuditService;
import cn.globalph.b2c.workflow.Activity;
import cn.globalph.b2c.workflow.ProcessContext;
import cn.globalph.b2c.workflow.state.RollbackFailureException;
import cn.globalph.b2c.workflow.state.RollbackHandler;

import org.springframework.stereotype.Component;


/**
 * Rolls back audits that were saved in the database from {@link RecordOfferUsageActivity}.
 *
 * @author Phillip Verheyden (phillipuniverse)
 * @see {@link RecordOfferUsageActivity}
 */
@Component("blRecordOfferUsageRollbackHandler")
public class RecordOfferUsageRollbackHandler implements RollbackHandler<CheckoutSeed> {

    @Resource(name = "blOfferAuditService")
    protected OfferAuditService offerAuditService;
    
    @Override
    public void rollbackState(Activity<? extends ProcessContext<CheckoutSeed>> activity, ProcessContext<CheckoutSeed> processContext, Map<String, Object> stateConfiguration) throws RollbackFailureException {
        List<OfferAudit> audits = (List<OfferAudit>) stateConfiguration.get(RecordOfferUsageActivity.SAVED_AUDITS);
        
        for (OfferAudit audit : audits) {
            offerAuditService.delete(audit);
        }
    }
    
}
