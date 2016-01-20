package cn.globalph.controller.catalog;


import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class works in combination with the CategoryHandlerMapping which finds a category based upon
 * the passed in URL.
 */
@Controller("categoryController")
public class CategoryController extends cn.globalph.b2c.web.controller.catalog.BasicCategoryController {
    
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ModelAndView modelAndView =  super.handleRequest(request, response);
    	return modelAndView;
    }
    
}
