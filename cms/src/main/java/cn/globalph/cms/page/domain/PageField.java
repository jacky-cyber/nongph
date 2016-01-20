package cn.globalph.cms.page.domain;

import java.io.Serializable;

import cn.globalph.openadmin.audit.AdminAuditable;

/**
 * Created by bpolster.
 */
public interface PageField extends Serializable {

    public Long getId();

    public void setId(Long id);

    public String getFieldKey();

    public void setFieldKey(String fieldKey);

    public String getValue();

    public void setValue(String value);

    public AdminAuditable getAuditable();

    public void setAuditable(AdminAuditable auditable);

}
