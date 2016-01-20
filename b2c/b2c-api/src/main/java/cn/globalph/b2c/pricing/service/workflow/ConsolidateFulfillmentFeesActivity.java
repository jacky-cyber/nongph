package cn.globalph.b2c.pricing.service.workflow;

import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.lang.StringUtils;

import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.FulfillmentGroupFee;
import cn.globalph.b2c.order.domain.FulfillmentGroupItem;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.service.FulfillmentGroupService;
import cn.globalph.b2c.product.domain.SkuFee;
import cn.globalph.b2c.product.domain.SkuFeeType;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;
import cn.globalph.common.rule.MvelHelper;

import org.mvel2.MVEL;
import org.mvel2.ParserContext;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * 
 * @author felix.wu
 */
public class ConsolidateFulfillmentFeesActivity extends BaseActivity<ProcessContext<Order>> {
    
    @SuppressWarnings("unchecked")
    protected static final Map EXPRESSION_CACHE = Collections.synchronizedMap(new LRUMap(1000));
    
    @Resource(name = "blFulfillmentGroupService")
    protected FulfillmentGroupService fulfillmentGroupService;

    @Override
    public ProcessContext<Order> execute(ProcessContext<Order> context) throws Exception {
        Order order = context.getSeedData();
        
        for (FulfillmentGroup fulfillmentGroup : order.getFulfillmentGroups()) {
            //create and associate all the Fulfillment Fees
            for (FulfillmentGroupItem item : fulfillmentGroup.getFulfillmentGroupItems()) {
                List<SkuFee> fees = item.getOrderItem().getSku().getFees();
                
                if (fees != null) {
                    for (SkuFee fee : fees) {
                        if (SkuFeeType.FULFILLMENT.equals(fee.getFeeType())) {
                            if (shouldApplyFeeToFulfillmentGroup(fee, fulfillmentGroup)) {
                                FulfillmentGroupFee fulfillmentFee = fulfillmentGroupService.createFulfillmentGroupFee();
                                fulfillmentFee.setName(fee.getName());
                                fulfillmentFee.setAmount(fee.getAmount());
                                fulfillmentFee.setFulfillmentGroup(fulfillmentGroup);
                                
                                fulfillmentGroup.addFulfillmentGroupFee(fulfillmentFee);
                            }
                        }
                    }
                }
            }
            
            if (fulfillmentGroup.getFulfillmentGroupFees().size() > 0) {
                fulfillmentGroup = fulfillmentGroupService.save(fulfillmentGroup);
            }
        }
        
        context.setSeedData(order);
        return context;
    }

    /**
     * If the SkuFee expression is null or empty, this method will always return true
     * 
     * @param fee
     * @param fulfillmentGroup
     * @return
     */
    protected boolean shouldApplyFeeToFulfillmentGroup(SkuFee fee, FulfillmentGroup fulfillmentGroup) {
        boolean appliesToFulfillmentGroup = true;
        String feeExpression = fee.getExpression();
        
        if (!StringUtils.isEmpty(feeExpression)) {
            Serializable exp = (Serializable) EXPRESSION_CACHE.get(feeExpression);
            if (exp == null) {
                ParserContext mvelContext = new ParserContext();
                mvelContext.addImport("MVEL", MVEL.class);
                mvelContext.addImport("MvelHelper", MvelHelper.class);
                exp = MVEL.compileExpression(feeExpression, mvelContext);
            
                EXPRESSION_CACHE.put(feeExpression, exp);
            }
            HashMap<String, Object> vars = new HashMap<String, Object>();
            vars.put("fulfillmentGroup", fulfillmentGroup);
            return (Boolean)MVEL.executeExpression(feeExpression, vars);
        }
        
        return appliesToFulfillmentGroup;
    }

}
