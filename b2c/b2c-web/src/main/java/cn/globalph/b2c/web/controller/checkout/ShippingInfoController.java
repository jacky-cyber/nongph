package cn.globalph.b2c.web.controller.checkout;

import java.util.List;

import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.FulfillmentOption;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.b2c.web.checkout.model.MultiShipInstructionForm;
import cn.globalph.b2c.web.checkout.model.ShippingInfoForm;
import cn.globalph.b2c.web.order.CartState;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.passport.domain.Address;
import cn.globalph.passport.domain.AddressImpl;
import cn.globalph.passport.domain.Community;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.web.core.CustomerState;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * In charge of performing the various checkout operations
 *
 */
public class ShippingInfoController extends AbstractCheckoutController {

    protected static String multishipView = "checkout/multiship";
    protected static String multishipAddAddressView = "checkout/multishipAddAddressForm";
    protected static String multishipAddAddressSuccessView = "redirect:/checkout/multiship";
    protected static String multishipSuccessView = "redirect:/checkout";

    public String getMultishipView() {
        return multishipView;
    }

    public String getMultishipAddAddressView() {
        return multishipAddAddressView;
    }

    public String getMultishipSuccessView() {
        return multishipSuccessView;
    }

    public String getMultishipAddAddressSuccessView() {
        return multishipAddAddressSuccessView;
    }

    /**
     * Converts the order to single ship by collapsing all of the shippable fulfillment groups into the default (first)
     * shippable fulfillment group.  Allows modules to add module specific shipping logic.
     *
     * @param request
     * @param response
     * @param model
     * @return a redirect to /checkout
     * @throws cn.globalph.b2c.pricing.service.exception.PricingException
     */
    public String convertToSingleship(HttpServletRequest request, HttpServletResponse response, Model model) throws PricingException {
        Order cart = CartState.getCart();
        fulfillmentGroupService.collapseToOneShippableFulfillmentGroup(cart, true);

        //Add module specific logic
        checkoutControllerExtensionManager.getHandlerProxy().performAdditionalShippingAction();

        return getCheckoutPageRedirect();
    }

    /**
     * Processes the request to save a single shipping address.  Allows modules to add module specific shipping logic.
     *
     * Note:  the default Broadleaf implementation creates an order
     * with a single fulfillment group. In the case of shipping to multiple addresses,
     * the multiship methods should be used.
     *
     * @param request
     * @param response
     * @param model
     * @param shippingForm
     * @return the return path
     * @throws cn.globalph.common.exception.ServiceException
     */
    public String saveSingleShip(HttpServletRequest request, 
    		                     HttpServletResponse response, Model model,
                                 ShippingInfoForm shippingForm, 
                                 BindingResult result) throws PricingException, ServiceException {
        Order cart = CartState.getCart();

        if(!shippingForm.getAddress().getId().equals(new Long(0))){
        	if(shippingForm.getFulfillmentOptionId()==null){
        		result.rejectValue("fulfillmentOptionId", "fulfillmentOptionId.required");
        		return getCheckoutView();
        	}
        	shippingForm.setAddress(addressService.readAddressById(shippingForm.getAddress().getId()));
        }else{
        	 shippingInfoFormValidator.validate(shippingForm, result);
             if (result.hasErrors()) {
                 return getCheckoutView();
             }
        	 shippingForm.getAddress().setCustomer(CustomerState.getCustomer());
        	 shippingForm.getAddress().setId(null);
        }
        
        FulfillmentGroup shippableFulfillmentGroup = fulfillmentGroupService.getFirstShippableFulfillmentGroup(cart);
        if (shippableFulfillmentGroup != null) {
        	Address address = shippingForm.getAddress();
        	address.setCustomer( CustomerState.getCustomer() );
            shippableFulfillmentGroup.setAddress( address );
            FulfillmentOption fulfillmentOption = fulfillmentOptionService.readFulfillmentOptionById(shippingForm.getFulfillmentOptionId());
            shippableFulfillmentGroup.setFulfillmentOption(fulfillmentOption);
            
            cart = orderService.save(cart, true);
            addressService.makeCustomerAddressDefault(address.getId(), address.getCustomer().getId());
        }

        //Add module specific logic
        checkoutControllerExtensionManager.getHandlerProxy().performAdditionalShippingAction();

        if (isAjaxRequest(request)) {
            //Add module specific model variables
            checkoutControllerExtensionManager.getHandlerProxy().addAdditionalModelVariables(model);
            return getCheckoutView();
        } else {
            return getCheckoutPageRedirect();
        }
    }

    /**
     * Renders the multiship page. This page is used by the user when shipping items
     * to different locations (or with different FulfillmentOptions) is desired.
     *
     * Note that the default Broadleaf implementation will require the user to input
     * an Address and FulfillmentOption for each quantity of each DiscreteOrderItem.
     *
     * @param request
     * @param response
     * @param model
     * @return the return path
     */
    public String showMultiship(HttpServletRequest request, HttpServletResponse response, Model model) {
        Customer customer = CustomerState.getCustomer();
        model.addAttribute("customerAddresses", customerService.readAddressesByCustomerId(customer.getId()));
        model.addAttribute("fulfillmentOptions", fulfillmentOptionService.readAllFulfillmentOptions());
        return getMultishipView();
    }

    /**
     * Renders the add address form during the multiship process
     *
     * @param request
     * @param response
     * @param model
     * @return the return path
     */
    public String showMultishipAddAddress(HttpServletRequest request, HttpServletResponse response, Model model) {
        //model.addAttribute("states", stateService.findStates());
        //model.addAttribute("countries", countryService.findCountries());
        return getMultishipAddAddressView();
    }

    public String saveMultiShipInstruction(HttpServletRequest request, HttpServletResponse response, Model model,
                                           MultiShipInstructionForm instructionForm) throws ServiceException, PricingException {
        Order cart = CartState.getCart();
        FulfillmentGroup fulfillmentGroup = null;

        for (FulfillmentGroup tempFulfillmentGroup : cart.getFulfillmentGroups()) {
            if (tempFulfillmentGroup.getId().equals(instructionForm.getFulfillmentGroupId())) {
                fulfillmentGroup = tempFulfillmentGroup;
            }
        }
        fulfillmentGroupService.save(fulfillmentGroup);

        //append current time to redirect to fix a problem with ajax caching in IE
        return getCheckoutPageRedirect()+ "?_=" + System.currentTimeMillis();
    }

    
    public String viewCustomerAddress(HttpServletRequest request, Model model,ShippingInfoForm shippingForm,Long customerAddressId) {
    	Address address = addressService.readAddressById(customerAddressId);
    	if(address == null){
    		address = new AddressImpl(new Long(0));
    	}
    	shippingForm.setAddress(address);
    	return getCheckoutView();
    }
    
    public String viewCustomerAddress(HttpServletRequest request, Model model,ShippingInfoForm shippingForm) {
    	shippingForm.setAddress(new AddressImpl());
    	return getCheckoutView();
    }
    
}
