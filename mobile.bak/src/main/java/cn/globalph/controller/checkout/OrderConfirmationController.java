package cn.globalph.controller.checkout;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.web.controller.checkout.BroadleafOrderConfirmationController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class OrderConfirmationController extends BroadleafOrderConfirmationController {
    
    @Override
    @RequestMapping(value = "/confirmation/{orderNumber}", method = RequestMethod.GET)
    public String displayOrderConfirmationByOrderNumber(@PathVariable("orderNumber") String orderNumber, Model model,
            HttpServletRequest request, HttpServletResponse response) {
        return super.displayOrderConfirmationByOrderNumber(orderNumber, model, request, response);
    }

    @RequestMapping(value = "/failure/{orderNumber}", method = RequestMethod.GET)
    public String displayOrderPayFailByOrderNumber(@PathVariable("orderNumber") String orderNumber, Model model) {
        Order order = orderService.findOrderByOrderNumber(orderNumber);
        model.addAttribute("order", order);
        return "checkout/failure";
    }

}
