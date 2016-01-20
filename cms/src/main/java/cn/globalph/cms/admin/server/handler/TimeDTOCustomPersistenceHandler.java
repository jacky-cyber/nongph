package cn.globalph.cms.admin.server.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.TimeDTO;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.openadmin.dto.ClassMetadata;
import cn.globalph.openadmin.dto.DynamicResultSet;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.MergedPropertyType;
import cn.globalph.openadmin.dto.PersistencePackage;
import cn.globalph.openadmin.server.dao.DynamicEntityDao;
import cn.globalph.openadmin.server.service.handler.CustomPersistenceOperationHandlerAdapter;
import cn.globalph.openadmin.server.service.persistence.module.InspectHelper;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("blTimeDTOCustomPersistenceHandler")
public class TimeDTOCustomPersistenceHandler extends CustomPersistenceOperationHandlerAdapter {

    private static final Log LOG = LogFactory.getLog(TimeDTOCustomPersistenceHandler.class);

    @Override
    public Boolean canHandleFetch(PersistencePackage persistencePackage) {
        return canHandleInspect(persistencePackage);
    }

    @Override
    public Boolean canHandleAdd(PersistencePackage persistencePackage) {
        return canHandleInspect(persistencePackage);
    }

    @Override
    public Boolean canHandleRemove(PersistencePackage persistencePackage) {
        return canHandleInspect(persistencePackage);
    }

    @Override
    public Boolean canHandleUpdate(PersistencePackage persistencePackage) {
        return canHandleInspect(persistencePackage);
    }

    @Override
    public Boolean canHandleInspect(PersistencePackage persistencePackage) {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getRootEntityClassname();
        return TimeDTO.class.getName().equals(ceilingEntityFullyQualifiedClassname);
    }

    @Override
    public DynamicResultSet inspect(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, InspectHelper helper) throws ServiceException {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getRootEntityClassname();
        try {
            Map<MergedPropertyType, Map<String, FieldMetadata>> allMergedProperties = new HashMap<MergedPropertyType, Map<String, FieldMetadata>>();
            Map<String, FieldMetadata> mergedProperties = dynamicEntityDao.getSimpleMergedProperties(ceilingEntityFullyQualifiedClassname, persistencePackage.getPersistenceAssociation());
            allMergedProperties.put(MergedPropertyType.PRIMARY, mergedProperties);
            ClassMetadata mergedMetadata = helper.getMergedClassMetadata(new Class<?>[]{Class.forName(ceilingEntityFullyQualifiedClassname)}, allMergedProperties);
            DynamicResultSet results = new DynamicResultSet(mergedMetadata);

            return results;
        } catch (Exception e) {
            ServiceException ex = new ServiceException("Unable to retrieve inspection results for " + persistencePackage.getRootEntityClassname(), e);
            throw ex;
        }
    }
}
