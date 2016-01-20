package cn.globalph.b2c.checkout.service.workflow;

import cn.globalph.b2c.delivery.DeliveryType;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderAddress;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.domain.OrderLog;
import cn.globalph.b2c.order.service.OrderAddressService;
import cn.globalph.b2c.order.service.OrderItemService;
import cn.globalph.b2c.order.service.OrderLogService;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.product.domain.PickupAddress;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Resource;
import java.util.List;


/**
 * Split order by provider
 *
 * @author steven
 * @since Jun 28 2015
 */
public class DetermineDeliveryTypeActivity extends BaseActivity<ProcessContext<CheckoutSeed>> {

    protected static final Log LOG = LogFactory.getLog(DetermineDeliveryTypeActivity.class);

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

        List<Order> suborderList = orderService.readSuborderList(order.getId());
        if (suborderList != null && !suborderList.isEmpty()) {
            for (Order suborder : suborderList) {
                determineDeliveryType(suborder);
            }
        } else {
            determineDeliveryType(order);
        }
        return context;
    }

    private void determineDeliveryType(Order order) {

        OrderAddress orderAddress = order.getOrderAddress();
        if (orderAddress == null) {
            orderAddress = orderAddressService.readOrderAddressByOrderId(order.getId());
        }

        List<OrderItem> orderItems = order.getOrderItems();
        if (orderItems == null) {
            orderItems = orderItemService.findOrderItemsByOrderId(order.getId());
        }
        for (OrderItem orderItem : orderItems) {
            if (StringUtils.isNotEmpty(orderItem.getDeliveryType())) {
                String deliveryType = orderItem.getDeliveryType();
                orderAddress.setDeliveryType(deliveryType);
                PickupAddress pickupAddress = orderItem.getPickupAddress();
                orderAddress.setPickupAddress(pickupAddress);
                orderAddressService.save(orderAddress);
                order.setDeliveryType(deliveryType);
                try {
                    orderService.save(order, false);
                } catch (Exception e) {
                    LOG.debug(e.getMessage(), e);
                    OrderLog orderLog = orderLogService.create();
                    orderLog.setOrder(order);
                    orderLog.setType("SYSTEM");
                    orderLog.setDisplay(false);
                    orderLog.setOperator("SYSTEM");
                    orderLog.setMessage("判断配送类型失败");
                    orderLogService.save(orderLog);
                }
            }
        }

        if (StringUtils.isEmpty(order.getDeliveryType())) {
            orderAddress.setDeliveryType(DeliveryType.LOGISTICS.getType());
            orderAddressService.save(orderAddress);
            order.setDeliveryType(DeliveryType.LOGISTICS.getType());
            try {
                orderService.save(order, false);
            } catch (Exception e) {
                LOG.debug(e.getMessage(), e);
                OrderLog orderLog = orderLogService.create();
                orderLog.setOrder(order);
                orderLog.setType("SYSTEM");
                orderLog.setDisplay(false);
                orderLog.setOperator("SYSTEM");
                orderLog.setMessage("判断配送类型失败");
                orderLogService.save(orderLog);
            }
        }
    }

}

