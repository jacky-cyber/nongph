package cn.globalph.cms.structure.domain;

import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.openadmin.audit.AdminAuditable;
import cn.globalph.openadmin.audit.AdminAuditableListener;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "CMS_SC_FLD")
@EntityListeners(value = { AdminAuditableListener.class })
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps=true)
})
public class StructuredContentFieldImpl implements StructuredContentField {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "StructuredContentFieldId")
    @GenericGenerator(
        name="StructuredContentFieldId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="StructuredContentFieldImpl"),
            @Parameter(name="entity_name", value="cn.globalph.cms.structure.domain.StructuredContentFieldImpl")
        }
    )
    @Column(name = "SC_FLD_ID")
    protected Long id;

    @Embedded
    @AdminPresentation(excluded = true)
    protected AdminAuditable auditable = new AdminAuditable();

    @Column (name = "FLD_KEY")
    protected String fieldKey;

    @Column (name = "VALUE")
    protected String stringValue;

    @Column (name = "LOB_VALUE", length = Integer.MAX_VALUE - 1)
    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    protected String lobValue;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getFieldKey() {
        return fieldKey;
    }

    @Override
    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }

    @Override
    public String getValue() {
        if (stringValue != null && stringValue.length() > 0) {
            return stringValue;
        } else {
            return lobValue;
        }
    }

    @Override
    public void setValue(String value) {
        if (value != null) {
            if (value.length() <= 256) {
                stringValue = value;
                lobValue = null;
            } else {
                stringValue = null;
                lobValue = value;
            }
        } else {
            lobValue = null;
            stringValue = null;
        }
    }

    @Override
    public AdminAuditable getAuditable() {
        return auditable;
    }

    @Override
    public void setAuditable(AdminAuditable auditable) {
        this.auditable = auditable;
    }
    
    @Override
    public StructuredContentField clone() {
        StructuredContentField clone = null;

        try {
            clone = (StructuredContentField) Class.forName(this.getClass().getName()).newInstance();
            clone.setFieldKey(getFieldKey());
            clone.setValue(getValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return clone;
    }
        
}