package cn.globalph.cms.structure.domain;

import cn.globalph.common.admin.domain.AdminMainEntity;
import cn.globalph.common.extensibility.jpa.clone.ClonePolicyCollection;
import cn.globalph.common.extensibility.jpa.clone.ClonePolicyMap;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.AdminPresentationMapField;
import cn.globalph.common.presentation.AdminPresentationMapFields;
import cn.globalph.common.presentation.AdminPresentationToOneLookup;
import cn.globalph.common.presentation.PopulateToOneFieldsEnum;
import cn.globalph.common.presentation.RequiredOverride;
import cn.globalph.common.presentation.RuleIdentifier;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.common.presentation.client.VisibilityEnum;
import cn.globalph.common.presentation.override.AdminPresentationOverride;
import cn.globalph.common.presentation.override.AdminPresentationOverrides;
import cn.globalph.openadmin.audit.AdminAuditable;
import cn.globalph.openadmin.audit.AdminAuditableListener;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "CMS_SC")
@EntityListeners(value = { AdminAuditableListener.class })
@AdminPresentationOverrides(
    {
        @AdminPresentationOverride(name = "auditable.createdBy.id", value = @AdminPresentation(readOnly = true, visibility = VisibilityEnum.HIDDEN_ALL)),
        @AdminPresentationOverride(name = "auditable.updatedBy.id", value = @AdminPresentation(readOnly = true, visibility = VisibilityEnum.HIDDEN_ALL)),
        @AdminPresentationOverride(name = "auditable.createdBy.name", value = @AdminPresentation(readOnly = true, visibility = VisibilityEnum.HIDDEN_ALL)),
        @AdminPresentationOverride(name = "auditable.updatedBy.name", value = @AdminPresentation(readOnly = true, visibility = VisibilityEnum.HIDDEN_ALL)),
        @AdminPresentationOverride(name = "auditable.dateCreated", value = @AdminPresentation(readOnly = true, visibility = VisibilityEnum.HIDDEN_ALL)),
        @AdminPresentationOverride(name = "auditable.dateUpdated", value = @AdminPresentation(readOnly = true, visibility = VisibilityEnum.HIDDEN_ALL)),
        @AdminPresentationOverride(name = "structuredContentType.name", value = @AdminPresentation(readOnly = true, visibility = VisibilityEnum.HIDDEN_ALL)),
        @AdminPresentationOverride(name = "structuredContentType.structuredContentFieldTemplate.name", value = @AdminPresentation(readOnly = true, visibility = VisibilityEnum.HIDDEN_ALL))
    }
)
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "StructuredContentImpl_baseStructuredContent")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps=true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_SITE)
})
public class StructuredContentImpl implements StructuredContent, AdminMainEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "StructuredContentId")
    @GenericGenerator(
        name="StructuredContentId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="StructuredContentImpl"),
            @Parameter(name="entity_name", value="cn.globalph.cms.structure.domain.StructuredContentImpl")
        }
    )
    @Column(name = "SC_ID")
    protected Long id;

    @Embedded
    @AdminPresentation(excluded = true)
    protected AdminAuditable auditable = new AdminAuditable();

    @AdminPresentation(friendlyName = "StructuredContentImpl_Content_Name", order = 1, 
        group = Presentation.Group.Name.Description, groupOrder = Presentation.Group.Order.Description,
        prominent = true, gridOrder = 1)
    @Column(name = "CONTENT_NAME", nullable = false)
    @Index(name="CONTENT_NAME_INDEX", columnNames={"CONTENT_NAME", "ARCHIVED_FLAG", "SC_TYPE_ID"})
    protected String contentName;

    @Column(name = "PRIORITY", nullable = false)
    @AdminPresentation(friendlyName = "StructuredContentImpl_Priority", order = 3,
        group = Presentation.Group.Name.Description, groupOrder = Presentation.Group.Order.Description)
    @Index(name="CONTENT_PRIORITY_INDEX", columnNames={"PRIORITY"})
    protected Integer priority;

    @ManyToMany(targetEntity = StructuredContentRuleImpl.class, cascade = {CascadeType.ALL})
    @JoinTable(name = "CMS_SC_RULE_MAP", inverseJoinColumns = @JoinColumn(name = "SC_RULE_ID", referencedColumnName = "SC_RULE_ID"))
    @Cascade(value={org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    @MapKeyColumn(name = "MAP_KEY", nullable = false)
    @AdminPresentationMapFields(
        mapDisplayFields = {
            @AdminPresentationMapField(
                fieldName = RuleIdentifier.CUSTOMER_FIELD_KEY,
                fieldPresentation = @AdminPresentation(fieldType = SupportedFieldType.RULE_SIMPLE, order = 1,
                    tab = Presentation.Tab.Name.Rules, tabOrder = Presentation.Tab.Order.Rules,
                    group = Presentation.Group.Name.Rules, groupOrder = Presentation.Group.Order.Rules,
                    ruleIdentifier = RuleIdentifier.CUSTOMER, friendlyName = "Generic_Customer_Rule")
            ),
            @AdminPresentationMapField(
                fieldName = RuleIdentifier.TIME_FIELD_KEY,
                fieldPresentation = @AdminPresentation(fieldType = SupportedFieldType.RULE_SIMPLE, order = 2,
                    tab = Presentation.Tab.Name.Rules, tabOrder = Presentation.Tab.Order.Rules,
                    group = Presentation.Group.Name.Rules, groupOrder = Presentation.Group.Order.Rules,
                    ruleIdentifier = RuleIdentifier.TIME, friendlyName = "Generic_Time_Rule")
            ),
            @AdminPresentationMapField(
                fieldName = RuleIdentifier.REQUEST_FIELD_KEY,
                fieldPresentation = @AdminPresentation(fieldType = SupportedFieldType.RULE_SIMPLE, order = 3,
                    tab = Presentation.Tab.Name.Rules, tabOrder = Presentation.Tab.Order.Rules,
                    group = Presentation.Group.Name.Rules, groupOrder = Presentation.Group.Order.Rules,
                    ruleIdentifier = RuleIdentifier.REQUEST, friendlyName = "Generic_Request_Rule")
            ),
            @AdminPresentationMapField(
                fieldName = RuleIdentifier.PRODUCT_FIELD_KEY,
                fieldPresentation = @AdminPresentation(fieldType = SupportedFieldType.RULE_SIMPLE, order = 4,
                    tab = Presentation.Tab.Name.Rules, tabOrder = Presentation.Tab.Order.Rules,
                    group = Presentation.Group.Name.Rules, groupOrder = Presentation.Group.Order.Rules,
                    ruleIdentifier = RuleIdentifier.PRODUCT, friendlyName = "Generic_Product_Rule")
                    ),
            @AdminPresentationMapField(
                fieldName = RuleIdentifier.ORDER_FIELD_KEY,
                fieldPresentation = @AdminPresentation(fieldType = SupportedFieldType.RULE_SIMPLE, order = 5,
                    tab = Presentation.Tab.Name.Rules, tabOrder = Presentation.Tab.Order.Rules,
                    group = Presentation.Group.Name.Rules, groupOrder = Presentation.Group.Order.Rules,
                    ruleIdentifier = RuleIdentifier.ORDER, friendlyName = "Generic_Order_Rule")
                    ),
            @AdminPresentationMapField(
                fieldName = RuleIdentifier.CATEGORY,
                fieldPresentation = @AdminPresentation(fieldType = SupportedFieldType.RULE_SIMPLE, order = 6,
                    tab = Presentation.Tab.Name.Rules, tabOrder = Presentation.Tab.Order.Rules,
                    group = Presentation.Group.Name.Rules, groupOrder = Presentation.Group.Order.Rules,
                    ruleIdentifier = RuleIdentifier.CATEGORY, friendlyName = "Generic_Category_Rule")
                    )
        }
    )
    @ClonePolicyMap
    Map<String, StructuredContentRule> structuredContentMatchRules = new HashMap<String, StructuredContentRule>();

    @OneToMany(fetch = FetchType.LAZY, targetEntity = StructuredContentItemCriteriaImpl.class, cascade={CascadeType.ALL})
    @JoinTable(name = "CMS_QUAL_CRIT_SC_XREF", joinColumns = @JoinColumn(name = "SC_ID"), inverseJoinColumns = @JoinColumn(name = "SC_ITEM_CRITERIA_ID"))
    @Cascade(value={org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    @AdminPresentation(friendlyName = "Generic_Item_Rule", order = 5,
        tab = Presentation.Tab.Name.Rules, tabOrder = Presentation.Tab.Order.Rules,
        group = Presentation.Group.Name.Rules, groupOrder = Presentation.Group.Order.Rules,
        fieldType = SupportedFieldType.RULE_WITH_QUANTITY, 
        ruleIdentifier = RuleIdentifier.ORDERITEM)
    @ClonePolicyCollection
    protected Set<StructuredContentItemCriteria> qualifyingItemCriteria = new HashSet<StructuredContentItemCriteria>();

    @ManyToOne(targetEntity = StructuredContentTypeImpl.class)
    @JoinColumn(name="SC_TYPE_ID")
    @AdminPresentation(friendlyName = "StructuredContentImpl_Content_Type", order = 2, prominent = true,
        group = Presentation.Group.Name.Description, groupOrder = Presentation.Group.Order.Description,
        requiredOverride = RequiredOverride.REQUIRED)
    @AdminPresentationToOneLookup(lookupDisplayProperty = "name", forcePopulateChildProperties = true)
    protected StructuredContentType structuredContentType;

    @ManyToMany(targetEntity = StructuredContentFieldImpl.class, cascade = CascadeType.ALL)
    @JoinTable(name = "CMS_SC_FLD_MAP", joinColumns = @JoinColumn(name = "SC_ID", referencedColumnName = "SC_ID"), inverseJoinColumns = @JoinColumn(name = "SC_FLD_ID", referencedColumnName = "SC_FLD_ID"))
    @MapKeyColumn(name = "MAP_KEY")
    @Cascade(value={org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    @BatchSize(size = 20)
    @ClonePolicyMap
    protected Map<String,StructuredContentField> structuredContentFields = new HashMap<String,StructuredContentField>();

    @AdminPresentation(friendlyName = "StructuredContentImpl_Offline", order = 4, 
        group = Presentation.Group.Name.Description, groupOrder = Presentation.Group.Order.Description)
    @Column(name = "OFFLINE_FLAG")
    @Index(name="SC_OFFLN_FLG_INDX", columnNames={"OFFLINE_FLAG"})
    protected Boolean offlineFlag = false;
    
    @Transient
    protected Map<String, String> fieldValuesMap = null;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getContentName() {
        return contentName;
    }

    @Override
    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    @Override
    public StructuredContentType getStructuredContentType() {
        return structuredContentType;
    }

    @Override
    public void setStructuredContentType(StructuredContentType structuredContentType) {
        this.structuredContentType = structuredContentType;
    }

    @Override
    public Map<String, StructuredContentField> getStructuredContentFields() {
        return structuredContentFields;
    }
    
    @Override
    public void setStructuredContentFields(Map<String, StructuredContentField> structuredContentFields) {
        this.structuredContentFields = structuredContentFields;
    }
    
    @Override
    public void setFieldValues(Map<String, String> fieldValuesMap) {
        this.fieldValuesMap = fieldValuesMap;
    }
    
    @Override
    public Map<String, String> getFieldValues() {
        if (fieldValuesMap == null) {
            fieldValuesMap = new HashMap<String, String>();
            for (Entry<String, StructuredContentField> entry : getStructuredContentFields().entrySet()) {
                fieldValuesMap.put(entry.getKey(), entry.getValue().getValue());
            }
        }
        return fieldValuesMap;
    }

    @Override
    public Boolean getOfflineFlag() {
        if (offlineFlag == null) {
            return Boolean.FALSE;
        } else {
            return offlineFlag;
        }
    }

    @Override
    public void setOfflineFlag(Boolean offlineFlag) {
        this.offlineFlag = offlineFlag;
    }

    @Override
    public Integer getPriority() {
        return priority;
    }

    @Override
    public void setPriority(Integer priority) {
        this.priority = priority;
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
    public Map<String, StructuredContentRule> getStructuredContentMatchRules() {
        return structuredContentMatchRules;
    }

    @Override
    public void setStructuredContentMatchRules(Map<String, StructuredContentRule> structuredContentMatchRules) {
        this.structuredContentMatchRules = structuredContentMatchRules;
    }

    @Override
    public Set<StructuredContentItemCriteria> getQualifyingItemCriteria() {
        return qualifyingItemCriteria;
    }

    @Override
    public void setQualifyingItemCriteria(Set<StructuredContentItemCriteria> qualifyingItemCriteria) {
        this.qualifyingItemCriteria = qualifyingItemCriteria;
    }
    
    public String getMainEntityName() {
        return getContentName();
    }
    
    public static class Presentation {
        public static class Tab {
            public static class Name {
                public static final String Rules = "StructuredContentImpl_Rules_Tab";
            }
            
            public static class Order {
                public static final int Rules = 1000;
            }
        }
            
        public static class Group {
            public static class Name {
                public static final String Description = "StructuredContentImpl_Description";
                public static final String Internal = "StructuredContentImpl_Internal";
                public static final String Rules = "StructuredContentImpl_Rules";
            }
            
            public static class Order {
                public static final int Description = 1000;
                public static final int Internal = 2000;
                public static final int Rules = 1000;
            }
        }
    }

}
