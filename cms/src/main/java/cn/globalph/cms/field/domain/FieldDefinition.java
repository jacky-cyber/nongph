package cn.globalph.cms.field.domain;

import cn.globalph.common.enumeration.domain.DataDrivenEnumeration;
import cn.globalph.common.presentation.client.SupportedFieldType;

import java.io.Serializable;

public interface FieldDefinition extends Serializable {

    public Long getId();

    public void setId(Long id);

    public String getName();

    public void setName(String name);

    public SupportedFieldType getFieldType();

    public void setFieldType(SupportedFieldType fieldType);

    public String getSecurityLevel();

    public void setSecurityLevel(String securityLevel);

    public Boolean getHiddenFlag();

    public void setHiddenFlag(Boolean hiddenFlag);

    public String getValidationRegEx();

    public void setValidationRegEx(String validationRegEx);

    public Integer getMaxLength();

    public void setMaxLength(Integer maxLength);

    public String getColumnWidth();

    public void setColumnWidth(String columnWidth);

    public Boolean getTextAreaFlag();

    public void setTextAreaFlag(Boolean textAreaFlag);
    
    public Boolean getRequiredFlag();

    public void setRequiredFlag(Boolean requiredFlag);

    public DataDrivenEnumeration getDataDrivenEnumeration();
    
    public void setDataDrivenEnumeration(DataDrivenEnumeration dataDrivenEnumeration);

    public Boolean getAllowMultiples();

    public void setAllowMultiples(Boolean allowMultiples);

    public String getFriendlyName();

    public void setFriendlyName(String friendlyName);

    public String getValidationErrorMesageKey();

    public void setValidationErrorMesageKey(String validationErrorMesageKey);

    public FieldGroup getFieldGroup();

    public void setFieldGroup(FieldGroup fieldGroup);

    public int getFieldOrder();

    public void setFieldOrder(int fieldOrder);

    public String getAdditionalForeignKeyClass();

    public void setAdditionalForeignKeyClass(String className);

}
