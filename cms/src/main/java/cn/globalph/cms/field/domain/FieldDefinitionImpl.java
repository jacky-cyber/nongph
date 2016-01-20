package cn.globalph.cms.field.domain;

import cn.globalph.common.enumeration.domain.DataDrivenEnumeration;
import cn.globalph.common.enumeration.domain.DataDrivenEnumerationImpl;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "CMS_FLD_DEFINE")
@Cache(usage= CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="blCMSElements")
public class FieldDefinitionImpl implements FieldDefinition {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "FieldDefinitionId")
    @GenericGenerator(
        name="FieldDefinitionId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="FieldDefinitionImpl"),
            @Parameter(name="entity_name", value="cn.globalph.cms.field.domain.FieldDefinitionImpl")
        }
    )
    @Column(name = "FLD_DEF_ID")
    protected Long id;

    @Column (name = "NAME")
    protected String name;

    @Column (name = "FRIENDLY_NAME")
    protected String friendlyName;

    @Column (name = "FLD_TYPE")
    protected String fieldType;

    @Column (name = "SECURITY_LEVEL")
    protected String securityLevel;

    @Column (name = "HIDDEN_FLAG")
    protected Boolean hiddenFlag = false;

    @Column (name = "VLDTN_REGEX")
    protected String validationRegEx;

    @Column (name = "VLDTN_ERROR_MSSG_KEY")
    protected String validationErrorMesageKey;

    @Column (name = "MAX_LENGTH")
    protected Integer maxLength;

    @Column (name = "COLUMN_WIDTH")
    protected String columnWidth;

    @Column (name = "TEXT_AREA_FLAG")
    protected Boolean textAreaFlag = false;
    
    @Column(name = "REQUIRED_FLAG")
    protected Boolean requiredFlag = false;

    @ManyToOne (targetEntity = DataDrivenEnumerationImpl.class)
    @JoinColumn(name = "ENUM_ID")
    protected DataDrivenEnumeration dataDrivenEnumeration;

    @Column (name = "ALLOW_MULTIPLES")
    protected Boolean allowMultiples = false;

    @ManyToOne(targetEntity = FieldGroupImpl.class)
    @JoinColumn(name = "FLD_GROUP_ID")
    protected FieldGroup fieldGroup;

    @Column(name="FLD_ORDER")
    protected int fieldOrder;

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
    public SupportedFieldType getFieldType() {
        if (fieldType == null) {
            return null;
        }
        
        if (fieldType.startsWith(SupportedFieldType.ADDITIONAL_FOREIGN_KEY.toString() + '|')) {
            return SupportedFieldType.ADDITIONAL_FOREIGN_KEY;
        }
        
        return SupportedFieldType.valueOf(fieldType);
    }
    
    @Override
    public String getAdditionalForeignKeyClass() {
        if (fieldType == null || !fieldType.startsWith(SupportedFieldType.ADDITIONAL_FOREIGN_KEY.toString() + '|')) {
            return null;
        }
        
        return fieldType.substring(fieldType.indexOf('|') + 1);
    }
    
    @Override
    public void setAdditionalForeignKeyClass(String className) {
        if (fieldType == null || !fieldType.startsWith(SupportedFieldType.ADDITIONAL_FOREIGN_KEY.toString() + '|')) {
            throw new IllegalArgumentException("Cannot set an additional foreign key class when the field type is not ADDITIONAL_FOREIGN_KEY");
        }
        
        this.fieldType = SupportedFieldType.ADDITIONAL_FOREIGN_KEY.toString() + '|' + className;
    }
    

    @Override
    public void setFieldType(SupportedFieldType fieldType) {
        this.fieldType = fieldType!=null?fieldType.toString():null;
    }

    @Override
    public String getSecurityLevel() {
        return securityLevel;
    }

    @Override
    public void setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
    }

    @Override
    public Boolean getHiddenFlag() {
        return hiddenFlag;
    }

    @Override
    public void setHiddenFlag(Boolean hiddenFlag) {
        this.hiddenFlag = hiddenFlag;
    }

    @Override
    public String getValidationRegEx() {
        return validationRegEx;
    }

    @Override
    public void setValidationRegEx(String validationRegEx) {
        this.validationRegEx = validationRegEx;
    }

    @Override
    public Integer getMaxLength() {
        return maxLength;
    }

    @Override
    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public String getColumnWidth() {
        return columnWidth;
    }

    @Override
    public void setColumnWidth(String columnWidth) {
        this.columnWidth = columnWidth;
    }

    @Override
    public Boolean getTextAreaFlag() {
        return textAreaFlag;
    }

    @Override
    public void setTextAreaFlag(Boolean textAreaFlag) {
        this.textAreaFlag = textAreaFlag;
    }
    
    @Override
    public Boolean getRequiredFlag() {
        return requiredFlag;
    }

    @Override
    public void setRequiredFlag(Boolean requiredFlag) {
        this.requiredFlag = requiredFlag;
    }

    @Override
    public Boolean getAllowMultiples() {
        return allowMultiples;
    }

    @Override
    public void setAllowMultiples(Boolean allowMultiples) {
        this.allowMultiples = allowMultiples;
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
    public String getValidationErrorMesageKey() {
        return validationErrorMesageKey;
    }

    @Override
    public void setValidationErrorMesageKey(String validationErrorMesageKey) {
        this.validationErrorMesageKey = validationErrorMesageKey;
    }

    @Override
    public FieldGroup getFieldGroup() {
        return fieldGroup;
    }

    @Override
    public void setFieldGroup(FieldGroup fieldGroup) {
        this.fieldGroup = fieldGroup;
    }

    @Override
    public int getFieldOrder() {
        return fieldOrder;
    }

    @Override
    public void setFieldOrder(int fieldOrder) {
        this.fieldOrder = fieldOrder;
    }

    @Override
    public DataDrivenEnumeration getDataDrivenEnumeration() {
        return dataDrivenEnumeration;
    }

    @Override
    public void setDataDrivenEnumeration(DataDrivenEnumeration dataDrivenEnumeration) {
        this.dataDrivenEnumeration = dataDrivenEnumeration;
    }
}

