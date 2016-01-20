package cn.globalph.admin.server.service.handler;

import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.security.util.PasswordReset;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.PersistencePackage;
import cn.globalph.openadmin.server.dao.DynamicEntityDao;
import cn.globalph.openadmin.server.service.handler.CustomPersistenceOperationHandlerAdapter;
import cn.globalph.openadmin.server.service.persistence.module.RecordHelper;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.service.CustomerService;

import org.hibernate.tool.hbm2x.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 
 * @felix.wu
 *
 */
@Component("blCustomerPasswordCustomPersistenceHandler")
public class CustomerPasswordCustomPersistenceHandler extends CustomPersistenceOperationHandlerAdapter {
    
    @Resource(name="blCustomerService")
    protected CustomerService customerService;

    @Override
    public Boolean canHandleUpdate(PersistencePackage persistencePackage) {
        String[] customCriteria = persistencePackage.getCustomCriteria();
        return customCriteria != null && customCriteria.length > 0 && customCriteria[0].equals("passwordUpdate");
    }

    @Override
    public Entity update(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        Entity entity = persistencePackage.getEntity();
        Customer customer = customerService.readCustomerByUsername(entity.findProperty("username").getValue());
        if (StringUtils.isEmpty(customer.getEmailAddress())) {
            throw new ServiceException("Unable to update password because an email address is not available for this customer. An email address is required to send the customer the new system generated password.");
        }
        
        PasswordReset passwordReset = new PasswordReset();
        passwordReset.setUsername(entity.findProperty("username").getValue());
        passwordReset.setPasswordChangeRequired(false);
        passwordReset.setEmail(customer.getEmailAddress());
        passwordReset.setPasswordLength(22);
        passwordReset.setSendResetEmailReliableAsync(false);
        
        customer = customerService.resetPassword(passwordReset);
        
        return entity;
    }
    
    
    
}
