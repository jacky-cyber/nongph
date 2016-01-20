package cn.globalph.b2c.checkout.service.workflow;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;

/**
 * Created by Admin on 2015/9/15.
 */
public class DecrementBonusPointActivity extends BaseActivity<ProcessContext<CheckoutSeed>> {

    private Boolean isRevert = false;

    public DecrementBonusPointActivity() {
        setAutomaticallyRegisterRollbackHandler(false);
    }

    @Override
    public ProcessContext<CheckoutSeed> execute(ProcessContext<CheckoutSeed> context) throws Exception {
        Order order = context.getSeedData().getOrder();
        int bonusPoint = order.getCustomer().getBonusPoint();
        int usedBonusPoint = order.getDeductionBonusPoint();
        if (isRevert) {
            order.getCustomer().setBonusPoint(bonusPoint + usedBonusPoint);
        } else {
            order.getCustomer().setBonusPoint(bonusPoint - usedBonusPoint);
        }
        return context;
    }

    public void setIsRevert(Boolean isRevert) {
        this.isRevert = isRevert;
    }
}
