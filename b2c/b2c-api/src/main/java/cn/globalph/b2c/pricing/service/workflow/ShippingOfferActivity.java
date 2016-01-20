package cn.globalph.b2c.pricing.service.workflow;

import cn.globalph.b2c.offer.service.ShippingOfferService;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;

import javax.annotation.Resource;

/**
 * 物流促销计算
 * @author fwu
 *
 */
public class ShippingOfferActivity extends BaseActivity<ProcessContext<Order>> {

    @Resource(name="blShippingOfferService")
    private ShippingOfferService shippingOfferService;

    @Override
    public ProcessContext<Order> execute(ProcessContext<Order> context) throws Exception {
        Order order = context.getSeedData();
        shippingOfferService.reviewOffers(order);
        context.setSeedData(order);

        return context;
    }

}
