package cn.globalph.passport.service;

import cn.globalph.passport.domain.Address;
import cn.globalph.passport.service.exception.AddressVerificationException;

import java.util.List;

public interface AddressService {

    public Address saveAddress(Address address);

    public Address readAddressById(Long addressId);

    public Address create();

    public void delete(Address address);
    
    public void deleteAddressById(Long addressId);

    /**
     * Verifies the address and returns a collection of addresses. If the address was 
     * invalid but close to a match, this method should return a list of one or more addresses that may be valid. 
     * If the address is valid, implementations should return the valid address in the list. 
     * Implementations may set the tokenized address, zip four, and verification level. If the address could not 
     * be validated, implementors should throw an <code>AddressValidationException</code>.
     * 
     * For example, an address may be close, but missing zip four. This service should return 
     * the address in question with zip four populated.
     * @param address
     * @return
     */
    public List<Address> verifyAddress(Address address) throws AddressVerificationException;
    
    public void makeCustomerAddressDefault(Long addressId, Long customerId);
    
    public List<Address> readActiveAddressesByCustomerId(Long customerId);
    
    public Address readDefaultAddressByCustomerId(Long customerId);
}
