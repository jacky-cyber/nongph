package cn.globalph.openadmin.server.service.persistence.module.provider.request;

import cn.globalph.openadmin.dto.Entity;

/**
 * Contains the {@link Entity} instance and unfiltered property list.
 *
 * @author Jeff Fischer
 */
public class AddFilterPropertiesRequest {

    private final Entity entity;

    public AddFilterPropertiesRequest(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

}
