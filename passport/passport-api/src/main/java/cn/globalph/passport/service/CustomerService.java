package cn.globalph.passport.service;

import cn.globalph.common.security.util.PasswordChange;
import cn.globalph.common.security.util.PasswordReset;
import cn.globalph.common.service.GenericResponse;
import cn.globalph.passport.domain.Address;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.service.handler.PasswordUpdatedHandler;
import cn.globalph.passport.service.listener.PostRegistrationObserver;

import org.springframework.security.authentication.dao.SaltSource;

import java.util.List;

public interface CustomerService {

    public Customer saveCustomer(Customer customer);

    public Customer saveCustomer(Customer customer, boolean register);

    public Customer registerCustomer(Customer customer, String password, String passwordConfirm, Boolean registerFromEmail);

    /**
     *
     * @param loginName
     * @param password
     * @param name
     * @param phone
     * @return
     */
    Customer registerCustomer(String loginName, String password, String name, String phone);

    public Customer readCustomerByUsername(String customerName);

    public Customer readCustomerByEmail(String emailAddress);

    public Customer readCustomerByPhone(String phone);

    public Customer changePassword(PasswordChange passwordChange);

    public Customer getCustomerById(Long userId);

    public Customer createCustomer();

    /**
     * Delete the customer entity from the persistent store
     *
     * @param customer the customer entity to remove
     */
    void deleteCustomer(Customer customer);

    /**
     * Returns a <code>Customer</code> by first looking in the database, otherwise creating a new non-persisted <code>Customer</code>
     * @param customerId the id of the customer to lookup
     * @return either a <code>Customer</code> from the database if it exists, or a new non-persisted <code>Customer</code>
     */
    public Customer createCustomerFromId(Long customerId);
    
    /**
     * Returns a non-persisted <code>Customer</code>.    Typically used with registering a new customer.
     */
    public Customer createNewCustomer();

    public void addPostRegisterListener(PostRegistrationObserver postRegisterListeners);

    public void removePostRegisterListener(PostRegistrationObserver postRegisterListeners);
    
    public Customer resetPassword(PasswordReset passwordReset);
    
    public List<PasswordUpdatedHandler> getPasswordResetHandlers();

    public void setPasswordResetHandlers(List<PasswordUpdatedHandler> passwordResetHandlers);
    
    public List<PasswordUpdatedHandler> getPasswordChangedHandlers();

    public void setPasswordChangedHandlers(List<PasswordUpdatedHandler> passwordChangedHandlers);
    
    /**
     * Looks up the corresponding Customer and emails the address on file with
     * the associated username.
     *
     * @param emailAddress
     * @return Response can contain errors including (notFound)
     */
    GenericResponse sendForgotUsernameNotification(String emailAddress);

    /**
     * Generates an access token and then emails the user.
     *
     * @param userName - the user to send a reset password email to.
     * @param forgotPasswordUrl - Base url to include in the email.
     * @return Response can contain errors including (invalidEmail, invalidUsername, inactiveUser)
     * 
     */
    GenericResponse sendForgotPasswordNotification(String userName, String forgotPasswordUrl);
    
    public GenericResponse sendEmailValidationNotification(Long customerId, String emailValidationUrl);
    
    /**
     * Updates the password for the passed in customer only if the passed
     * in token is valid for that customer.
     *
     * @param username Username of the customer
     * @param token Valid reset token
     * @param password new password
     *
     * @return Response can contain errors including (invalidUsername, inactiveUser, invalidToken, invalidPassword, tokenExpired)
     */
    GenericResponse resetPasswordUsingToken(String username, String token, String password, String confirmPassword);
    
    /**
     * Verifies that the passed in token is valid.   
     * 
     * Returns responseCodes of "invalidToken", "tokenUsed", and "tokenExpired".
     * @param token
     * @return
     */
    public GenericResponse checkPasswordResetToken(String token);
    
    /**
     * Returns the {@link SaltSource} used with the blPasswordEncoder to encrypt the user password. Usually configured in
     * applicationContext-security.xml. This is not a required property and will return null if not configured
     */
    public SaltSource getSaltSource();
    
    /**
     * Sets the {@link SaltSource} used with blPasswordencoder to encrypt the user password. Usually configured within
     * applicationContext-security.xml
     * 
     * @param saltSource
     */
    public void setSaltSource(SaltSource saltSource);
    
    /**
     * @deprecated use {@link #getSalt(Customer, String)} instead
     */
    @Deprecated
    public Object getSalt(Customer customer);
    
    /**
     * Gets the salt object for the current customer. By default this delegates to {@link #getSaltSource()}. If there is
     * not a {@link SaltSource} configured ({@link #getSaltSource()} returns null) then this also returns null.
     * 
     * @param customer
     * @return the salt for the current customer
     */
    public Object getSalt(Customer customer, String unencodedPassword);
    
    /**
     * Encodes the clear text parameter, using the customer as a potential Salt. Does not change the customer properties. 
     * This method only encodes the password and returns the encoded result.
     * @param clearText
     * @param customer
     * @return
     */
    public String encodePassword(String clearText, Customer customer);

    /**
     * Use this to determine if passwords match. Don't encode the password separately since sometimes salts 
     * are generated randomly and stored with the password.
     * 
     * @param rawPassword
     * @param encodedPassword
     * @param customer
     * @return
     */
    public boolean isPasswordValid(String rawPassword, String encodedPassword, Customer customer);

    /**
     * 得到客户的收货地址
     * @param customerId
     * @return
     */
    public List<Address> readAddressesByCustomerId(Long customerId);
    
    /**
     * 得到用户默认收货地址
     * @param customerId
     * @return
     */
    public Address readDefaultAddressByCustomerId(Long customerId);
}
