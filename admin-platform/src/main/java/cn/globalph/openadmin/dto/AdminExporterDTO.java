package cn.globalph.openadmin.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author Phillip Verheyden
 */
public class AdminExporterDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String name;
    protected String friendlyName;
    protected List<Property> additionalCriteriaProperties;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getFriendlyName() {
        return friendlyName;
    }
    
    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }
    
    public List<Property> getAdditionalCriteriaProperties() {
        return additionalCriteriaProperties;
    }
    
    public void setAdditionalCriteriaProperties(List<Property> additionalCriteriaProperties) {
        this.additionalCriteriaProperties = additionalCriteriaProperties;
    }
    
    
}
