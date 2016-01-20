package cn.globalph.admin.server.service.handler;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;

import cn.globalph.b2c.catalog.domain.Category;
import cn.globalph.b2c.catalog.domain.CategoryImpl;
import cn.globalph.b2c.catalog.domain.CategoryXref;
import cn.globalph.b2c.catalog.domain.CategoryXrefImpl;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.presentation.client.OperationScope;
import cn.globalph.common.presentation.client.PersistenceAssociationItemType;
import cn.globalph.openadmin.dto.AdornedTargetList;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.PersistencePackage;
import cn.globalph.openadmin.server.dao.DynamicEntityDao;
import cn.globalph.openadmin.server.service.handler.CustomPersistenceOperationHandlerAdapter;
import cn.globalph.openadmin.server.service.persistence.module.RecordHelper;

import org.springframework.stereotype.Component;

@Component("blChildCategoriesCustomPersistenceHandler")
public class ChildCategoriesCustomPersistenceHandler extends CustomPersistenceOperationHandlerAdapter {

    @Override
    public Boolean canHandleAdd(PersistencePackage persistencePackage) {
        return (!ArrayUtils.isEmpty(persistencePackage.getCustomCriteria()) && persistencePackage.getCustomCriteria()[0].equals("blcAllParentCategories"));
    }

    @Override
    public Entity add(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        AdornedTargetList adornedTargetList = (AdornedTargetList) persistencePackage.getPersistenceAssociation().getPersistenceAssociationItems().get(PersistenceAssociationItemType.ADORNEDTARGETLIST);
        String targetPath = adornedTargetList.getTargetObjectPath() + "." + adornedTargetList.getTargetIdProperty();
        String linkedPath = adornedTargetList.getLinkedObjectPath() + "." + adornedTargetList.getLinkedIdProperty();
        
        Long parentId = Long.parseLong(persistencePackage.getEntity().findProperty(linkedPath).getValue());
        Long childId = Long.parseLong(persistencePackage.getEntity().findProperty(targetPath).getValue());
        
        Category parent = (Category) dynamicEntityDao.retrieve(CategoryImpl.class, parentId);
        Category child = (Category) dynamicEntityDao.retrieve(CategoryImpl.class, childId);

        CategoryXref categoryXref = new CategoryXrefImpl();
        categoryXref.setSubCategory(child);
        categoryXref.setCategory(parent);
        if (parent.getAllChildCategoryXrefs().contains(categoryXref)) {
            throw new ServiceException("Add unsuccessful. Cannot add a duplicate child category.");
        }

        checkParents(child, parent);
        
        return helper.getCompatibleModule(OperationScope.ADORNEDTARGETLIST).add(persistencePackage);
    }
    
    protected void checkParents(Category child, Category parent) throws ServiceException {
        if (child.getId().equals(parent.getId())) {
            throw new ServiceException("Add unsuccessful. Cannot add a category to itself.");
        }
        for (CategoryXref category : parent.getAllParentCategoryXrefs()) {
            if (!CollectionUtils.isEmpty(category.getCategory().getAllParentCategoryXrefs())) {
                checkParents(child, category.getCategory());
            }
        }
    }
 }
