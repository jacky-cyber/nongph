package cn.globalph.openadmin.server.service;

import cn.globalph.common.exception.ServiceException;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.server.service.persistence.module.RecordHelper;


/**
 * Thrown when an entity fails validation when attempting to populate an actual Hibernate entity based on its DTO
 * representation
 *
 * @see {@link RecordHelper#createPopulatedInstance(java.io.Serializable, Entity, java.util.Map, Boolean)}
 * @author Phillip Verheyden (phillipuniverse)
 */
public class ValidationException extends ServiceException {

    private static final long serialVersionUID = 1L;
    
    protected Entity entity;

    public ValidationException(Entity entity, String message) {
        super(message);
        setEntity(entity);
    }
    
    public void setEntity(Entity entity) {
        this.entity = entity;
    }
    
    public Entity getEntity() {
        return entity;
    }
    
}
