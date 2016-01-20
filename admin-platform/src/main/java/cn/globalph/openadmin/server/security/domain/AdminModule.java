package cn.globalph.openadmin.server.security.domain;

import java.io.Serializable;
import java.util.List;

public interface AdminModule extends Serializable {

    public Long getId();

    public String getName();

    public void setName(String name);

    public String getModuleKey();

    public void setModuleKey(String moduleKey);

    public String getIcon();

    public void setIcon(String icon);

    public List<AdminSection> getSections();

    public void setSections(List<AdminSection> sections);

    public Integer getDisplayOrder();

    public void setDisplayOrder(Integer displayOrder);

}
