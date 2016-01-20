package cn.globalph.controller.catalog;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.service.OrderItemService;
import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.b2c.product.domain.Product;
import cn.globalph.b2c.rating.domain.ReviewDetail;
import cn.globalph.b2c.rating.service.type.RatingType;
import cn.globalph.b2c.web.controller.catalog.BroadleafRatingsController;
import cn.globalph.b2c.web.controller.catalog.ReviewForm;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.security.service.ExploitProtectionService;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.web.core.CustomerState;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class RatingsController extends BroadleafRatingsController {
	private static final Log LOG = LogFactory.getLog(RatingsController.class);
	@Resource(name = "blOrderItemService")
	OrderItemService orderItemService;
	@Resource(name = "blExploitProtectionService")
	private ExploitProtectionService exploitProtectionService;

    @RequestMapping(value = "/reviews/product/{itemId}", method = RequestMethod.GET)
    public String viewReviewForm(HttpServletRequest request, Model model, @PathVariable("itemId") String itemId, @ModelAttribute("reviewForm") ReviewForm form) {
        return super.viewReviewForm(request, model, form, itemId);
    }

    @RequestMapping(value = "/reviews/product/{itemId}", method = RequestMethod.POST)
    public String reviewItem(HttpServletRequest request, Model model, @PathVariable("itemId") String itemId, @ModelAttribute("reviewForm") ReviewForm form) {
        return super.reviewItem(request, model, form, itemId);
    }

    @RequestMapping(value = "/reviews/orderItem/{itemId}", method = RequestMethod.GET)
    public String viewOrderItemReviewForm(HttpServletRequest request, Model model, @PathVariable("itemId") String itemId, @ModelAttribute("reviewForm") ReviewForm form) {
    	OrderItem orderItem = orderItemService.readOrderItemById(Long.valueOf(itemId));
    	Product product = orderItem.getSku().getProduct();
        form.setProduct(product);
        form.setOrderItemId(itemId);
        ReviewDetail reviewDetail = ratingService.readReviewByCustomerAndItem(CustomerState.getCustomer(), product.getId().toString());
        if (reviewDetail != null) {
            form.setReviewText(reviewDetail.getReviewText());
            form.setRating(reviewDetail.getRatingDetail().getRating());
            model.addAttribute("view", true);
        }
        model.addAttribute("reviewForm", form);
        return getFormView();
    }

    @RequestMapping(value = "/reviews/orderItem/{itemId}", method = RequestMethod.POST)
    public String reviewOrderItem(HttpServletRequest request, Model model,
    		@PathVariable("itemId") String itemId, @ModelAttribute("reviewForm") ReviewForm form, BindingResult errors) {
    	Double rating = form.getRating();
    	if(rating == null || rating.doubleValue() < 1.0){
    		errors.rejectValue("rating", "rating.required");
    	}
    	String reviewText = form.getReviewText();
    	if(reviewText == null || reviewText.equals("")){
    		errors.rejectValue("reviewText", "reviewText.required");
    	}else{
    		if(reviewText.length() > 255){
    			errors.rejectValue("reviewText", "reviewText.too.long");
    		}
    	}
    	if(errors.hasErrors()){
        	OrderItem orderItem = orderItemService.readOrderItemById(Long.valueOf(itemId));
        	Product product = orderItem.getSku().getProduct();
            form.setProduct(product);
            form.setOrderItemId(itemId);
    		return getFormView();
    	}

    	OrderItem orderItem = orderItemService.readOrderItemById(Long.valueOf(itemId));
    	Product product = orderItem.getSku().getProduct();
		reviewText = exploitProtectionService.cleanString(form.getReviewText());
		ratingService.reviewItem(product.getId().toString(), RatingType.PRODUCT, CustomerState.getCustomer(), form.getRating(), reviewText);
		model.addAttribute("reviewForm", form);

    	orderItem.setIsReview(true);
    	orderItemService.saveOrderItem(orderItem);
    	Order order = orderItem.getOrder();
    	List<OrderItem> orderItems = order.getOrderItems();
    	boolean isAllReview = true;
    	for(OrderItem oi : orderItems){
    		if(!oi.isReview()) {
    			isAllReview = false;
    			break;
    		}
    	}
    	if(isAllReview){
    		order.setReview(true);
    		try {
				orderService.save(order, false);
			} catch (PricingException e) {
				LOG.error("order items is all reviewed, save order error");
			}
    	}
        return getSuccessView();
    }

    @RequestMapping(value = "/review/{orderNumber}/items", method = RequestMethod.GET)
    public String orderItems(@PathVariable String orderNumber, Model model, HttpServletRequest request) throws ServiceException {
        Order order = orderService.findOrderByOrderNumber(orderNumber);
        String errorMsg = request.getParameter("errorMsg");
        model.addAttribute("errorMsg", errorMsg);
        model.addAttribute("order", order);
        return "catalog/reviewItems";
    }

    @RequestMapping(value = "/review/{orderNumber}/items", method = RequestMethod.POST)
    public String viewOrderItems(@PathVariable String orderNumber, HttpServletRequest request, RedirectAttributes attributes) throws ServiceException {
        Order order = orderService.findOrderByOrderNumber(orderNumber);
        Customer customer = CustomerState.getCustomer();
        if (customer.isAnonymous()
                    || order == null
                    || order.getCustomer() == null
                    || !customer.getId().equals(order.getCustomer().getId())) {
            return "redirect:/account/menu";
        }
        if(!order.isReview()){
	        List<OrderItem> orderItems = order.getOrderItems();
	        boolean isAllReviewed = true;
            for (OrderItem orderItem : orderItems) {
                if (orderItem.getReviewDetail() == null) {
                    String id = orderItem.getId() != null ? orderItem.getId().toString() : null;
                    if (id == null) {
                        LOG.error("order item id is null,order id:" + order.getId());
                        isAllReviewed = false;
                        continue;
                    }
                    String rating = request.getParameter("rating" + id);
                    String reviewText = request.getParameter("reviewText" + id);
                    String rating2 = request.getParameter("rating2" + id);
                    String rating3 = request.getParameter("rating3" + id);
                    String[] ratingArray = new String[]{rating, rating2, rating3};
                    if (StringUtils.isEmpty(reviewText) || StringUtils.isEmpty(reviewText.trim()) || reviewText.length() > 255) {
                        isAllReviewed = false;
                    } else {
                        for (String aRatingArray : ratingArray) {
                            if (!(StringUtils.isEmpty(aRatingArray)
                                        || StringUtils.isEmpty(aRatingArray.trim()))) {
                                if (!judgeRatingISValid(aRatingArray)) {
                                    isAllReviewed = false;
                                    break;
                                }
                            } else {
                                isAllReviewed = false;
                            }
                        }
                    }
                    if (isAllReviewed) {
                        reviewText = exploitProtectionService.cleanString(reviewText);
                        if (orderItem.getSku() != null
                                    && orderItem.getSku().getProduct() != null
                                    && orderItem.getSku().getProduct().getId() != null) {

                            ratingService.reviewItem(orderItem.getSku().getProduct().getId().toString(),
                                        RatingType.PRODUCT, CustomerState.getCustomer(), ratingArray, reviewText, orderItem);
                        } else {
                            LOG.error("order item,get (sku.product.id) is null, order item id:" + +orderItem.getId());
                            isAllReviewed = false;
                        }
                    }
                }
            }
            if (isAllReviewed) {
                order.setReview(true);
                try {
                    orderService.save(order, false);
                } catch (PricingException e) {
                    LOG.error("order items is all reviewed, but save order error, order id:" + order.getId());
                    attributes.addAttribute("errorMsg", "error");
                }
            } else {
                attributes.addAttribute("errorMsg", "error");
            }
        }
        return "redirect:/review/" + order.getOrderNumber() + "/items";
    }
	public boolean judgeRatingISValid(String rating){
        return !(!rating.equals("1.0")
                    && !rating.equals("2.0")
                    && !rating.equals("3.0")
                    && !rating.equals("4.0")
                    && !rating.equals("5.0"));
    }
}
