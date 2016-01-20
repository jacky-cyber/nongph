package cn.globalph.openadmin.server.service.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.config.domain.SystemProperty;
import cn.globalph.common.config.service.SystemPropertiesService;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.PersistenceAssociation;
import cn.globalph.openadmin.dto.PersistencePackage;
import cn.globalph.openadmin.server.dao.DynamicEntityDao;
import cn.globalph.openadmin.server.service.persistence.module.RecordHelper;

import org.springframework.stereotype.Component;

import java.util.Map;

import javax.annotation.Resource;

/**
 * Custom persistence handler for SystemProperty to ensure that the value is validated against the type appropriately.
 * 
 * @author Andre Azzolini (apazzolini)
 */
@Component("blSystemPropertyCustomPersistenceHandler")
public class SystemPropertyCustomPersistenceHandler extends CustomPersistenceOperationHandlerAdapter {
    private final Log LOG = LogFactory.getLog(SystemPropertyCustomPersistenceHandler.class);

    @Resource(name = "blSystemPropertiesService")
    protected SystemPropertiesService spService;

    protected Boolean classMatches(PersistencePackage persistencePackage) {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getRootEntityClassname();
        return SystemProperty.class.getName().equals(ceilingEntityFullyQualifiedClassname);
    }

    @Override
    public Boolean canHandleAdd(PersistencePackage persistencePackage) {
        return classMatches(persistencePackage);
    }

    @Override
    public Boolean canHandleUpdate(PersistencePackage persistencePackage) {
        return classMatches(persistencePackage);
    }

    @Override
    public Entity update(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) 
            throws ServiceException {
        Entity entity = persistencePackage.getEntity();
        try {
            // Get an instance of SystemProperty with the updated values from the form
            PersistenceAssociation persistencePerspective = persistencePackage.getPersistenceAssociation();
            Map<String, FieldMetadata> adminProperties = helper.getSimpleMergedProperties(SystemProperty.class.getName(), persistencePerspective);
            Object primaryKey = helper.getPrimaryKey(entity, adminProperties);
            SystemProperty adminInstance = (SystemProperty) dynamicEntityDao.retrieve(Class.forName(entity.getType()[0]), primaryKey);
            adminInstance = (SystemProperty) helper.createPopulatedInstance(adminInstance, entity, adminProperties, false);

            // Verify that the value entered matches up with the type of this property
            Entity errorEntity = validateTypeAndValueCombo(adminInstance);
            if (errorEntity != null) {
                entity.setPropertyValidationErrors(errorEntity.getPropertyValidationErrors());
                return entity;
            }

            adminInstance = (SystemProperty) dynamicEntityDao.merge(adminInstance);

            // Fill out the DTO and add in the product option value properties to it
            return helper.getRecord(adminProperties, adminInstance, null, null);
        } catch (Exception e) {
            throw new ServiceException("Unable to perform fetch for entity: " + SystemProperty.class.getName(), e);
        }
    }

    @Override
    public Entity add(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        Entity entity = persistencePackage.getEntity();
        try {
            // Get an instance of SystemProperty with the updated values from the form
            PersistenceAssociation persistencePerspective = persistencePackage.getPersistenceAssociation();
            SystemProperty adminInstance = (SystemProperty) Class.forName(entity.getType()[0]).newInstance();
            Map<String, FieldMetadata> adminProperties = helper.getSimpleMergedProperties(SystemProperty.class.getName(), persistencePerspective);
            adminInstance = (SystemProperty) helper.createPopulatedInstance(adminInstance, entity, adminProperties, false);
            
            // Verify that the value entered matches up with the type of this property
            Entity errorEntity = validateTypeAndValueCombo(adminInstance);
            if (errorEntity != null) {
                entity.setPropertyValidationErrors(errorEntity.getPropertyValidationErrors());
                return entity;
            }
            
            adminInstance = (SystemProperty) dynamicEntityDao.merge(adminInstance);

            // Fill out the DTO and add in the product option value properties to it
            return helper.getRecord(adminProperties, adminInstance, null, null);
        } catch (Exception e) {
            throw new ServiceException("Unable to perform fetch for entity: " + SystemProperty.class.getName(), e);
        }
    }
    
    protected Entity validateTypeAndValueCombo(SystemProperty prop) {
        if (!spService.isValueValidForType(prop.getValue(), prop.getPropertyType())) {
            Entity errorEntity = new Entity();
            errorEntity.addValidationError("value", "valueIllegalForPropertyType");
            return errorEntity;
        }

        return null;
    }
}
