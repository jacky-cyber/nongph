package cn.globalph.passport.domain.wrapper;

import cn.globalph.common.domain.wrapper.APIUnwrapper;
import cn.globalph.common.domain.wrapper.APIWrapper;
import cn.globalph.passort.domain.wrap.CustomerWrap;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.service.CustomerService;

import javax.annotation.Resource;

public class CustomerWrapper implements APIWrapper<Customer, CustomerWrap>, APIUnwrapper<Customer, CustomerWrap> {
	
	@Resource(name="blCustomerService")
	CustomerService customerService;
	 
    @Override
    public CustomerWrap wrapDetails(Customer model) {
    	CustomerWrap wrap = new CustomerWrap();
    	wrap.setId( model.getId() );
    	wrap.setName( model.getName() );
    	wrap.setEmailAddress( model.getEmailAddress() );
    	return wrap;
    }

    @Override
    public CustomerWrap wrapSummary(Customer model) {
        return wrapDetails(model);
    }

    @Override
    public Customer unwrap( CustomerWrap wrap ) {
        Customer customer = customerService.getCustomerById(wrap.getId());
        customer.setId( wrap.getId() );
        customer.setName( wrap.getName() );
        customer.setEmailAddress( wrap.getEmailAddress() );
        return customer;
    }
}
