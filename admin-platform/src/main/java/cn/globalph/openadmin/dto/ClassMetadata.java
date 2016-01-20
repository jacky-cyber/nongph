package cn.globalph.openadmin.dto;

import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.common.util.BLCMapUtils;
import cn.globalph.common.util.TypedClosure;

import java.io.Serializable;
import java.util.Map;

public class ClassMetadata implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String className;
    private String securityCeilingType;
    private ClassTree polymorphicEntities;
    private Property[] properties;
    
    private Map<String, Property> pMap = null;

    public Map<String, Property> getPMap() {
        if (pMap == null) {
            pMap = BLCMapUtils.keyedMap(properties, new TypedClosure<String, Property>() {

                @Override
                public String getKey(Property value) {
                    return value.getName();
                }
            });
        }
        return pMap;
    }

    public String getClassName() {
        return className;
    }
    
    public void setClassName(String type) {
        this.className = type;
    }

    /**
     * For dynamic forms, the type to check security permissions will be different than the type used to generate the 
     * forms.   For example, a user with "ADD" or "UPDATE" permissions on STRUCTURE_CONTENT does not need 
     * to have the same level of access to StructuredContentTemplate.   
     * 
     * @param type
     */
    public String getSecurityCeilingType() {
        return securityCeilingType;
    }

    public void setSecurityCeilingType(String securityCeilingType) {
        this.securityCeilingType = securityCeilingType;
    }

    public ClassTree getPolymorphicEntities() {
        return polymorphicEntities;
    }

    public void setPolymorphicEntities(ClassTree polymorphicEntities) {
        this.polymorphicEntities = polymorphicEntities;
    }

    public Property[] getProperties() {
        return properties;
    }
    
    public void setProperties(Property[] property) {
        this.properties = property;
    }
    
    public String getIdProperty(){
	    for (Property p : getProperties()) {
	        if (p.getFieldMetadata() instanceof BasicFieldMetadata) {
	            BasicFieldMetadata fmd = (BasicFieldMetadata) p.getFieldMetadata();
	            //check for ID type and also make sure the field we're looking at is not a "ToOne" association
	            if (SupportedFieldType.ID == fmd.getFieldType() && !p.getName().contains(".")) {
	                return p.getName();
	            }
	        }
	    }
	    return null;
    }
}
