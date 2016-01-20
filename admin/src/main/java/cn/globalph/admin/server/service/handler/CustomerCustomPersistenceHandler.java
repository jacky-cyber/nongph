package cn.globalph.admin.server.service.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.exception.ServiceException;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.PersistenceAssociation;
import cn.globalph.openadmin.dto.PersistencePackage;
import cn.globalph.openadmin.server.dao.DynamicEntityDao;
import cn.globalph.openadmin.server.service.handler.CustomPersistenceOperationHandlerAdapter;
import cn.globalph.openadmin.server.service.persistence.module.RecordHelper;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.service.CustomerService;

import org.springframework.stereotype.Component;

import java.util.Map;

import javax.annotation.Resource;

@Component("blCustomerCustomPersistenceHandler")
public class CustomerCustomPersistenceHandler extends CustomPersistenceOperationHandlerAdapter {

    private static final Log LOG = LogFactory.getLog(CustomerCustomPersistenceHandler.class);

    @Resource(name="blCustomerService")
    protected CustomerService customerService;

    @Override
    public Boolean canHandleAdd(PersistencePackage persistencePackage) {
        return persistencePackage.getRootEntityClassname() != null && persistencePackage.getRootEntityClassname().equals(Customer.class.getName());
    }

    @Override
    public Entity add(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        Entity entity  = persistencePackage.getEntity();
        try {
            PersistenceAssociation persistencePerspective = persistencePackage.getPersistenceAssociation();
            Customer adminInstance = (Customer) Class.forName(entity.getType()[0]).newInstance();
            Map<String, FieldMetadata> adminProperties = helper.getSimpleMergedProperties(Customer.class.getName(), persistencePerspective);
            adminInstance = (Customer) helper.createPopulatedInstance(adminInstance, entity, adminProperties, false);
            
            if (customerService.readCustomerByUsername(adminInstance.getLoginName()) != null) {
                Entity error = new Entity();
                error.addValidationError("username", "nonUniqueUsernameError");
                return error;
            }
            
            adminInstance = (Customer) dynamicEntityDao.merge(adminInstance);
            Entity adminEntity = helper.getRecord(adminProperties, adminInstance, null, null);

            return adminEntity;
        } catch (Exception e) {
            LOG.error("Unable to execute persistence activity", e);
            throw new ServiceException("Unable to add entity for " + entity.getType()[0], e);
        }
    }
}
