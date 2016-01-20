package cn.globalph.common.sandbox.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface SandBox extends Serializable {

    Long getId();

    void setId(Long id);

    /**
     * The name of the sandbox.
     * Certain sandbox names are reserved in the system.    User created
     * sandboxes cannot start with "", "approve_", or "deploy_".
     *
     * @return String sandbox name
     */
    String getName();

    void setName(String name);

    SandBoxType getSandBoxType();

    void setSandBoxType(SandBoxType sandBoxType);

    Long getAuthor();

    void setAuthor(Long author);

    SandBox getParentSandBox();

    void setParentSandBox(SandBox parentSandBox);

    String getColor();

    void setColor(String color);

    Date getGoLiveDate();

    void setGoLiveDate(Date goLiveDate);

    List<Long> getSandBoxIdsForUpwardHierarchy(boolean includeInherited);

    List<Long> getSandBoxIdsForUpwardHierarchy(boolean includeInherited, boolean includeCurrent);

    List<SandBox> getChildSandBoxes();

    void setChildSandBoxes(List<SandBox> childSandBoxes);

    /**
     * @return whether or not this sandbox, or any of its parent sandboxes, has type DEFAULT.
     */
    public boolean getIsInDefaultHierarchy();

}


