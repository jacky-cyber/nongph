package cn.globalph.controller.account;

import cn.globalph.common.web.controller.BroadleafRedirectController;
import cn.globalph.common.web.security.BroadleafAuthenticationSuccessRedirectStrategy;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The controller expects a session attribute to be set called BLC_REDIRECT_URL and 
 * that it is being called from an Ajax redirect process.
 * 
 * It would be unexpected for an implementor to modify this class or the corresponding view
 * blcRedirect.html.  
 * 
 * The purpose of this class is to support ajax redirects after a successful login. 
 * 
 * @see BroadleafAuthenticationSuccessRedirectStrategy
 * 
 * @author bpolster
 */
@Controller
public class RedirectController extends BroadleafRedirectController {
    
    @RequestMapping("/redirect")
    public String redirect(HttpServletRequest request, HttpServletResponse response, Model model) {
        return super.redirect(request, response, model);
    }
}
