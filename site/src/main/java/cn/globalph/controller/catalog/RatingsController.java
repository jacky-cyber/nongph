package cn.globalph.controller.catalog;

import javax.servlet.http.HttpServletRequest;

import cn.globalph.b2c.web.controller.catalog.BroadleafRatingsController;
import cn.globalph.b2c.web.controller.catalog.ReviewForm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RatingsController extends BroadleafRatingsController {

    @RequestMapping(value = "/reviews/product/{itemId}", method = RequestMethod.GET)
    public String viewReviewForm(HttpServletRequest request, Model model, @PathVariable("itemId") String itemId, @ModelAttribute("reviewForm") ReviewForm form) {
        return super.viewReviewForm(request, model, form, itemId);
    }
    
    @RequestMapping(value = "/reviews/product/{itemId}", method = RequestMethod.POST)
    public String reviewItem(HttpServletRequest request, Model model, @PathVariable("itemId") String itemId, @ModelAttribute("reviewForm") ReviewForm form) {
        return super.reviewItem(request, model, form, itemId);
    }
    
    
}
