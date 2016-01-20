package cn.globalph.openadmin.server.security.domain;

import java.util.ArrayList;
import java.util.List;

public class AdminModuleDTO implements AdminModule {

    private static final long serialVersionUID = 1L;

    protected Long id;
    protected String name;
    protected String moduleKey;
    protected String icon;
    protected List<AdminSection> sections = new ArrayList<AdminSection>();
    protected Integer displayOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModuleKey() {
        return moduleKey;
    }

    public void setModuleKey(String moduleKey) {
        this.moduleKey = moduleKey;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<AdminSection> getSections() {
        return sections;
    }

    public void setSections(List<AdminSection> sections) {
        this.sections = sections;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
