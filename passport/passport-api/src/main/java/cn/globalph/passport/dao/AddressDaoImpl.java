package cn.globalph.passport.dao;

import java.util.List;

import cn.globalph.common.persistence.EntityConfiguration;
import cn.globalph.passport.domain.Address;
import cn.globalph.passport.domain.AddressImpl;

import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository("blAddressDao")
public class AddressDaoImpl implements AddressDao {

	@PersistenceContext(unitName = "blPU")
	protected EntityManager em;

	@Resource(name = "blEntityConfiguration")
	protected EntityConfiguration entityConfiguration;

	public Address save(Address address) {
		return em.merge(address);
	}

	public Address readAddressById(Long id) {
		return (Address) em.find(AddressImpl.class, id);
	}

	public Address create() {
		return (Address) entityConfiguration.createEntityInstance(Address.class.getName());
	}

	public void delete(Address address) {
		if (!em.contains(address)) {
			address = readAddressById(address.getId());
		}
		em.remove(address);
	}

	@Override
	public void deleteAddressById(Long addressId) {
		Address address = readAddressById(addressId);
		if (address != null) {
			em.remove(address);
		}
	}

	@Override
	public void makeCustomerAddressDefault(Long addressId, Long customerId) {
		List<Address> addresses = readActiveAddressesByCustomerId(customerId);
		for (Address address : addresses) {
			address.setDefault(address.getId().equals(addressId));
			em.merge(address);
		}
	}

	@Override
	public List<Address> readActiveAddressesByCustomerId(Long customerId) {
		Query query = em.createNamedQuery("NPH_READ_ACTIVE_ADDRESSES_BY_CUSTOMER_ID");
		query.setParameter("customerId", customerId);
		return query.getResultList();
	}
	@Override
	public Address readDefaultAddressByCustomerId(Long customerId){
		Query query = em.createNamedQuery("NPH_READ_DEFAULT_ADDRESS_BY_CUSTOMER_ID");
		query.setParameter("customerId", customerId);
		List<Address> addresses = query.getResultList();
		if(addresses!=null && addresses.size()!=0){
			return addresses.get(0);
		}else{
			return null;
		}
	}
}
