package cn.globalph.passport.domain.wrapper;

import cn.globalph.common.domain.wrapper.APIUnwrapper;
import cn.globalph.common.domain.wrapper.APIWrapper;
import cn.globalph.passort.domain.wrap.AddressWrap;
import cn.globalph.passport.domain.Address;
import cn.globalph.passport.service.AddressService;

import javax.annotation.Resource;

public class AddressWrapper implements APIWrapper<Address, AddressWrap>, APIUnwrapper<Address, AddressWrap> {
	
	@Resource(name="blAddressService")
	AddressService addressService;
	 
    @Override
    public AddressWrap wrapDetails(Address model) {
    	AddressWrap wrap = new AddressWrap();
    	wrap.setId( model.getId() );
    	wrap.setReceiver( model.getReceiver() );
    	wrap.setAddress( model.getAddress() );
    	wrap.setPostalCode( model.getPostalCode() );
    	wrap.setIsBusiness( model.isBusiness() );
    	wrap.setIsDefault( model.isDefault() );
    	wrap.setPhone( model.getPhone() );
    	return wrap;
    }

    @Override
    public AddressWrap wrapSummary(Address model) {
        return wrapDetails(model);
    }

    @Override
    public Address unwrap( AddressWrap wrap ) {
        Address address = addressService.create();

        address.setId(wrap.getId());
        address.setReceiver(wrap.getReceiver());
        address.setAddress(wrap.getAddress());
        address.setPostalCode(wrap.getPostalCode());

        if (wrap.getIsBusiness() != null) {
            address.setBusiness(wrap.getIsBusiness());
        }

        if (wrap.getIsDefault() != null) {
            address.setDefault(wrap.getIsDefault());
        }

        if (wrap.getPhone() != null) {
            address.setPhone(wrap.getPhone());
        }

        return address;
    }
}
