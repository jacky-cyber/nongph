package cn.globalph.cms.field.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bpolster.
 */
public interface FieldGroup extends Serializable {

    public Long getId();

    public void setId(Long id);

    public String getName();

    public void setName(String name);

    public Boolean getInitCollapsedFlag();

    public void setInitCollapsedFlag(Boolean initCollapsedFlag);

    public List<FieldDefinition> getFieldDefinitions();

    public void setFieldDefinitions(List<FieldDefinition> fieldDefinitions);

}
