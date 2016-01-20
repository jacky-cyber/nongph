package cn.globalph.passport.dao;

import java.util.List;

import cn.globalph.passport.domain.Address;

public interface AddressDao {

    public Address save(Address address);

    public Address readAddressById(Long addressId);

    public Address create();

    public void delete(Address address);
    
    public void deleteAddressById(Long addressId);
    
    public void makeCustomerAddressDefault(Long addressId, Long customerId);
    
    public List<Address> readActiveAddressesByCustomerId(Long customerId);
    
    public Address readDefaultAddressByCustomerId(Long customerId);

}
