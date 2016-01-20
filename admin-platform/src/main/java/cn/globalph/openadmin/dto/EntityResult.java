package cn.globalph.openadmin.dto;

import java.io.Serializable;

/**
 * The DynamicEntityDao infrastructure provides a generic representation of an entity in 
 * the system.   Some utilities and services want both the generic representation and the
 * entity as it was persisted (e.g. the result of the <code>merge</code> call.
 * 
 * This object returns both properties.
 * 
 * @author bpolster
 * 
 * @see {@link Entity}
 * @see {@link Property}
 *
 */
public class EntityResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private Entity entity;
    private Object entityBackingObject;
    
    public Entity getEntity() {
        return entity;
    }
    
    public void setEntity(Entity entity) {
        this.entity = entity;
    }
    
    public Object getEntityBackingObject() {
        return entityBackingObject;
    }
    
    public void setEntityBackingObject(Object entityBackingObject) {
        this.entityBackingObject = entityBackingObject;
    }
}
