package cn.globalph.controller.account;

import cn.global.passport.web.controller.CustomerAddressForm;
import cn.global.passport.web.controller.ManageCustomerAddressesController;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.passport.domain.Address;
import cn.globalph.passport.domain.Community;
import cn.globalph.passport.domain.NetNode;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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
        return super.viewCustomerAddresses(request, model);
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String addCustomerAddress(HttpServletRequest request, Model model, @ModelAttribute("customerAddressForm") CustomerAddressForm form, BindingResult result, RedirectAttributes redirectAttributes) throws ServiceException {
        return super.addCustomerAddress(request, model, form, result, redirectAttributes);
    }
    
    @RequestMapping(value = "/{customerAddressId}", method = RequestMethod.GET)
    public String viewCustomerAddress(HttpServletRequest request, Model model, @PathVariable("customerAddressId") Long customerAddressId) {
        return super.viewCustomerAddress(request, model, customerAddressId);
    }

    @RequestMapping(value = "/{customerAddressId}", method = RequestMethod.POST)
    public String updateCustomerAddress(HttpServletRequest request, Model model, @PathVariable("customerAddressId") Long customerAddressId, @ModelAttribute("customerAddressForm") CustomerAddressForm form, BindingResult result, RedirectAttributes redirectAttributes) throws ServiceException {
        return super.updateCustomerAddress(request, model, customerAddressId, form, result, redirectAttributes);
    }

    @RequestMapping(value = "/{customerAddressId}", method = RequestMethod.POST, params="removeAddress=删除")
    public String removeCustomerAddress(HttpServletRequest request, Model model, @PathVariable("customerAddressId") Long customerAddressId, RedirectAttributes redirectAttributes) {
        return super.removeCustomerAddress(request, model, customerAddressId, redirectAttributes);
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
