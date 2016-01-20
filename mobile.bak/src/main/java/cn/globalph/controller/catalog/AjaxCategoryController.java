package cn.globalph.controller.catalog;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class works in combination with the CategoryHandlerMapping which finds a category based upon
 * the passed in URL.
 */
@Controller("ajaxCategoryController")
public class AjaxCategoryController extends cn.globalph.b2c.web.controller.catalog.BasicAjaxCategoryController {
    
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ModelAndView modelAndView =  super.handleRequest(request, response);
    	return modelAndView;
    }
}
