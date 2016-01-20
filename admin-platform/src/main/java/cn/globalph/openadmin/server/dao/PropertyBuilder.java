package cn.globalph.openadmin.server.dao;

import cn.globalph.openadmin.dto.FieldMetadata;

import java.util.Map;

/**
 * @author Jeff Fischer
 */
public interface PropertyBuilder {

    public Map<String, FieldMetadata> execute(Boolean overridePopulateManyToOne);

}
