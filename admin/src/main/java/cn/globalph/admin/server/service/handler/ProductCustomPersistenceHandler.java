package cn.globalph.admin.server.service.handler;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.b2c.catalog.service.CatalogService;
import cn.globalph.b2c.product.domain.Product;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.PersistenceAssociation;
import cn.globalph.openadmin.dto.PersistencePackage;
import cn.globalph.openadmin.server.dao.DynamicEntityDao;
import cn.globalph.openadmin.server.service.handler.CustomPersistenceOperationHandlerAdapter;
import cn.globalph.openadmin.server.service.persistence.module.RecordHelper;

import org.springframework.stereotype.Component;

import java.util.Map;

import javax.annotation.Resource;

/**
 * @author felix.wu
 */
@Component("blProductCustomPersistenceHandler")
public class ProductCustomPersistenceHandler extends CustomPersistenceOperationHandlerAdapter {
    
    @Resource(name = "blCatalogService")
    protected CatalogService catalogService;

    private static final Log LOG = LogFactory.getLog(ProductCustomPersistenceHandler.class);

    @Override
    public Boolean canHandleAdd(PersistencePackage persistencePackage) {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getRootEntityClassname();
        String[] customCriteria = persistencePackage.getCustomCriteria();
        return !ArrayUtils.isEmpty(customCriteria) && "productDirectEdit".equals(customCriteria[0]) && Product.class.getName().equals(ceilingEntityFullyQualifiedClassname);
    }

    @Override
    public Boolean canHandleUpdate(PersistencePackage persistencePackage) {
        return canHandleAdd(persistencePackage);
    }

    @Override
    public Entity add(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        Entity entity  = persistencePackage.getEntity();
        try {
            PersistenceAssociation persistencePerspective = persistencePackage.getPersistenceAssociation();
            Product adminInstance = (Product) Class.forName(entity.getType()[0]).newInstance();
            Map<String, FieldMetadata> adminProperties = helper.getSimpleMergedProperties(Product.class.getName(), persistencePerspective);
            
            adminInstance = (Product) helper.createPopulatedInstance(adminInstance, entity, adminProperties, false);
           
            adminInstance = (Product) dynamicEntityDao.merge(adminInstance);
                 
                /*
            CategoryProductXref categoryXref = new CategoryProductXrefImpl();
            categoryXref.setCategory( adminInstance.getDefaultCategory() );
            categoryXref.setProduct( adminInstance );
            if ( adminInstance.getDefaultCategory() != null && !adminInstance.getAllParentCategoryXrefs().contains(categoryXref)) {
                categoryXref = (CategoryProductXref) dynamicEntityDao.merge(categoryXref);
                adminInstance.getAllParentCategoryXrefs().add(categoryXref);
                }
                */
            
            return helper.getRecord(adminProperties, adminInstance, null, null);
        } catch (Exception e) {
            throw new ServiceException("Unable to add entity for " + entity.getType()[0], e);
        }
    }

    @Override
    public Entity update(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        Entity entity = persistencePackage.getEntity();
        try {
            PersistenceAssociation persistencePerspective = persistencePackage.getPersistenceAssociation();
            Map<String, FieldMetadata> adminProperties = helper.getSimpleMergedProperties(Product.class.getName(), persistencePerspective);
            Object primaryKey = helper.getPrimaryKey(entity, adminProperties);
            Product adminInstance = (Product) dynamicEntityDao.retrieve(Class.forName(entity.getType()[0]), primaryKey);
            
            adminInstance = (Product) helper.createPopulatedInstance(adminInstance, entity, adminProperties, false);
            adminInstance = (Product) dynamicEntityDao.merge(adminInstance);

                /*
            CategoryProductXref categoryXref = new CategoryProductXrefImpl();
            categoryXref.setCategory(adminInstance.getDefaultCategory());
            categoryXref.setProduct(adminInstance);
            if (adminInstance.getDefaultCategory() != null && !adminInstance.getAllParentCategoryXrefs().contains(categoryXref)) {
                adminInstance.getAllParentCategoryXrefs().add(categoryXref);
                }
                */
            
            return helper.getRecord(adminProperties, adminInstance, null, null);
        } catch (Exception e) {
            throw new ServiceException("Unable to update entity for " + entity.getType()[0], e);
        }
    }
}
