package cn.globalph.b2c.web.controller.checkout;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.common.web.controller.BasicController;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.web.core.CustomerState;

import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BroadleafOrderConfirmationController extends BasicController {

    @Resource(name = "blOrderService")
    protected OrderService orderService;
    
    @Resource(name = "blConfirmationControllerExtensionManager")
    protected ConfirmationControllerExtensionManager extensionManager;
    
    protected static String orderConfirmationView = "checkout/confirmation";

    public String displayOrderConfirmationByOrderNumber(String orderNumber, Model model,
             HttpServletRequest request, HttpServletResponse response) {
        Customer customer = CustomerState.getCustomer();
        if (customer != null) {
            Order order = orderService.findOrderByOrderNumber(orderNumber);
            if (order != null && customer.equals(order.getCustomer())) {
                extensionManager.getHandlerProxy().processAdditionalConfirmationActions(order);

                model.addAttribute("order", order);
                return getOrderConfirmationView();
            }
        }
        return null;
    }

    public String displayOrderConfirmationByOrderId(Long orderId, Model model,
             HttpServletRequest request, HttpServletResponse response) {

        Customer customer = CustomerState.getCustomer();
        if (customer != null) {
            Order order = orderService.getOrderById(orderId);
            if (order != null && customer.equals(order.getCustomer())) {
                extensionManager.getHandlerProxy().processAdditionalConfirmationActions(order);

                model.addAttribute("order", order);
                return getOrderConfirmationView();
            }
        }
        return null;
    }

    public String getOrderConfirmationView() {
        return orderConfirmationView;
    }

}
