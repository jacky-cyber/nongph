package cn.globalph.b2c.checkout.service.workflow;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;

public class BonusPointActivity extends BaseActivity<ProcessContext<CheckoutSeed>> {

    private Boolean isRevert = false;

    public BonusPointActivity() {
        setAutomaticallyRegisterRollbackHandler(true);
    }

    @Override
    public ProcessContext<CheckoutSeed> execute(ProcessContext<CheckoutSeed> context) throws Exception {
        Order order = context.getSeedData().getOrder();

        int bonusPoint = order.getCustomer().getBonusPoint();
        int willGetBonusPoint = (int) Math.floor(order.getTotal().getAmount().doubleValue());
        if (isRevert) {
            order.getCustomer().setBonusPoint(bonusPoint - willGetBonusPoint);
        } else {
            order.getCustomer().setBonusPoint(bonusPoint + willGetBonusPoint);
        }
        return context;
    }

    public void setIsRevert(Boolean isRevert) {
        this.isRevert = isRevert;
    }
}
