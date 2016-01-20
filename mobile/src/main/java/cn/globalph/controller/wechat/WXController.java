package cn.globalph.controller.wechat;

import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.domain.WechatCustomer;
import cn.globalph.passport.service.AddressService;
import cn.globalph.passport.service.CustomerService;
import cn.globalph.passport.service.LoginService;
import cn.globalph.passport.service.WechatCustomerService;
import cn.globalph.wechat.JssdkSign;
import cn.globalph.wechat.WechatUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author steven
 * @since 7/31/15
 */
@Controller
public class WXController extends BasicAuthController {
    private static final Log LOG = LogFactory.getLog(AccessController.class);

    @Value("${default.propriety.host}")
    protected String defaultProprietyHost;
    @Resource(name = "blCustomerService")
    protected CustomerService customerService;
    @Resource(name = "blLoginService")
    protected LoginService loginService;
    @Resource(name = "blAddressService")
    protected AddressService addressService;
    @Resource(name = "phWechatCustomerService")
    private WechatCustomerService wechatCustomerService;
    @Resource(name = "phWechatUtil")
    protected WechatUtil wechatUtil;


    @ResponseBody
    @RequestMapping(value = "/api/queryOpenIdByMobile", method = RequestMethod.GET)
    public ResponseMessage queryOpenIdByMobile(HttpServletRequest request, HttpServletResponse response, String mobile) throws IOException {
        LOG.debug("query openid by mobile " + mobile);
        if (!verifyBasicAuth(request, response)) {
            LOG.debug("request not authorized");
            return ResponseMessage.ERR_UNAUTHORIZED;
        }
        Customer customer = customerService.readCustomerByUsername(mobile);
        if (customer != null) {
            WechatCustomer wechatCustomer = wechatCustomerService.readWechatCustomerByCustomerId(customer.getId());
            if (wechatCustomer != null) {
                return ResponseMessage.success(wechatCustomer);
            }
        }
        LOG.debug("customer or wechat customer not found for mobile " + mobile);
        return ResponseMessage.ERR_NOT_FOUND;
    }

    @ResponseBody
    @RequestMapping(value = "/api/requestJssdkSign", method = RequestMethod.POST)
    public JssdkSign requestJssdkSign(String url) {
        return wechatUtil.requestJssdkSign(url);
    }
}

