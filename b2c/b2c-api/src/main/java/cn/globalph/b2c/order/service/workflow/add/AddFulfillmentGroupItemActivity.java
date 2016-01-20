package cn.globalph.b2c.order.service.workflow.add;

import cn.globalph.b2c.order.service.workflow.CartOperationRequest;
import cn.globalph.b2c.order.strategy.FulfillmentGroupItemStrategy;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;

import javax.annotation.Resource;

public class AddFulfillmentGroupItemActivity extends BaseActivity<ProcessContext<CartOperationRequest>> {
    
    @Resource(name = "blFulfillmentGroupItemStrategy")
    protected FulfillmentGroupItemStrategy fgItemStrategy;

    @Override
    public ProcessContext<CartOperationRequest> execute(ProcessContext<CartOperationRequest> context) throws Exception {
        CartOperationRequest request = context.getSeedData();
        
        request = fgItemStrategy.onItemAdded(request);
        
        context.setSeedData(request);
        return context;
    }

}
