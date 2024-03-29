package cn.globalph.common.config.domain;

import cn.globalph.common.admin.domain.AdminMainEntity;
import cn.globalph.common.config.service.type.SystemPropertyFieldType;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.client.SupportedFieldType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 * Allows the storage and retrieval of System Properties in the database
 * <p/>
 * User: Kelly Tisdell
 * Date: 6/20/12
 */
@Entity
@Table(name="NPH_SYSTEM_PROPERTY")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blStandardElements")
@AdminPresentationClass(friendlyName = "SystemPropertyImpl")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_SITE),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX)
})
public class SystemPropertyImpl implements SystemProperty, AdminMainEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "SystemPropertyId")
    @GenericGenerator(
        name="SystemPropertyId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="SystemPropertyImpl"),
            @Parameter(name="entity_name", value="cn.globalph.common.config.domain.SystemPropertyImpl")
        }
    )
    @Column(name = "ID")
    protected Long id;

    @Column(name = "PROPERTY_NAME", nullable = false)
    @AdminPresentation(friendlyName = "SystemPropertyImpl_name", order = 1000, 
        group = "SystemPropertyImpl_general", groupOrder = 1000, prominent = true, gridOrder = 1000)
    protected String name;

    @Column(name= "PROPERTY_VALUE", nullable = false)
    @AdminPresentation(friendlyName = "SystemPropertyImpl_value", order = 3000,
        group = "SystemPropertyImpl_general", groupOrder = 1000, prominent = true, gridOrder = 3000)
    protected String value;

    @Column(name = "PROPERTY_TYPE")
    @AdminPresentation(friendlyName = "SystemPropertyImpl_propertyType", order = 2000, 
        group = "SystemPropertyImpl_general", groupOrder = 1000, prominent = true, gridOrder = 2000,
        fieldType = SupportedFieldType.BROADLEAF_ENUMERATION, 
        broadleafEnumeration = "cn.globalph.common.config.service.type.SystemPropertyFieldType")
    protected String propertyType;

    @Column(name = "FRIENDLY_NAME")
    @AdminPresentation(friendlyName = "SystemPropertyImpl_friendlyName", order = 4000, 
        group = "SystemPropertyImpl_general", groupOrder = 1000)
    protected String friendlyName;

    @Column(name = "FRIENDLY_GROUP")
    @AdminPresentation(friendlyName = "SystemPropertyImpl_friendlyGroup", order = 5000, 
        group = "SystemPropertyImpl_general", groupOrder = 1000)
    protected String friendlyGroup;

    @Column(name = "FRIENDLY_TAB")
    @AdminPresentation(friendlyName = "SystemPropertyImpl_friendlyTab", order = 5000, 
        group = "SystemPropertyImpl_general", groupOrder = 1000)
    protected String friendlyTab;

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
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public String getFriendlyName() {
        return friendlyName;
    }

    @Override
    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @Override
    public String getFriendlyGroup() {
        return friendlyGroup;
    }

    @Override
    public void setFriendlyGroup(String friendlyGroup) {
        this.friendlyGroup = friendlyGroup;
    }
    
    @Override
    public String getFriendlyTab() {
        return friendlyTab;
    }

    @Override
    public void setFriendlyTab(String friendlyTab) {
        this.friendlyTab = friendlyTab;
    }

    public SystemPropertyFieldType getPropertyType() {
        if (propertyType != null) {
            SystemPropertyFieldType returnType = SystemPropertyFieldType.getInstance(propertyType);
            if (returnType != null) {
                return returnType;
            }
        }
        return SystemPropertyFieldType.STRING_TYPE;
    }

    public void setPropertyType(SystemPropertyFieldType propertyType) {
        this.propertyType = propertyType.getType();
    }

    @Override
    public String getMainEntityName() {
        return getName();
    }

}
