package cn.globalph.openadmin.server.service.persistence.module;

import cn.globalph.common.presentation.client.OperationScope;
import cn.globalph.openadmin.dto.ClassMetadata;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.MergedPropertyType;
import cn.globalph.openadmin.dto.PersistenceAssociation;

import java.util.Map;

/**
 * 
 * @felix.wu
 *
 */
public interface InspectHelper {

    public ClassMetadata getMergedClassMetadata(Class<?>[] entities, Map<MergedPropertyType, Map<String, FieldMetadata>> mergedProperties);
    
    public Map<String, FieldMetadata> getSimpleMergedProperties(String entityName, PersistenceAssociation persistencePerspective);

    public PersistenceModule getCompatibleModule(OperationScope operationType);
    
}
