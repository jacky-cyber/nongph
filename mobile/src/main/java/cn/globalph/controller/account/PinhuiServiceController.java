package cn.globalph.controller.account;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Admin on 2015/9/2.
 */
@Controller
@RequestMapping("/account/service")
public class PinhuiServiceController {

    @RequestMapping(method = RequestMethod.GET)
    public String viewPinhuiService(){
       return "account/pinhuiservice";
    }
}
