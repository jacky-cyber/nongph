package cn.global.passport.web.controller;

import cn.globalph.common.security.service.ExploitProtectionService;
import org.apache.commons.lang.StringUtils;

import cn.global.passport.web.controller.validator.CustomerAddressValidator;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.web.controller.BasicController;
import cn.globalph.passport.domain.Address;
import cn.globalph.passport.domain.Community;
import cn.globalph.passport.domain.NetNode;
import cn.globalph.passport.service.AddressService;
import cn.globalph.passport.service.CommunityService;
import cn.globalph.passport.service.CustomerService;
import cn.globalph.passport.service.NetNodeService;
import cn.globalph.passport.web.core.CustomerState;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.beans.PropertyEditorSupport;
import java.util.List;

public class ManageCustomerAddressesController extends BasicController {

    @Resource(name = "blAddressService")
    protected AddressService addressService;
    @Resource(name = "blCustomerAddressValidator")
    protected CustomerAddressValidator customerAddressValidator;
    @Resource(name="blCustomerService")
    protected CustomerService customerService;
    @Resource(name = "nphCommunityService")
    protected CommunityService communityService;
    @Resource(name = "nphNetNodeService")
    protected NetNodeService netNodeService;
    @Resource(name = "blExploitProtectionService")
    private ExploitProtectionService exploitProtectionService;

    protected String addressUpdatedMessage = "收货地址修改成功!";
    protected String addressAddedMessage = "收货地址添加成功!";
    protected String addressRemovedMessage = "收货地址删除成功!";
    protected String addressRemovedErrorMessage = "此收货地址正在被使用，无法删除!";
    
    protected static String customerAddressesView = "account/manageCustomerAddresses";
    protected static String customerAddressesRedirect = "redirect:/account/addresses";
    protected static String editAddressView = "account/editAddress";
    
    /**
     * Initializes some custom binding operations for the managing an address. 
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
        binder.registerCustomEditor(Community.class,"firstLevelCommunity",new PropertyEditorSupport(){
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
        binder.registerCustomEditor(Community.class,"secondLevelCommunity",new PropertyEditorSupport(){
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
        binder.registerCustomEditor(NetNode.class,"netNode",new PropertyEditorSupport(){
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
     
    protected List<Address> populateCustomerAddresses() {
        return addressService.readActiveAddressesByCustomerId(CustomerState.getCustomer().getId());
    }
    
    protected List<Community> populateFirstLevelCommunities(){
    	return communityService.findFirstLevelComms();
    }
    
    protected List<Community> populateSecondLevelCommunities(Long communityId){
    	return communityService.findChildCommunities(communityId);
    }
    
    protected List<NetNode> populateNetNode(Long communityId){
    	return netNodeService.findNetNodeByCommunity(communityId);
    }
    
    public String viewCustomerAddresses(HttpServletRequest request, Model model) {
        model.addAttribute("customerAddressForm", new CustomerAddressForm());
        return getCustomerAddressesView();
    }
    
    public String addCustomerAddress(HttpServletRequest request, Model model) {
        model.addAttribute("customerAddressForm", new CustomerAddressForm());
        return getEditAddressView();
    }
    
    public String viewCustomerAddress(HttpServletRequest request, Model model, Long customerAddressId) {
    	Address address = addressService.readAddressById(customerAddressId);
    	if(address == null){
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
        model.addAttribute("customerAddressForm", form);

    	return getEditAddressView();
    }

    public String addCustomerAddress(HttpServletRequest request, Model model, CustomerAddressForm form, BindingResult result, RedirectAttributes redirectAttributes) throws ServiceException {
        customerAddressValidator.validate(form, result);
        if (result.hasErrors()) {
            return getEditAddressView();
        }
        
        Address address = addressService.create();

        address.setProvince(form.getProvince());
        address.setCity(form.getCity());
        address.setDistrict(form.getDistrict());
        address.setCommunity(form.getCommunity());
        address.setDefault(form.isDefault());
        String addressStr = exploitProtectionService.cleanString(form.getAddress());
        address.setAddress(addressStr);
        address.setPostalCode(form.getPostalCode());
    	address.setReceiver(form.getReceiver());
    	address.setPhone(form.getPhone());
    	address.setCustomer(CustomerState.getCustomer());
        
        address= addressService.saveAddress(address);
        model.addAttribute("address", address);
        if (form.isDefault()) {
            addressService.makeCustomerAddressDefault(address.getId(), address.getCustomer().getId());
        }
        if (!isAjaxRequest(request)) {
            List<Address> addresses = addressService.readActiveAddressesByCustomerId(CustomerState.getCustomer().getId());
            model.addAttribute("addresses", addresses);
        }
        redirectAttributes.addFlashAttribute("successMessage", getAddressAddedMessage());
        return getCustomerAddressesRedirect();
    }
    
    public String updateCustomerAddress(HttpServletRequest request, Model model, Long customerAddressId, CustomerAddressForm form, BindingResult result, RedirectAttributes redirectAttributes) throws ServiceException {
        customerAddressValidator.validate(form, result);
        if (result.hasErrors()) {
        	form.setAddressId(customerAddressId);
            return getEditAddressView();
        }
        Address address = addressService.readAddressById(customerAddressId);
        if (address == null) {
            throw new IllegalArgumentException("Customer Address not found with the specified customerAddressId");
        }

        address.setProvince(form.getProvince());
        address.setCity(form.getCity());
        address.setDistrict(form.getDistrict());
        address.setCommunity(form.getCommunity());
        address.setDefault(form.isDefault());
        String addressStr = exploitProtectionService.cleanString(form.getAddress());
        address.setAddress(addressStr);
        address.setReceiver(form.getReceiver());
    	address.setPostalCode(form.getPostalCode());
    	address.setPhone(form.getPhone());
    	address.setCustomer(CustomerState.getCustomer());
        address = addressService.saveAddress(address);
      
        if (form.isDefault()) {
            addressService.makeCustomerAddressDefault(address.getId(), address.getCustomer().getId());
        }
        redirectAttributes.addFlashAttribute("successMessage", getAddressUpdatedMessage());
        return getCustomerAddressesRedirect();
    }
    
    public String removeCustomerAddress(HttpServletRequest request, Model model, Long addressId, RedirectAttributes redirectAttributes) {
        try {
            addressService.deleteAddressById(addressId);
            redirectAttributes.addFlashAttribute("successMessage", getAddressRemovedMessage());
        } catch (DataIntegrityViolationException e) {
            // This likely occurred because there is an order or cart in the system that is currently utilizing this
            // address. Therefore, we're not able to remove it as it breaks a foreign key constraint
            redirectAttributes.addFlashAttribute("errorMessage", getAddressRemovedErrorMessage());
        }
        return getCustomerAddressesRedirect();
    }

    public String getCustomerAddressesView() {
        return customerAddressesView;
    }

    public String getCustomerAddressesRedirect() {
        return customerAddressesRedirect;
    }
    
    public String getEditAddressView(){
    	return editAddressView;
    }
    
    public String getAddressUpdatedMessage() {
        return addressUpdatedMessage;
    }

    public String getAddressAddedMessage() {
        return addressAddedMessage;
    }

    public String getAddressRemovedMessage() {
        return addressRemovedMessage;
    }
    
    public String getAddressRemovedErrorMessage() {
        return addressRemovedErrorMessage;
    }
    
}