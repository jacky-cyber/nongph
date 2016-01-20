package cn.globalph.b2c.web.controller.cart;

import cn.globalph.b2c.catalog.service.CatalogService;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.service.OrderLogService;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.order.service.type.OrderStatus;
import cn.globalph.common.web.controller.BasicController;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.web.core.CustomerState;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

public class OrderHistoryController extends BasicController {
	
    @Resource(name="blOrderService")
    protected OrderService orderService;

    @Resource(name="blCatalogService")
    protected CatalogService catalogService;
	@Resource(name = "phOrderLogService")
	protected OrderLogService orderLogService;

	protected static final Log LOG = LogFactory.getLog(OrderHistoryController.class);
    
    protected static String orderHistoryView = "account/orderHistory";
    protected static String orderDetailsView = "account/partials/orderDetails";
    protected static String orderDetailsRedirectView = "account/partials/orderDetails";
    
    protected static int PAGE_SIZE = 5;
    
    public String viewOrderHistory(HttpServletRequest request, Model model) {
		String type = (String) request.getParameter("type");
		List<Order> orders;
		// Integer total;
		Customer customer = CustomerState.getCustomer();
		List<OrderStatus> sts = new ArrayList<OrderStatus>();
		if (customer == null || customer.isAnonymous()) {
			if (LOG.isInfoEnabled()) {
				LOG.info("view orders, customer is null or anonymous");
			}
			return null;
		}
		if (StringUtils.isNotEmpty(type)) {
			if (type.equals("wait4pay")) {
				sts.add(OrderStatus.SUBMITTED);
				orders = orderService.findOrdersForCustomer(customer, sts,
						PAGE_SIZE, 1);
			} else if (type.equals("wait4receive")) {
				sts.add(OrderStatus.CONFIRMED);
				orders = orderService.findOrdersForCustomer(customer, sts,
						PAGE_SIZE, 1);
			} else if (type.equals("wait4comment")) {
				orders = orderService.findNoCommentOrdersForCustomer(customer,
						PAGE_SIZE, 1);
			} else {
				sts.add(OrderStatus.CONFIRMED);
				sts.add(OrderStatus.SUBMITTED);
				sts.add(OrderStatus.COMPLETED);
				orders = orderService.findOrdersForCustomer(customer, sts,
						PAGE_SIZE, 1);
			}
		} else {
			sts.add(OrderStatus.CONFIRMED);
			sts.add(OrderStatus.SUBMITTED);
			sts.add(OrderStatus.COMPLETED);
			sts.add(OrderStatus.ORDER_REFUND);
			sts.add(OrderStatus.ITEM_REFUND);
			orders = orderService.findOrdersForCustomer(customer, sts,
					PAGE_SIZE, 1);
		}
		model.addAttribute("orders", orders);
		model.addAttribute("type", type);
		return getOrderHistoryView();
    }

    public String viewOrderDetails(HttpServletRequest request, Model model, String orderNumber) {
        Order order = orderService.findOrderByOrderNumber(orderNumber);
        if (order == null) {
            throw new IllegalArgumentException("The orderNumber provided is not valid");
        }
        model.addAttribute("order", order);
        return isAjaxRequest(request) ? getOrderDetailsView() : getOrderDetailsRedirectView();
    }

    public String getOrderHistoryView() {
        return orderHistoryView;
    }

    public String getOrderDetailsView() {
        return orderDetailsView;
    }

    public String getOrderDetailsRedirectView() {
        return orderDetailsRedirectView;
    }
}
