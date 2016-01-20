package cn.globalph.controller.wechat;

import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.security.util.MD5Util;
import cn.globalph.common.sms.SMSUtil;
import cn.globalph.common.web.RequestProcessor;
import cn.globalph.common.web.WebRequestContext;
import cn.globalph.common.web.controller.BasicController;
import cn.globalph.coupon.issue.event.CouponIssueEvent;
import cn.globalph.coupon.issue.event.CouponIssueEventSource;
import cn.globalph.coupon.issue.event.CouponIssueEventType;
import cn.globalph.passport.domain.Address;
import cn.globalph.passport.domain.AddressImpl;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.domain.WechatCustomer;
import cn.globalph.passport.service.AddressService;
import cn.globalph.passport.service.CustomerService;
import cn.globalph.passport.service.LoginService;
import cn.globalph.passport.service.WechatCustomerService;
import cn.globalph.passport.web.core.CustomerState;
import cn.globalph.passport.web.core.form.BindPhoneForm;
import cn.globalph.passport.web.core.security.CustomerStateRequestProcessor;
import cn.globalph.web.util.WebValidation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The controller responsible for all actions involving logging a customer from wechat
 *
 * @author steven
 */
@Controller
@RequestMapping("/wechat")
public class AccessController extends BasicController {
    private static final Log LOG = LogFactory.getLog(AccessController.class);

    @Value("${default.propriety.host}")
    protected String defaultProprietyHost;
    @Resource(name = "blCustomerService")
    protected CustomerService customerService;
    @Resource(name = "blLoginService")
    protected LoginService loginService;
    @Resource(name = "blAddressService")
    protected AddressService addressService;
    @Resource(name = "blCustomerStateRequestProcessor")
    private CustomerStateRequestProcessor customerStateRequestProcessor;
    @Resource(name = "blCartStateRequestProcessor")
    private RequestProcessor cartStateRequestProcessor;
    @Resource(name = "phWechatCustomerService")
    private WechatCustomerService wechatCustomerService;
    @Resource(name = "wechatUtils")
    private WechatUtils wechatUtils;
    @Autowired
    private ApplicationContext applicationContext;

    private static Map<String, BindCode> bindCodeMap = new ConcurrentHashMap<>();

    @RequestMapping(value = "/redirect2destination", method = RequestMethod.GET)
    public String redirect2destination(HttpSession session) {
        String category = (String) session.getAttribute("CATEGORY");
        return "redirect:/" + (StringUtils.isNotEmpty(category) ? category : "");
    }

    @RequestMapping(value = "/route", method = RequestMethod.GET)
    public void route(String url, String phone, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Customer customer = CustomerState.getCustomer();
        Boolean needOpenId = true;
        if (customer.getId() != null && customer.isLoggedIn()) {//已登录
            WechatCustomer wechatCustomer = wechatCustomerService.readWechatCustomerByCustomerId(customer.getId());
            if (wechatCustomer != null) {//有openid,就不需要
                loadProperties(defaultProprietyHost, customer);
                needOpenId = false;
            }
        } else if (StringUtils.isNotEmpty(phone)) {//如果没登录,根据phone登录
            customer = customerService.readCustomerByUsername(phone);
            if (customer != null) {//根据phone找到customer
                loginService.switchCustomer(customer.getLoginName());
                customerStateRequestProcessor.process(WebRequestContext.getWebRequestContext().getWebRequest());
                cartStateRequestProcessor.process(WebRequestContext.getWebRequestContext().getWebRequest());
                WechatCustomer wechatCustomer = wechatCustomerService.readWechatCustomerByCustomerId(customer.getId());

                if (wechatCustomer != null) {//如果有openid
                    loadProperties(defaultProprietyHost, customer);
                    needOpenId = false;
                }
            }
        }

        if (needOpenId && request.getHeader("user-agent").toLowerCase().contains("micromessenger")) {// 是微信浏览器
            String ext = "";
            if (StringUtils.isNotEmpty(url)) {
                ext += "?url=" + URLEncoder.encode(url, "UTF-8");
                if (StringUtils.isNotEmpty(phone)) {
                    ext += "&phone=" + URLEncoder.encode(phone, "UTF-8");
                }
            } else {
                if (StringUtils.isNotEmpty(phone)) {
                    ext += "?phone=" + URLEncoder.encode(phone, "UTF-8");
                }
            }
            String redirectUri = "http://" + request.getServerName() + "/wechat/getOpenid" + ext;
            String href = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wechatUtils.getAppId()
                    + "&redirect_uri=" + URLEncoder.encode(redirectUri, "UTF-8")
                    + "&response_type=code&scope=snsapi_base&state=123#wechat_redirect";

            response.getWriter().write("<script type='text/javascript'> location.href = '"+href+"'</script>");

        } else {
            response.sendRedirect(StringUtils.isNotEmpty(url) ? url : "/");
        }
    }

    @RequestMapping(value = "/getOpenid", method = RequestMethod.GET, params = "code")
    public String getOpenid(HttpSession session, String code, String url, final String phone, ModelMap model) throws Exception {

        String openId = wechatUtils.requestOpenId(code);
        if (StringUtils.isNotEmpty(openId)) {
            Customer customer = CustomerState.getCustomer();
            if (customer.getId() != null && customer.isLoggedIn()) {//已登录
                WechatCustomer wechatCustomer = wechatCustomerService.readWechatCustomerByCustomerId(customer.getId());
                if (wechatCustomer == null) {
                    wechatCustomer = wechatCustomerService.create();
                    wechatCustomer.setCustomerId(customer.getId());
                    wechatCustomer.setActive(true);
                    wechatCustomer.setOpenId(openId);
                    wechatCustomerService.save(wechatCustomer);
                    loadProperties(defaultProprietyHost, customer);
                }
            } else {//未登录
                WechatCustomer wechatCustomer = wechatCustomerService.readWechatCustomerByOpenId(openId);
                if (wechatCustomer != null) {
                    customer = customerService.getCustomerById(wechatCustomer.getCustomerId());
                    if (customer != null) {
                        loadProperties(defaultProprietyHost, customer);
                        loginService.switchCustomer(customer.getLoginName());
                        return "redirect:" + (StringUtils.isNotEmpty(url) ? url : "/");
                    }
                } else if (StringUtils.isNotEmpty(phone)) {
                    String password = MD5Util.MD5WithTime();
                    customer = customerService.registerCustomer(phone, password, "m" + phone, phone);

                    //注册成功送优惠券
                    applicationContext.publishEvent(
                            new CouponIssueEvent(CouponIssueEventType.REGISTER_COUPON_EVENT.getType(), false,
                                    new CouponIssueEventSource(customer.getId(),
                                            new Date(System.currentTimeMillis())
                                    )
                            )
                    );

                    loginService.switchCustomer(customer.getLoginName());
                    customerStateRequestProcessor.process(WebRequestContext.getWebRequestContext().getWebRequest());
                    cartStateRequestProcessor.process(WebRequestContext.getWebRequestContext().getWebRequest());

                    wechatCustomer = wechatCustomerService.create();
                    wechatCustomer.setCustomerId(customer.getId());
                    wechatCustomer.setActive(true);
                    wechatCustomer.setOpenId(openId);
                    wechatCustomer.setFromRRM(true);
                    wechatCustomerService.save(wechatCustomer);

                    loadProperties(defaultProprietyHost, customer);
                } else {
                    session.setAttribute("openId", openId);
                    model.addAttribute("openId", openId);
                    BindPhoneForm bindPhoneForm = new BindPhoneForm();
                    model.addAttribute("bindPhoneForm", bindPhoneForm);
                    model.addAttribute("redirectUrl", url);
                    return "authentication/bind";
                }

            }
        }
        return "redirect:" + (StringUtils.isNotEmpty(url) ? url : "/");
    }

    private void loadProperties(final String proprietyHost, final Customer customer) {
        try {
            WechatCustomer wechatCustomer = wechatCustomerService.readWechatCustomerByCustomerId(customer.getId());
            if (wechatCustomer != null && !wechatCustomer.isPropertiesLoaded() && !isPropertiesLoaded(customer)) {
                List<Proprietor> proprietors = wechatUtils.getProprietors(proprietyHost, customer.getLoginName());
                if (proprietors != null && !proprietors.isEmpty()) {
                    for (int i = 0; i < proprietors.size(); i++) {
                        Proprietor proprietor = proprietors.get(i);
                        Address address = new AddressImpl();
                        address.setCustomer(customer);
                        address.setReceiver(proprietor.getName());
                        address.setPhone(proprietor.getContactno());
                        address.setProvince(proprietor.getProvince());
                        address.setCity(proprietor.getCity());
                        address.setDistrict(proprietor.getDistrict());
                        address.setCommunity(proprietor.getCommunityname());
                        address.setAddress(proprietor.getBuildingno() + "-" + proprietor.getRoomno());
                        address.setActive(true);
                        if (i == 0) {
                            address.setDefault(true);
                        }
                        address.setFromRRM(true);
                        addressService.saveAddress(address);
                    }
                    wechatCustomer.setPropertiesLoaded(true);
                    wechatCustomerService.save(wechatCustomer);
                }

            }

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

    }

    private Boolean isPropertiesLoaded(Customer customer) {
        List<Address> addresses = addressService.readActiveAddressesByCustomerId(customer.getId());
        if (addresses != null && !addresses.isEmpty()) {
            for (Address address : addresses) {
                if (address.isFromRRM()) {
                    return true;
                }
            }
        }
        return false;
    }

    @RequestMapping(value = "/bind", method = RequestMethod.POST)
    public String processBindPhone(@ModelAttribute("bindPhoneForm") BindPhoneForm form, BindingResult errors) throws ServiceException, PricingException {

        validate(form, errors);

        if (!errors.hasErrors()) {
            String phone = form.getPhone();
            Customer customer = customerService.readCustomerByUsername(phone);

            if (customer == null) {
                String password = MD5Util.MD5WithTime();
                customer = customerService.registerCustomer(phone, password, "m" + phone, phone);

                //注册成功送优惠券
                applicationContext.publishEvent(
                        new CouponIssueEvent(CouponIssueEventType.REGISTER_COUPON_EVENT.getType(), false,
                                new CouponIssueEventSource(customer.getId(),
                                        new Date(System.currentTimeMillis())
                                )
                        )
                );
            }

            loginService.switchCustomer(phone);
            customerStateRequestProcessor.process(WebRequestContext.getWebRequestContext().getWebRequest());
            cartStateRequestProcessor.process(WebRequestContext.getWebRequestContext().getWebRequest());

            String redirectUrl = form.getRedirectUrl();
            if (StringUtils.isNotBlank(redirectUrl) && redirectUrl.contains(":")) {
                redirectUrl = null;
            }
            return "redirect:" + (StringUtils.isEmpty(redirectUrl) ? "/" : redirectUrl);
        } else {
            return "authentication/bind";
        }
    }

    // 发送手机短信验证码
    @RequestMapping(value = "/sms/{phone}")
    @ResponseBody
    public Boolean sendBindCode(@PathVariable("phone") String phone) {
        String bindCode = generateBindCode(phone);
        try {
            SMSUtil.sendMessage(phone, "您的手机验证码为" + bindCode + ",有效时间为两分钟。");
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private BindCode retrieveBindCode(String phone) {
        BindCode bindCode = bindCodeMap.get(phone);
        Date genTime = bindCode.getGenTime();
        long diff = new Date().getTime() - genTime.getTime();
        if (diff > 2 * 60 * 1000) {
            return null;
        } else {
            return bindCode;
        }
    }

    private synchronized String generateBindCode(String phone) {
        for (String key : bindCodeMap.keySet()) {
            BindCode bindCode = bindCodeMap.get(key);
            Date genTime = bindCode.getGenTime();
            long diff = new Date().getTime() - genTime.getTime();
            if (diff > 2 * 60 * 1000) {
                bindCodeMap.remove(key);
            }
        }

        String bindCode = SMSUtil.generateValidationCode();
        bindCodeMap.put(phone, new BindCode(phone, bindCode));
        return bindCode;
    }

    public void validate(BindPhoneForm form, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "phone.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "bindCode", "validationCode.required");

        if (!errors.hasErrors()) {
            if (!WebValidation.isPhone(form.getPhone())) {
                errors.rejectValue("phone", "phone.invalid", null, null);
            }

            BindCode bindCode = retrieveBindCode(form.getPhone());
            if (bindCode == null || !form.getBindCode().equals(bindCode.getBindCode())) {
                errors.rejectValue("bindCode", "validationCode.invalid",
                        null, null);
            }
        }
    }

    class BindCode {
        private String phone;
        private String bindCode;
        private Date genTime;

        public BindCode(String phone, String bindCode) {
            this.phone = phone;
            this.bindCode = bindCode;
            this.genTime = new Date();
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getBindCode() {
            return bindCode;
        }

        public void setBindCode(String bindCode) {
            this.bindCode = bindCode;
        }

        public Date getGenTime() {
            return genTime;
        }

        public void setGenTime(Date genTime) {
            this.genTime = genTime;
        }
    }
}
