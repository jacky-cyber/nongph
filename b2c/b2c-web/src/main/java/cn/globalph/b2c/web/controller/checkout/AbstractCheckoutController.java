package cn.globalph.b2c.web.controller.checkout;

import cn.globalph.b2c.order.service.*;
import cn.globalph.passport.service.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.b2c.checkout.service.CheckoutService;
import cn.globalph.b2c.coupon.service.CouponCodeService;
import cn.globalph.b2c.payment.service.OrderPaymentService;
import cn.globalph.b2c.payment.service.OrderToPaymentRequestDTOService;
import cn.globalph.b2c.web.checkout.validator.GiftCardInfoFormValidator;
import cn.globalph.b2c.web.checkout.validator.OrderInfoFormValidator;
import cn.globalph.b2c.web.checkout.validator.ShippingInfoFormValidator;
import cn.globalph.common.payment.service.PaymentGatewayCheckoutService;
import cn.globalph.common.web.controller.BasicController;
import cn.globalph.coupon.service.CouponService;
import cn.globalph.coupon.service.CustomerCouponService;
import cn.globalph.passport.domain.Community;
import cn.globalph.passport.domain.NetNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.beans.PropertyEditorSupport;

/**
 * An abstract controller that provides convenience methods and resource declarations for its
 * children. Operations that are shared between controllers that deal with checkout belong here.
 */
public abstract class AbstractCheckoutController extends BasicController {

    private static final Log LOG = LogFactory.getLog(AbstractCheckoutController.class);

    protected static String cartPageRedirect = "redirect:/cart";
    protected static String checkoutView = "checkout/checkout";
    protected static String checkoutPageRedirect = "redirect:/checkout";
    protected static String baseConfirmationView = "ajaxredirect:/confirmation";
    protected static String applyCouponCodeView = "checkout/couponCode";
    /*add by jenny s 15/09/11*/
    protected static String applyBonusPointView = "checkout/bonusPoint";
    /*add by jenny e*/

    /* Optional Service */
    @Autowired(required=false)
    @Qualifier("blPaymentGatewayCheckoutService")
    protected PaymentGatewayCheckoutService paymentGatewayCheckoutService;

    /* Services */
    @Resource(name = "blOrderService")
    protected OrderService orderService;
    
    @Resource(name = "blOrderItemService")
    protected OrderItemService orderItemService;

    @Resource(name = "blOrderPaymentService")
    protected OrderPaymentService orderPaymentService;

    @Resource(name = "blOrderToPaymentRequestDTOService")
    protected OrderToPaymentRequestDTOService dtoTranslationService;

    @Resource(name = "blFulfillmentGroupService")
    protected FulfillmentGroupService fulfillmentGroupService;

    @Resource(name = "blFulfillmentOptionService")
    protected FulfillmentOptionService fulfillmentOptionService;

    @Resource(name = "blCheckoutService")
    protected CheckoutService checkoutService;
    
    @Resource(name = "blCustomerService")
    protected CustomerService customerService;

    @Resource(name = "blAddressService")
    protected AddressService addressService;
    
    /* Validators */
    @Resource(name = "blShippingInfoFormValidator")
    protected ShippingInfoFormValidator shippingInfoFormValidator;

    @Resource(name = "blGiftCardInfoFormValidator")
    protected GiftCardInfoFormValidator giftCardInfoFormValidator;

    @Resource(name = "blOrderInfoFormValidator")
    protected OrderInfoFormValidator orderInfoFormValidator;

    /* Extension Managers */
    @Resource(name = "blCheckoutControllerExtensionManager")
    protected BroadleafCheckoutControllerExtensionManager checkoutControllerExtensionManager;

    @Resource(name = "nphCommunityService")
    protected CommunityService communityService;
    
    @Resource(name = "nphNetNodeService")
    protected NetNodeService netNodeService;

    @Resource(name = "nphOrderAddressService")
    protected OrderAddressService orderAddressService;
    
    @Resource(name = "phCustomerCouponService")
    protected CustomerCouponService customerCouponService;
    
    @Resource(name = "phCouponService")
    protected CouponService couponService;
    
    @Resource(name = "phCouponCodeService")
    protected CouponCodeService couponCodeService;
    
    /* Views and Redirects */
    public String getCartPageRedirect() {
        return cartPageRedirect;
    }

    public String getCheckoutView() {
        return checkoutView;
    }

    public String getCheckoutPageRedirect() {
        return checkoutPageRedirect;
    }

    public String getBaseConfirmationView() {
        return baseConfirmationView;
    }

    protected String getConfirmationView(String orderNumber) {
        return getBaseConfirmationView() + "/" + orderNumber;
    }

    protected void populateModelWithReferenceData(HttpServletRequest request, Model model) {
        //Add module specific model variables
        checkoutControllerExtensionManager.getHandlerProxy().addAdditionalModelVariables(model);
    }
    
    public String getApplyCouponCodeView(){
    	return applyCouponCodeView;
    }

    /*add by jenny 15/09/11*/
    public String getApplyBonusPointView(){
        return applyBonusPointView;
    }
    /*add by jenny e*/
    /**
     * Initializes some custom binding operations for the checkout flow.
     * More specifically, this method will attempt to bind state and country
     * abbreviations to actual State and Country objects when the String
     * representation of the abbreviation is submitted.
     *
     * @param request
     * @param binder
     * @throws Exception
     */
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {        
        //增加片区和网点选择
        //大片区
        binder.registerCustomEditor(Community.class,"address.firstLevelCommunity",new PropertyEditorSupport(){
        	@Override
        	public void setAsText(String text){
        		if(!StringUtils.isBlank(text)){
        			Long communityId = Long.parseLong(text);
        			Community community = communityService.readCommunityById(communityId);
        			setValue(community);
        			
        		}else 
        			setValue(null);
        	}
        });
        
        //小片区
        binder.registerCustomEditor(Community.class,"address.secondLevelCommunity",new PropertyEditorSupport(){
        	@Override
        	public void setAsText(String text){
        		if(!StringUtils.isBlank(text)){
        			Long communityId = Long.parseLong(text);
        			Community community = communityService.readCommunityById(communityId);
        			setValue(community);
        			
        		}else 
        			setValue(null);
        	}
        });
        
        //网点
        binder.registerCustomEditor(NetNode.class,"address.netNode",new PropertyEditorSupport(){
        	@Override
        	public void setAsText(String text){
        		if(!StringUtils.isBlank(text)){
        			Long netNodeId = Long.parseLong(text);
        			NetNode netNode = netNodeService.readNetNodeById(netNodeId);
        			setValue(netNode);
        			
        		}else 
        			setValue(null);
        	}
        });
    }
}
