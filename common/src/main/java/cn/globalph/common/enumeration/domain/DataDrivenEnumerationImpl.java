package cn.globalph.common.enumeration.domain;

import cn.globalph.common.extensibility.jpa.clone.ClonePolicyCollection;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.AdminPresentationCollection;
import cn.globalph.common.presentation.PopulateToOneFieldsEnum;
import cn.globalph.common.presentation.client.AddMethodType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="NPH_DATA_DRVN_ENUM")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blStandardElements")
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "DataDrivenEnumerationImpl_friendyName")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps=true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_SITE)
})
public class DataDrivenEnumerationImpl implements DataDrivenEnumeration {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "DataDrivenEnumerationId")
    @GenericGenerator(
        name="DataDrivenEnumerationId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="DataDrivenEnumerationImpl"),
            @Parameter(name="entity_name", value="cn.globalph.common.enumeration.domain.DataDrivenEnumerationImpl")
        }
    )
    @Column(name = "ENUM_ID")
    protected Long id;
    
    @Column(name = "ENUM_KEY")
    @Index(name = "ENUM_KEY_INDEX", columnNames = {"KEY"})
    @AdminPresentation(friendlyName = "DataDrivenEnumerationImpl_Key", order = 1, gridOrder = 1, prominent = true)
    protected String key;
    
    @Column(name = "MODIFIABLE")
    @AdminPresentation(friendlyName = "DataDrivenEnumerationImpl_Modifiable", order = 2, gridOrder = 2, prominent = true)
    protected Boolean modifiable = false;

    @OneToMany(mappedBy = "type", targetEntity = DataDrivenEnumerationValueImpl.class, cascade = {CascadeType.ALL})
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blStandardElements")
    @AdminPresentationCollection(addType = AddMethodType.PERSIST, friendlyName = "DataDrivenEnumerationImpl_Enum_Values", order = 3)
    @ClonePolicyCollection
    protected List<DataDrivenEnumerationValue> enumValues = new ArrayList<DataDrivenEnumerationValue>();
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public Boolean getModifiable() {
        if (modifiable == null) {
            return Boolean.FALSE;
        } else {
            return modifiable;
        }
    }

    @Override
    public void setModifiable(Boolean modifiable) {
        this.modifiable = modifiable;
    }

    @Override
    public List<DataDrivenEnumerationValue> getEnumValues() {
        return enumValues;
    }

    @Override
    public void setEnumValues(List<DataDrivenEnumerationValue> enumValues) {
        this.enumValues = enumValues;
    }

    @Override
    @Deprecated
    public List<DataDrivenEnumerationValue> getOrderItems() {
        return enumValues;
    }

    @Override
    @Deprecated
    public void setOrderItems(List<DataDrivenEnumerationValue> orderItems) {
        this.enumValues = orderItems;
    }
}
