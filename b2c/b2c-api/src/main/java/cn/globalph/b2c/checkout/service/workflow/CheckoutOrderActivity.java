package cn.globalph.b2c.checkout.service.workflow;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.service.type.OrderStatus;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;
import cn.globalph.common.time.SystemTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CheckoutOrderActivity extends BaseActivity<ProcessContext<CheckoutSeed>> {

    public CheckoutOrderActivity() {
        setAutomaticallyRegisterRollbackHandler(true);
    }

    @Override
    public ProcessContext<CheckoutSeed> execute(ProcessContext<CheckoutSeed> context) throws Exception {
        Order order = context.getSeedData().getOrder();

        order.setStatus(OrderStatus.SUBMITTED);
        order.setSubmitDate(new Date(System.currentTimeMillis()));
        order.setOrderNumber(new SimpleDateFormat("yyMMdd").format(SystemTime.asDate()) + order.getId());

        return context;
    }
}
