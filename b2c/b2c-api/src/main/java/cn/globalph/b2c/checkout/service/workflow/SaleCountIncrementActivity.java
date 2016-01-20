package cn.globalph.b2c.checkout.service.workflow;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.product.dao.SkuDao;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Resource;
import java.util.List;


/**
 * increment sale count
 */
public class SaleCountIncrementActivity extends BaseActivity<ProcessContext<CheckoutSeed>> {

    protected static final Log LOG = LogFactory.getLog(SaleCountIncrementActivity.class);
    @Resource(name = "blSkuDao")
    protected SkuDao skuDao;

    @Override
    public ProcessContext<CheckoutSeed> execute(ProcessContext<CheckoutSeed> context) throws Exception {
        Order order = context.getSeedData().getOrder();
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            int quantity = orderItem.getQuantity();
            Sku sku = orderItem.getSku();
            sku.setSaleCount((sku.getSaleCount() == null ? 0 : sku.getSaleCount()) + quantity);
            skuDao.save(sku);
        }
        return context;
    }
}

