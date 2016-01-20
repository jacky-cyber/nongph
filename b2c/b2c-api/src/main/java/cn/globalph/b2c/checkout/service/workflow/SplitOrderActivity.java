package cn.globalph.b2c.checkout.service.workflow;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderAddress;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.domain.OrderLog;
import cn.globalph.b2c.order.service.OrderAddressService;
import cn.globalph.b2c.order.service.OrderItemService;
import cn.globalph.b2c.order.service.OrderLogService;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.product.domain.Provider;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;
import cn.globalph.common.time.SystemTime;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Split order by provider
 *
 * @author steven
 * @since Jun 28 2015
 */
public class SplitOrderActivity extends BaseActivity<ProcessContext<CheckoutSeed>> {

    protected static final Log LOG = LogFactory.getLog(SplitOrderActivity.class);

    @Resource(name = "blOrderService")
    protected OrderService orderService;
    @Resource(name = "nphOrderAddressService")
    protected OrderAddressService orderAddressService;
    @Resource(name = "blOrderItemService")
    protected OrderItemService orderItemService;
    @Resource(name = "phOrderLogService")
    protected OrderLogService orderLogService;

    @Override
    public ProcessContext<CheckoutSeed> execute(ProcessContext<CheckoutSeed> context) throws Exception {
        Order order = context.getSeedData().getOrder();
        Map<Provider, List<OrderItem>> map = new HashMap<>();
        List<OrderItem> orderItemList = order.getOrderItems();
        for (OrderItem orderItem : orderItemList) {
            Provider provider = orderItem.getSku().getProduct().getProvider();
            if (!map.containsKey(provider)) {
                List<OrderItem> orderItems = new ArrayList<>();
                orderItems.add(orderItem);
                map.put(provider, orderItems);
            } else {
                List<OrderItem> orderItems = map.get(provider);
                orderItems.add(orderItem);
            }
        }
        if (map.size() == 1) {
            for (Provider provider : map.keySet()) {
                order.setProvider(provider);
                orderService.save(order, false);
            }
        } else if (map.size() >= 2) {
            OrderAddress orderAddress = orderAddressService.readOrderAddressByOrderId(order.getId());
            List<String> orderNumbers = new ArrayList<>();
            for (Provider provider : map.keySet()) {
                Order subOrder = orderService.createSuborderForOrder(order);
                subOrder.setProvider(provider);
                subOrder = orderService.save(subOrder, false);
                List<OrderItem> orderItems = map.get(provider);
                for (OrderItem orderItem : orderItems) {
                    OrderItem newOrderItem = (OrderItem) BeanUtils.cloneBean(orderItem);
                    newOrderItem.setId(null);
                    newOrderItem.setOrder(subOrder);
                    newOrderItem = orderItemService.saveOrderItem(newOrderItem);
                    subOrder.addOrderItem(newOrderItem);
                }
                OrderAddress newOrderAddress = (OrderAddress) BeanUtils.cloneBean(orderAddress);
                newOrderAddress.setOrder(subOrder);
                newOrderAddress.setId(null);
                newOrderAddress = orderAddressService.save(newOrderAddress);
                subOrder.setOrderAddress(newOrderAddress);
                subOrder.setSubmitDate(order.getSubmitDate());
                subOrder.setConfirmDate(order.getConfirmDate());
                subOrder.setSubTotal(subOrder.calculateSubTotal());
                subOrder.setOrderNumber(new SimpleDateFormat("yyyyMMddHHmmssS").format(SystemTime.asDate()) + subOrder.getId());
                orderService.save(subOrder, false);

                OrderLog orderLog = orderLogService.create();
                orderLog.setOrder(subOrder);
                orderLog.setMessage("由于您的货物在不同仓库，此订单由<a href='/account/orders/" + order.getOrderNumber() + "'>" + order.getOrderNumber() + "</a>分拆而来，此订单中的货物将单独配送。");
                orderLog.setType(OrderLog.ORDER_LOG_TYPE_SYSTEM);
                orderLog.setOperator("SYSTEM");
                orderLogService.save(orderLog);
                orderNumbers.add(subOrder.getOrderNumber());
            }


            OrderLog orderLog1 = orderLogService.create();
            orderLog1.setOrder(order);
            orderLog1.setMessage("由于您的货物在不同仓库，系统将分拆此订单.");
            orderLog1.setType(OrderLog.ORDER_LOG_TYPE_SYSTEM);
            orderLog1.setOperator("SYSTEM");
            orderLogService.save(orderLog1);

            for (String orderNumber : orderNumbers) {
                OrderLog orderLog2 = orderLogService.create();
                orderLog2.setOrder(order);
                orderLog2.setMessage("子订单为:" + "<a href='/account/orders/" + orderNumber + "'>" + orderNumber + "</a>");
                orderLog2.setType(OrderLog.ORDER_LOG_TYPE_SYSTEM);
                orderLog2.setOperator("SYSTEM");
                orderLogService.save(orderLog2);
            }

            OrderLog orderLog3 = orderLogService.create();
            orderLog3.setOrder(order);
            orderLog3.setMessage("子订单中的货物将单独配送");
            orderLog3.setType(OrderLog.ORDER_LOG_TYPE_SYSTEM);
            orderLog3.setOperator("SYSTEM");
            orderLogService.save(orderLog3);
        }
        return context;
    }

}

