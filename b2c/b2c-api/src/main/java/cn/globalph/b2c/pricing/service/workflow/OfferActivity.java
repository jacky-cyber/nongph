package cn.globalph.b2c.pricing.service.workflow;

import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.b2c.offer.service.OfferService;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;

import java.util.List;

import javax.annotation.Resource;

/**
 * 应用活动，用于添加到购物车流程
 * @author felix.wu
 *
 */
public class OfferActivity extends BaseActivity<ProcessContext<Order>> {

    @Resource(name="blOfferService")
    private OfferService offerService;

    @Override
    public ProcessContext<Order> execute(ProcessContext<Order> context) throws Exception {
        Order order = context.getSeedData();
        //查找适用于当前订单的所有优惠活动
        List<Offer> offers = offerService.buildOfferListForOrder(order);
        //应用优惠活动
        offerService.applyOffersToOrder(offers, order);
        context.setSeedData(order);

        return context;
    }

}
