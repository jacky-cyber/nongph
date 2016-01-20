package cn.globalph.openadmin.server.security.domain;

import java.io.Serializable;
import java.util.List;

public interface AdminSection extends Serializable {

    public Long getId();

    public String getName();

    public void setName(String name);

    public String getSectionKey();

    public void setSectionKey(String sectionKey);

    public String getUrl();

    public void setUrl(String url);

    public List<AdminPermission> getPermissions();

    public void setPermissions(List<AdminPermission> permissions);

    /**
     * No longer needed after GWT removal
     * @param displayController
     */
    @Deprecated
    public void setDisplayController(String displayController);

    /**
     * No longer needed after GWT removal
     * @param displayController
     */
    @Deprecated
    public String getDisplayController();

    public AdminModule getModule();

    public void setModule(AdminModule module);

    /**
     * No longer needed after GWT removal
     * @param displayController
     */
    @Deprecated
    public Boolean getUseDefaultHandler();

    /**
     * No longer needed after GWT removal
     * @param displayController
     */
    @Deprecated
    public void setUseDefaultHandler(Boolean useDefaultHandler);

    public String getCeilingEntity();

    public void setCeilingEntity(String ceilingEntity);

    public Integer getDisplayOrder();

    public void setDisplayOrder(Integer displayOrder);
}
