package cn.globalph.controller.account;

import cn.global.passport.web.controller.CustomerAddressForm;
import cn.global.passport.web.controller.ManageCustomerAddressesController;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.passport.domain.Address;
import cn.globalph.passport.domain.Community;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.domain.NetNode;
import cn.globalph.passport.web.core.CustomerState;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/account/addresses")
public class GlobalphManageCustomerAddressesController extends ManageCustomerAddressesController {

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);
    }

    @ModelAttribute("customerAddresses")
    protected List<Address> populateCustomerAddresses() {
        return super.populateCustomerAddresses();
    }

    @ModelAttribute("firstLevelCommunities")
    protected List<Community> populateFirstLevelCommunities() {
        return super.populateFirstLevelCommunities();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String viewCustomerAddresses(HttpServletRequest request, Model model) {
        String response = super.viewCustomerAddresses(request, model);
        String orderAddress = request.getParameter("orderAddress");
        if (StringUtils.isNotEmpty(orderAddress)) {
            Customer customer = CustomerState.getCustomer();
            List<Address> addresses = customer.getConsigneeAddresses();
            if (addresses == null || addresses.isEmpty()) {
                return "redirect:/account/addresses/add?orderAddress=true";
            }
            model.addAttribute("orderAddress", orderAddress);
        }
        return response;
    }

    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String addCustomerAddress(HttpServletRequest request, Model model) {
        CustomerAddressForm form = new CustomerAddressForm();
        String orderAddress = request.getParameter("orderAddress");
        if (StringUtils.isNotEmpty(orderAddress)) {
            form.setOrderNumber(orderAddress);
        }
        model.addAttribute("customerAddressForm", form);
        return getEditAddressView();
    }

    @RequestMapping(method = RequestMethod.POST)
    public String addCustomerAddress(HttpServletRequest request, Model model, @ModelAttribute("customerAddressForm") CustomerAddressForm form, BindingResult result, RedirectAttributes redirectAttributes) throws ServiceException {
        String response = super.addCustomerAddress(request, model, form, result, redirectAttributes);
        if (!result.hasErrors() && StringUtils.isNotEmpty(form.getOrderNumber())) {

            Address address = (Address) model.asMap().get("address");
            if (address != null) {
                return "redirect:/checkout/orderAddress?addressId=" + address.getId();
            }
        }
        return response;
    }

    @RequestMapping(value = "/{customerAddressId}", method = RequestMethod.GET)
    public String viewCustomerAddress(HttpServletRequest request, Model model, @PathVariable("customerAddressId") Long customerAddressId) {

        Address address = addressService.readAddressById(customerAddressId);
        if (address == null) {
            throw new IllegalArgumentException("Customer Address not found with the specified customerAddressId");
        }
        CustomerAddressForm form = new CustomerAddressForm();
        form.setAddressId(address.getId());
        form.setProvince(address.getProvince());
        form.setCity(address.getCity());
        form.setDistrict(address.getDistrict());
        form.setCommunity(address.getCommunity());
        form.setAddress(address.getAddress());
        form.setPostalCode(address.getPostalCode());
        form.setReceiver(address.getReceiver());
        form.setPhone(address.getPhone());
        form.setDefault(address.isDefault());
        String orderAddress = request.getParameter("orderAddress");
        if (StringUtils.isNotEmpty(orderAddress)) {
            form.setOrderNumber("notnull");
        }
        model.addAttribute("customerAddressForm", form);
        return getEditAddressView();
    }

    @RequestMapping(value = "/{customerAddressId}", method = RequestMethod.POST)
    public String updateCustomerAddress(HttpServletRequest request, Model model, @PathVariable("customerAddressId") Long customerAddressId, @ModelAttribute("customerAddressForm") CustomerAddressForm form, BindingResult result, RedirectAttributes redirectAttributes) throws ServiceException {
        String response = super.updateCustomerAddress(request, model, customerAddressId, form, result, redirectAttributes);
        if (StringUtils.isNotEmpty(form.getOrderNumber())) {
            return getCustomerAddressesRedirect() + "?orderAddress=true";
        }
        return response;
    }

    @RequestMapping(value = "/{customerAddressId}/delete", method = RequestMethod.GET)
    public String removeCustomerAddress(HttpServletRequest request, Model model, @PathVariable("customerAddressId") Long customerAddressId, RedirectAttributes redirectAttributes) {
        String response = super.removeCustomerAddress(request, model, customerAddressId, redirectAttributes);

        String orderAddress = request.getParameter("orderAddress");
        if (StringUtils.isNotEmpty(orderAddress)) {
            return getCustomerAddressesRedirect() + "?orderAddress=true";
        }
        return response;
    }

    @RequestMapping(value = "/communities/{communityId}", produces = "application/json")
    public @ResponseBody Map<String, Object> findSecondLevelCommunityJson(@PathVariable("communityId") Long communityId){
    	List<Community> communities = super.populateSecondLevelCommunities(communityId);
    	List<Map<String,Object>> comms = new ArrayList<Map<String,Object>>();
    	for(Community c : communities){
    		Map<String,Object> map = new HashMap<String,Object>();
    		map.put("id", c.getId());
    		map.put("communityName", c.getCommunityName());
    		comms.add(map);
    	}
    	 Map<String, Object> responseMap = new HashMap<String, Object>();
    	 responseMap.put("secondLevelCommunities",comms);
    	return responseMap;
    }

    @RequestMapping(value = "/netNodes/{communityId}", produces = "application/json")
    public @ResponseBody Map<String, Object> findNetNodesJson(@PathVariable("communityId") Long communityId){
    	List<NetNode> netNodes = super.populateNetNode(communityId);
    	List<Map<String,Object>> entities = new ArrayList<Map<String,Object>>();
    	for(NetNode nn : netNodes){
    		Map<String,Object> map = new HashMap<String,Object>();
    		map.put("id", nn.getId());
    		map.put("netNodeName", nn.getNetNodeName());
    		entities.add(map);
    	}
    	 Map<String, Object> responseMap = new HashMap<String, Object>();
    	 responseMap.put("netNodes",entities);
    	return responseMap;
    }

}
