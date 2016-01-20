package cn.globalph.b2c.order.service.workflow.remove;

import cn.globalph.b2c.order.service.workflow.CartOperationRequest;
import cn.globalph.b2c.order.strategy.FulfillmentGroupItemStrategy;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;

import javax.annotation.Resource;

public class RemoveFulfillmentGroupItemActivity extends BaseActivity<ProcessContext<CartOperationRequest>> {
    
    @Resource(name = "blFulfillmentGroupItemStrategy")
    protected FulfillmentGroupItemStrategy fgItemStrategy;

    @Override
    public ProcessContext<CartOperationRequest> execute(ProcessContext<CartOperationRequest> context) throws Exception {
        CartOperationRequest request = context.getSeedData();
        
        request = fgItemStrategy.onItemRemoved(request);
        
        context.setSeedData(request);
        return context;
    }

}
