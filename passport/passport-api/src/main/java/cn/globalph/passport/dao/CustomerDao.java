package cn.globalph.passport.dao;

import java.util.List;

import cn.globalph.common.dao.BasicEntityDao;
import cn.globalph.passport.domain.Customer;

public interface CustomerDao extends BasicEntityDao<Customer> {
    
    /**
     * Returns the first customer that match the passed in username.
     * 
     * @param username
     * @return
     */
    public Customer readCustomerByUsername(String username);
    
    /**
     * Returns all customers that match the passed in username.
     * 
     * @param username
     * @return
     */
    public List<Customer> readCustomersByUsername(String username);


    /**
     * Returns the first customer that matches the passed in email.
     *
     * @param emailAddress
     * @return
     */
    public Customer readCustomerByEmail(String emailAddress);

    /**
     * 根据手机号查找用户
     *
     * @param phone
     * @return
     */
    public Customer readCustomerByPhone(String phone);

    /**
     * Returns all customers that matches the passed in email.
     *
     * @param emailAddress
     * @return
     */
    public List<Customer> readCustomersByEmail(String emailAddress);

    public Customer create();

    Customer save(Customer customer);

}
