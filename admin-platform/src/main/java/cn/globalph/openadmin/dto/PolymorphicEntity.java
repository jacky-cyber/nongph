package cn.globalph.openadmin.dto;

import java.io.Serializable;

/**
 * 
 * @felix.wu
 *
 */
public class PolymorphicEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String type;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
}
