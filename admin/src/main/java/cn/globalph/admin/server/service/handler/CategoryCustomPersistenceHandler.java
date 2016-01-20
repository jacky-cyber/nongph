package cn.globalph.admin.server.service.handler;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.b2c.catalog.domain.Category;
import cn.globalph.b2c.catalog.domain.CategoryXref;
import cn.globalph.b2c.catalog.domain.CategoryXrefImpl;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.ForeignKey;
import cn.globalph.openadmin.dto.MergedPropertyType;
import cn.globalph.openadmin.dto.PersistenceAssociation;
import cn.globalph.openadmin.dto.PersistencePackage;
import cn.globalph.openadmin.server.dao.DynamicEntityDao;
import cn.globalph.openadmin.server.service.handler.CustomPersistenceOperationHandlerAdapter;
import cn.globalph.openadmin.server.service.persistence.module.RecordHelper;

import org.springframework.stereotype.Component;

/**
 * 
 * @felix.wu
 *
 */
@Component("blCategoryCustomPersistenceHandler")
public class CategoryCustomPersistenceHandler extends CustomPersistenceOperationHandlerAdapter {
    
    private static final Log LOG = LogFactory.getLog(CategoryCustomPersistenceHandler.class);

    @Override
    public Boolean canHandleAdd(PersistencePackage persistencePackage) {
        String ceilingEntityClassname = persistencePackage.getRootEntityClassname();
        String[] customCriteria = persistencePackage.getCustomCriteria();
        return !ArrayUtils.isEmpty(customCriteria) && "addNewCategory".equals(customCriteria[0]) && Category.class.getName().equals(ceilingEntityClassname);
    }

    @Override
    public Entity add(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        Entity entity  = persistencePackage.getEntity();
        try {
            PersistenceAssociation persistencePerspective = persistencePackage.getPersistenceAssociation();
            Category adminInstance = (Category) Class.forName(entity.getType()[0]).newInstance();
            Map<String, FieldMetadata> adminProperties = helper.getSimpleMergedProperties(Category.class.getName(), persistencePerspective);
            adminInstance = (Category) helper.createPopulatedInstance(adminInstance, entity, adminProperties, false);

            CategoryXref categoryXref = new CategoryXrefImpl();
            categoryXref.setCategory(adminInstance.getDefaultParentCategory());
            categoryXref.setSubCategory(adminInstance);
            if (adminInstance.getDefaultParentCategory() != null && !adminInstance.getAllParentCategoryXrefs().contains(categoryXref)) {
                adminInstance.getAllParentCategoryXrefs().add(categoryXref);
            }

            adminInstance = (Category) dynamicEntityDao.merge(adminInstance);

            return helper.getRecord(adminProperties, adminInstance, null, null);
        } catch (Exception e) {
            throw new ServiceException("Unable to add entity for " + entity.getType()[0], e);
        }
    }

    protected Map<String, FieldMetadata> getMergedProperties(Class<?> ceilingEntityFullyQualifiedClass, DynamicEntityDao dynamicEntityDao, Boolean populateManyToOneFields, String[] includeManyToOneFields, String[] excludeManyToOneFields, String configurationKey) throws ClassNotFoundException, SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        Class<?>[] entities = dynamicEntityDao.getAllPolymorphicEntitiesFromCeiling(ceilingEntityFullyQualifiedClass);
        return dynamicEntityDao.getMergedProperties(
            ceilingEntityFullyQualifiedClass.getName(), 
            entities, 
            null, 
            new String[]{}, 
            new ForeignKey[]{},
            MergedPropertyType.PRIMARY,
            populateManyToOneFields,
            includeManyToOneFields, 
            excludeManyToOneFields,
            configurationKey,
            ""
        );
    }
}
