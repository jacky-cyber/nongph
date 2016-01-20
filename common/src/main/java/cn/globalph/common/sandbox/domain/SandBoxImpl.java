package cn.globalph.common.sandbox.domain;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.admin.domain.AdminMainEntity;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.common.presentation.client.VisibilityEnum;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="ADMIN_SANDBOX")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blSandBoxElements")
public class SandBoxImpl implements SandBox, AdminMainEntity {

    private static final Log LOG = LogFactory.getLog(SandBoxImpl.class);
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "SandBoxId")
    @GenericGenerator(
        name="SandBoxId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="SandBoxImpl"),
            @Parameter(name="entity_name", value="cn.globalph.common.sandbox.domain.SandBoxImpl")
        }
    )
    @Column(name = "SANDBOX_ID")
    @AdminPresentation(visibility = VisibilityEnum.HIDDEN_ALL)
    protected Long id;
    
    @Column(name = "SANDBOX_NAME")
    @Index(name="SANDBOX_NAME_INDEX", columnNames={"SANDBOX_NAME"})
    @AdminPresentation(friendlyName = "SandBoxImpl_Name", group = "SandBoxImpl_Description", prominent = true, 
        gridOrder = 2000, order = 1000)
    protected String name;
    
    @Column(name="AUTHOR")
    @AdminPresentation(friendlyName = "SandBoxImpl_Author", group = "SandBoxImpl_Description", prominent = true, 
        gridOrder = 3000, order = 3000, visibility = VisibilityEnum.FORM_HIDDEN)
    protected Long author;

    @Column(name = "SANDBOX_TYPE")
    @AdminPresentation(friendlyName = "SandBoxImpl_SandBox_Type", group = "SandBoxImpl_Description",
        visibility = VisibilityEnum.HIDDEN_ALL, readOnly = true,
        fieldType = SupportedFieldType.BROADLEAF_ENUMERATION,
        broadleafEnumeration="cn.globalph.common.sandbox.domain.SandBoxType")
    //need to set a default value so that add sandbox works correctly in the admin
    protected String sandboxType = SandBoxType.APPROVAL.getType();

    @ManyToOne(targetEntity = SandBoxImpl.class)
    @JoinColumn(name = "PARENT_SANDBOX_ID")
    protected SandBox parentSandBox;

    @OneToMany(mappedBy = "parentSandBox", targetEntity = SandBoxImpl.class)
    protected List<SandBox> childSandBoxes;

    @Column(name = "COLOR")
    @AdminPresentation(friendlyName = "SandBoxImpl_Color", group = "SandBoxImpl_Description", 
        prominent = true, gridOrder = 1000, fieldType = SupportedFieldType.COLOR, order = 2000)
    protected String color;

    @Column(name = "DESCRIPTION")
    @AdminPresentation(friendlyName = "SandBoxImpl_Description", group = "SandBoxImpl_Description",
        prominent = true, gridOrder = 4000, order = 4000)
    protected String description;

    /*
     * This field should not be used until logic for it is implemented.
     * 
     * @AdminPresentation(friendlyName = "SandBoxImpl_Go_Live_Date", group = "SandBoxImpl_Description",
     *   prominent = true, gridOrder = 5000, order = 3000)
    */
    @Column(name = "GO_LIVE_DATE")
    protected Date goLiveDate;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public SandBoxType getSandBoxType() {
        return SandBoxType.getInstance(sandboxType);
    }

    @Override
    public void setSandBoxType(final SandBoxType sandboxType) {
        if (sandboxType != null) {
            this.sandboxType = sandboxType.getType();
        }
    }

    @Override
    public Long getAuthor() {
        return author;
    }

    @Override
    public void setAuthor(Long author) {
        this.author = author;
    }

    @Override
    public SandBox getParentSandBox() {
        return parentSandBox;
    }

    @Override
    public void setParentSandBox(SandBox parentSandBox) {
        this.parentSandBox = parentSandBox;
    }

    @Override
    public String getColor() {
        if (StringUtils.isNotBlank(color)) {
            return color;
        }

        if (parentSandBox != null) {
            return parentSandBox.getColor();
        }

        return null;
    }

    @Override
    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public Date getGoLiveDate() {
        return goLiveDate;
    }

    @Override
    public void setGoLiveDate(Date goLiveDate) {
        this.goLiveDate = goLiveDate;
    }

    public List<SandBox> getChildSandBoxes() {
        return childSandBoxes;
    }

    public void setChildSandBoxes(List<SandBox> childSandBoxes) {
        this.childSandBoxes = childSandBoxes;
    }

    @Override
    public List<Long> getSandBoxIdsForUpwardHierarchy(boolean includeInherited) {
        return getSandBoxIdsForUpwardHierarchy(includeInherited, true);
    }

    @Override
    public List<Long> getSandBoxIdsForUpwardHierarchy(boolean includeInherited, boolean includeCurrent) {
        List<Long> ids = new ArrayList<Long>();
        if (includeCurrent) {
            ids.add(this.getId());
        }
        if (includeInherited) {
            SandBox current = this;
            while (current.getParentSandBox() != null) {
                current = current.getParentSandBox();
                ids.add(current.getId());
            }
            Collections.reverse(ids);
        }
        return ids;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(1, 31)
            .append(author)
            .append(id)
            .append(name)
            .append(color)
            .append(goLiveDate)
            .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SandBoxImpl) {
            SandBoxImpl other = (SandBoxImpl) obj;
            return new EqualsBuilder()
                .append(author, other.author)
                .append(id, other.id)
                .append(name, other.name)
                .append(color, other.color)
                .append(goLiveDate, other.goLiveDate)
                .build();
        }
        return false;
    }

    @Override
    public String getMainEntityName() {
        return getName();
    }
    
    @Override
    public boolean getIsInDefaultHierarchy() {
        if (SandBoxType.DEFAULT.equals(getSandBoxType())) {
            return true;
        }

        if (getParentSandBox() != null) {
            return getParentSandBox().getIsInDefaultHierarchy();
        }
        
        return false;
    }

}
