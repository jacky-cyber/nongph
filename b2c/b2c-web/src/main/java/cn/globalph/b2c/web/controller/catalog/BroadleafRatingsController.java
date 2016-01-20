package cn.globalph.b2c.web.controller.catalog;

import cn.globalph.b2c.catalog.service.CatalogService;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.product.domain.Product;
import cn.globalph.b2c.rating.domain.ReviewDetail;
import cn.globalph.b2c.rating.service.RatingService;
import cn.globalph.b2c.rating.service.type.RatingType;
import cn.globalph.passport.web.core.CustomerState;

import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

public class BroadleafRatingsController {

    @Resource(name = "blRatingService")
    protected RatingService ratingService;
    @Resource(name = "blCatalogService")
    protected CatalogService catalogService;
    @Resource(name = "blOrderService")
    protected OrderService orderService;
    
    protected String formView = "catalog/partials/review";
    protected String successView = "catalog/partials/reviewSuccessful";
    
    
    public String viewReviewForm(HttpServletRequest request, Model model, ReviewForm form, String itemId) {
        Product product = catalogService.findProductById(Long.valueOf(itemId));
        form.setProduct(product);
        ReviewDetail reviewDetail = ratingService.readReviewByCustomerAndItem(CustomerState.getCustomer(), itemId);
        if (reviewDetail != null) {
            form.setReviewText(reviewDetail.getReviewText());
            form.setRating(reviewDetail.getRatingDetail().getRating());
        }
        model.addAttribute("reviewForm", form);
        return getFormView();
    }
    
    public String reviewItem(HttpServletRequest request, Model model, ReviewForm form, String itemId) {
        ratingService.reviewItem(itemId, RatingType.PRODUCT, CustomerState.getCustomer(), form.getRating(), form.getReviewText());
        model.addAttribute("reviewForm", form);
        return getSuccessView();
    }
    
    public String getFormView() {
        return formView;
    }
    
    public String getSuccessView() {
        return successView;
    }
    
}
