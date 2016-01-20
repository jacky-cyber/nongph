package cn.globalph.b2c.web.order.security;

import cn.globalph.passport.domain.WechatCustomer;
import cn.globalph.passport.service.CustomerUserDetails;
import cn.globalph.passport.service.WechatCustomerService;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 认证成功处理器
 * @author felix.wu
 */
@Component("blAuthenticationSuccessHandler")
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Resource(name = "phWechatCustomerService")
    private WechatCustomerService wechatCustomerService;

    @Override
    public void onAuthenticationSuccess( HttpServletRequest request, 
    		                             HttpServletResponse response,
                                         Authentication authentication ) throws ServletException, IOException {

        String openId = (String) request.getSession().getAttribute("openId");

        if (StringUtils.isNotEmpty(openId)) {
            CustomerUserDetails customer = (CustomerUserDetails) authentication.getPrincipal();
            if (customer != null) {
                List<WechatCustomer> wechatCustomerList = wechatCustomerService.readWechatCustomersByOpenId(openId);
                if (wechatCustomerList != null && !wechatCustomerList.isEmpty()) {
                    WechatCustomer wechatCustomer = wechatCustomerList.get(0);
                    wechatCustomer.setCustomerId(customer.getId());
                    wechatCustomerService.save(wechatCustomer);
                } else {
                    WechatCustomer wechatCustomer = wechatCustomerService.create();
                    wechatCustomer.setCustomerId(customer.getId());
                    wechatCustomer.setActive(true);
                    wechatCustomer.setOpenId(openId);
                    wechatCustomerService.save(wechatCustomer);
                }
            }
        }

        String targetUrl = request.getParameter(getTargetUrlParameter());
        if ( StringUtils.isNotBlank(targetUrl) && targetUrl.contains(":") ) {
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
