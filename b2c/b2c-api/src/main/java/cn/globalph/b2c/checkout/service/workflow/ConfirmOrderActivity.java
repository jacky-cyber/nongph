package cn.globalph.b2c.checkout.service.workflow;

import cn.globalph.b2c.order.service.type.OrderStatus;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;

import java.util.Calendar;
import java.util.Date;

public class ConfirmOrderActivity extends BaseActivity<ProcessContext<CheckoutSeed>> {

    public ConfirmOrderActivity() {
        setAutomaticallyRegisterRollbackHandler(true);
    }

    @Override
    public ProcessContext<CheckoutSeed> execute(ProcessContext<CheckoutSeed> context) throws Exception {
        CheckoutSeed seed = context.getSeedData();

        seed.getOrder().setStatus(OrderStatus.CONFIRMED);
        seed.getOrder().setConfirmDate(new Date());

        return context;
    }
}
