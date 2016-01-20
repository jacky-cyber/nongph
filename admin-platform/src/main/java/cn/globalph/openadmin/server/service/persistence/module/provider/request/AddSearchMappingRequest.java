package cn.globalph.openadmin.server.service.persistence.module.provider.request;

import cn.globalph.openadmin.dto.CriteriaTransferObject;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.PersistenceAssociation;
import cn.globalph.openadmin.server.service.persistence.module.DataFormatProvider;
import cn.globalph.openadmin.server.service.persistence.module.FieldManager;
import cn.globalph.openadmin.server.service.persistence.module.criteria.RestrictionFactory;

import java.util.Map;

/**
 * Contains the requested ctoConverter, cto and support classes.
 *
 * @author Jeff Fischer
 */
public class AddSearchMappingRequest {

    private final PersistenceAssociation persistenceAssociation;
    private final CriteriaTransferObject requestedCto;
    private final String rootClassname;
    private final Map<String, FieldMetadata> mergedProperties;
    private final String propertyName;
    private final FieldManager fieldManager;
    private final DataFormatProvider dataFormatProvider;
    private final RestrictionFactory restrictionFactory;

    public AddSearchMappingRequest(PersistenceAssociation pa, 
    		                       CriteriaTransferObject
                                   requestedCto, 
                                   String rootClassname, 
                                   Map<String, FieldMetadata> mergedProperties,
                                   String propertyName, 
                                   FieldManager fieldManager,
                                   DataFormatProvider dataFormatProvider, 
                                   RestrictionFactory restrictionFactory) {
        this.persistenceAssociation = pa;
        this.requestedCto = requestedCto;
        this.rootClassname = rootClassname;
        this.mergedProperties = mergedProperties;
        this.propertyName = propertyName;
        this.fieldManager = fieldManager;
        this.dataFormatProvider = dataFormatProvider;
        this.restrictionFactory = restrictionFactory;
    }

    public PersistenceAssociation getPersistenceAssociation() {
        return persistenceAssociation;
    }

    public CriteriaTransferObject getRequestedCto() {
        return requestedCto;
    }

    public String getRootClassname() {
        return rootClassname;
    }

    public Map<String, FieldMetadata> getMergedProperties() {
        return mergedProperties;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public FieldManager getFieldManager() {
        return fieldManager;
    }
    
    public DataFormatProvider getDataFormatProvider() {
        return dataFormatProvider;
    }

    public RestrictionFactory getRestrictionFactory() {
        return restrictionFactory;
    }
}
