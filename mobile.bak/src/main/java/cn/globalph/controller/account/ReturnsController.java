package cn.globalph.controller.account;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.xml.ws.Response;

/**
 * Created by Admin on 2015/9/2.
 */
@Controller
@RequestMapping("/account/returns")
public class ReturnsController {
    @RequestMapping(method = RequestMethod.GET)
    public String viewReturnsRules(){
        return "account/returns";
    }
}
