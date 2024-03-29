package cn.globalph.b2c.offer.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import cn.globalph.b2c.offer.service.type.OfferDiscountType;
import cn.globalph.b2c.offer.service.type.OfferItemRestrictionRuleType;
import cn.globalph.b2c.offer.service.type.OfferType;
import cn.globalph.common.admin.domain.AdminMainEntity;
import cn.globalph.common.extensibility.jpa.clone.ClonePolicyCollection;
import cn.globalph.common.extensibility.jpa.clone.ClonePolicyMap;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.i18n.service.DynamicTranslationProvider;
import cn.globalph.common.money.Money;
import cn.globalph.common.persistence.ArchiveStatus;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.AdminPresentationMapField;
import cn.globalph.common.presentation.AdminPresentationMapFields;
import cn.globalph.common.presentation.PopulateToOneFieldsEnum;
import cn.globalph.common.presentation.RequiredOverride;
import cn.globalph.common.presentation.RuleIdentifier;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.common.presentation.client.VisibilityEnum;
import cn.globalph.common.util.DateUtil;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.SQLDelete;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "NPH_OFFER")
@Inheritance(strategy=InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blOffers")
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "OfferImpl_baseOffer")
@SQLDelete(sql="UPDATE NPH_OFFER SET ARCHIVED = 'Y' WHERE OFFER_ID = ?")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps=true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX_PRECLONE_INFORMATION),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_CATALOG),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_SITE)
})
public class OfferImpl implements Offer, AdminMainEntity {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator= "OfferId")
    @GenericGenerator(
        name="OfferId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="OfferImpl"),
            @Parameter(name="entity_name", value="cn.globalph.b2c.offer.domain.OfferImpl")
        }
    )
    @Column(name = "OFFER_ID")
    @AdminPresentation(friendlyName = "OfferImpl_Offer_Id", visibility = VisibilityEnum.HIDDEN_ALL)
    protected Long id;

    @Column(name = "OFFER_NAME", nullable=false)
    @Index(name="OFFER_NAME_INDEX", columnNames={"OFFER_NAME"})
    @AdminPresentation(friendlyName = "OfferImpl_Offer_Name", order = 1000, 
        group = Presentation.Group.Name.Description, groupOrder = Presentation.Group.Order.Description,
        prominent = true, gridOrder = 1)
    protected String name;

    @Column(name = "OFFER_DESCRIPTION")
    @AdminPresentation(friendlyName = "OfferImpl_Offer_Description", order = 2000, 
        group = Presentation.Group.Name.Description, groupOrder = Presentation.Group.Order.Description,
        prominent = true, gridOrder = 2,
        largeEntry = true)
    protected String description;

    @Column(name = "MARKETING_MESSASGE")
    @Index(name = "OFFER_MARKETING_MESSAGE_INDEX", columnNames = { "MARKETING_MESSASGE" })
    @AdminPresentation(friendlyName = "OfferImpl_marketingMessage", order = 6000,
            group = Presentation.Group.Name.Description, groupOrder = Presentation.Group.Order.Description,
            translatable = true)
    protected String marketingMessage;

    @Column(name = "OFFER_TYPE", nullable=false)
    @Index(name="OFFER_TYPE_INDEX", columnNames={"OFFER_TYPE"})
    @AdminPresentation(friendlyName = "OfferImpl_Offer_Type", order = 3000, 
        group = Presentation.Group.Name.Description, groupOrder = Presentation.Group.Order.Description,
        prominent =  true, gridOrder = 3,
        fieldType=SupportedFieldType.BROADLEAF_ENUMERATION, 
        broadleafEnumeration="cn.globalph.b2c.offer.service.type.OfferType")
    protected String type;

    @Column(name = "OFFER_DISCOUNT_TYPE")
    @Index(name="OFFER_DISCOUNT_INDEX", columnNames={"OFFER_DISCOUNT_TYPE"})
    @AdminPresentation(friendlyName = "OfferImpl_Offer_Discount_Type", order = 1000, 
        group = Presentation.Group.Name.Amount, groupOrder = Presentation.Group.Order.Amount,
        requiredOverride = RequiredOverride.REQUIRED,
        fieldType=SupportedFieldType.BROADLEAF_ENUMERATION,
        broadleafEnumeration="cn.globalph.b2c.offer.service.type.OfferDiscountType")
    protected String discountType;

    @Column(name = "OFFER_VALUE", nullable=false, precision=19, scale=5)
    @AdminPresentation(friendlyName = "OfferImpl_Offer_Value", order = 2000, 
        group = Presentation.Group.Name.Amount, groupOrder = Presentation.Group.Order.Amount,
        prominent = true, gridOrder = 4)
    protected BigDecimal value;

    @Column(name = "OFFER_PRIORITY")
    @AdminPresentation(friendlyName = "OfferImpl_Offer_Priority", order = 7, 
        tab = Presentation.Tab.Name.Advanced, tabOrder = Presentation.Tab.Order.Advanced,
        group = Presentation.Group.Name.Advanced, groupOrder = Presentation.Group.Order.Advanced)
    protected Integer priority;

    @Column(name = "START_DATE")
    @AdminPresentation(friendlyName = "OfferImpl_Offer_Start_Date", order = 1,
        group = Presentation.Group.Name.ActivityRange, groupOrder = Presentation.Group.Order.ActivityRange)
    protected Date startDate;

    @Column(name = "END_DATE")
    @AdminPresentation(friendlyName = "OfferImpl_Offer_End_Date", order = 2,
        group = Presentation.Group.Name.ActivityRange, groupOrder = Presentation.Group.Order.ActivityRange)
    protected Date endDate;

    @Column(name = "TARGET_SYSTEM")
    @AdminPresentation(friendlyName = "OfferImpl_Offer_Target_System",
        tab = Presentation.Tab.Name.Advanced, tabOrder = Presentation.Tab.Order.Advanced,
        group = Presentation.Group.Name.Advanced, groupOrder = Presentation.Group.Order.Advanced)
    protected String targetSystem;

    @Column(name = "APPLY_TO_SALE_PRICE")
    @AdminPresentation(friendlyName = "OfferImpl_Apply_To_Sale_Price",
            tab = Presentation.Tab.Name.Advanced, tabOrder = Presentation.Tab.Order.Advanced,
            group = Presentation.Group.Name.Advanced, groupOrder = Presentation.Group.Order.Advanced)
    protected Boolean applyToSalePrice = false;
    
    /**
     * No offers can be applied on top of this offer; 
     * If false, stackable has to be false also
     */
    @Column(name = "COMBINABLE_WITH_OTHER_OFFERS")
    @AdminPresentation(friendlyName = "OfferImpl_Offer_Combinable",
        tab = Presentation.Tab.Name.Advanced, tabOrder = Presentation.Tab.Order.Advanced,
        group = Presentation.Group.Name.Advanced, groupOrder = Presentation.Group.Order.Advanced)
    protected Boolean combinableWithOtherOffers = true;

    @Column(name = "AUTOMATICALLY_ADDED")
    @AdminPresentation(friendlyName = "OfferImpl_Offer_Automatically_Added", order = 5000,
            group = Presentation.Group.Name.Description, groupOrder = Presentation.Group.Order.Description,
            fieldType = SupportedFieldType.BOOLEAN)
    protected Boolean automaticallyApplied = false;

    @Column(name = "MAX_USES")
    @AdminPresentation(friendlyName = "OfferImpl_Offer_Max_Uses_Per_Order", order = 7,
        tab = Presentation.Tab.Name.Advanced, tabOrder = Presentation.Tab.Order.Advanced,
        group = Presentation.Group.Name.Advanced, groupOrder = Presentation.Group.Order.Advanced)
    protected Integer maxUsesPerOrder;

    @Column(name = "MAX_USES_PER_CUSTOMER")
    @AdminPresentation(friendlyName = "OfferImpl_Max_Uses_Per_Customer", order = 8,
        tab = Presentation.Tab.Name.Advanced, tabOrder = Presentation.Tab.Order.Advanced,
        group = Presentation.Group.Name.Advanced, groupOrder = Presentation.Group.Order.Advanced)
    protected Long maxUsesPerCustomer;
    
    @Column(name = "OFFER_ITEM_QUALIFIER_RULE")
    @AdminPresentation(friendlyName = "OfferImpl_Item_Qualifier_Rule", 
        tab = Presentation.Tab.Name.Advanced, tabOrder = Presentation.Tab.Order.Advanced,
        group = Presentation.Group.Name.Advanced, groupOrder = Presentation.Group.Order.Advanced,
        fieldType = SupportedFieldType.BROADLEAF_ENUMERATION, 
        broadleafEnumeration = "cn.globalph.b2c.offer.service.type.OfferItemRestrictionRuleType")
    protected String offerItemQualifierRuleType;
    
    @Column(name = "OFFER_ITEM_TARGET_RULE")
    @AdminPresentation(friendlyName = "OfferImpl_Item_Target_Rule", 
        tab = Presentation.Tab.Name.Advanced, tabOrder = Presentation.Tab.Order.Advanced,
        group = Presentation.Group.Name.Advanced, groupOrder = Presentation.Group.Order.Advanced,
        fieldType = SupportedFieldType.BROADLEAF_ENUMERATION, 
        broadleafEnumeration = "cn.globalph.b2c.offer.service.type.OfferItemRestrictionRuleType")
    protected String offerItemTargetRuleType;
    
    @OneToMany(fetch = FetchType.LAZY, targetEntity = OfferItemCriteriaImpl.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "NPH_QUAL_CRIT_OFFER_XREF", joinColumns = @JoinColumn(name = "OFFER_ID"), 
        inverseJoinColumns = @JoinColumn(name = "OFFER_ITEM_CRITERIA_ID"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blOffers")
    @AdminPresentation(friendlyName = "OfferImpl_Qualifying_Item_Rule",
        group = Presentation.Group.Name.Qualifiers, groupOrder = Presentation.Group.Order.Qualifiers,
        fieldType = SupportedFieldType.RULE_WITH_QUANTITY, ruleIdentifier = RuleIdentifier.ORDERITEM)
    @ClonePolicyCollection
    protected Set<OfferItemCriteria> qualifyingItemCriteria = new HashSet<OfferItemCriteria>();
    
    @OneToMany(fetch = FetchType.LAZY, targetEntity = OfferItemCriteriaImpl.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "NPH_TAR_CRIT_OFFER_XREF", joinColumns = @JoinColumn(name = "OFFER_ID"), 
        inverseJoinColumns = @JoinColumn(name = "OFFER_ITEM_CRITERIA_ID"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blOffers")
    @AdminPresentation(friendlyName = "OfferImpl_Target_Item_Rule",
        group = Presentation.Group.Name.ItemTarget, groupOrder = Presentation.Group.Order.ItemTarget,
        fieldType = SupportedFieldType.RULE_WITH_QUANTITY, 
        ruleIdentifier = RuleIdentifier.ORDERITEM)
    @ClonePolicyCollection
    protected Set<OfferItemCriteria> targetItemCriteria = new HashSet<OfferItemCriteria>();
    
    @Column(name = "TOTALITARIAN_OFFER")
    @AdminPresentation(friendlyName = "OfferImpl_Totalitarian_Offer",
        tab = Presentation.Tab.Name.Advanced, tabOrder = Presentation.Tab.Order.Advanced,
        group = Presentation.Group.Name.Advanced, groupOrder = Presentation.Group.Order.Advanced,
        visibility = VisibilityEnum.HIDDEN_ALL)
    protected Boolean totalitarianOffer = false;

    @Column(name = "REQUIRES_RELATED_TAR_QUAL")
    @AdminPresentation(friendlyName = "OfferImpl_Requires_Related_Target_And_Qualifiers", 
        tab = Presentation.Tab.Name.Advanced, tabOrder = Presentation.Tab.Order.Advanced,
        group = Presentation.Group.Name.Advanced, groupOrder = Presentation.Group.Order.Advanced,
        visibility = VisibilityEnum.VISIBLE_ALL)
    protected Boolean requiresRelatedTargetAndQualifiers = false;
    
    @ManyToMany(targetEntity = OfferRuleImpl.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "NPH_OFFER_RULE_MAP",
    		joinColumns = @JoinColumn(name="OFFER_ID", referencedColumnName="OFFER_ID"),
            inverseJoinColumns = @JoinColumn(name = "OFFER_RULE_ID", referencedColumnName = "OFFER_RULE_ID"))
    @MapKeyColumn(name = "MAP_KEY", nullable = false)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blOffers")
    @AdminPresentationMapFields(
        mapDisplayFields = {
            @AdminPresentationMapField(
                fieldName = RuleIdentifier.CUSTOMER_FIELD_KEY,
                fieldPresentation = @AdminPresentation(fieldType = SupportedFieldType.RULE_SIMPLE, 
                    group = Presentation.Group.Name.Qualifiers, groupOrder = Presentation.Group.Order.Qualifiers,
                    ruleIdentifier = RuleIdentifier.CUSTOMER, friendlyName = "OfferImpl_Customer_Rule")
            ),
            @AdminPresentationMapField(
            fieldName = RuleIdentifier.TIME_FIELD_KEY,
                    fieldPresentation = @AdminPresentation(fieldType = SupportedFieldType.RULE_SIMPLE,
                            group = Presentation.Group.Name.ActivityRange, groupOrder = Presentation.Group.Order.ActivityRange,
                            ruleIdentifier = RuleIdentifier.TIME, friendlyName = "OfferImpl_Time_Rule")
            ),
            @AdminPresentationMapField(
                fieldName = RuleIdentifier.ORDER_FIELD_KEY,
                fieldPresentation = @AdminPresentation(fieldType = SupportedFieldType.RULE_SIMPLE, 
                    group = Presentation.Group.Name.Qualifiers, groupOrder = Presentation.Group.Order.Qualifiers,
                    ruleIdentifier = RuleIdentifier.ORDER, friendlyName = "OfferImpl_Order_Rule")
            ),
            @AdminPresentationMapField(
                fieldName = RuleIdentifier.FULFILLMENT_GROUP_FIELD_KEY,
                fieldPresentation = @AdminPresentation(fieldType = SupportedFieldType.RULE_SIMPLE, 
                    group = Presentation.Group.Name.Qualifiers, groupOrder = Presentation.Group.Order.Qualifiers,
                                    ruleIdentifier = RuleIdentifier.FULFILLMENTGROUP, friendlyName = "OfferImpl_FG_Rule")
            )
        }
    )
    @ClonePolicyMap
    Map<String, OfferRule> offerMatchRules = new HashMap<String, OfferRule>();
    
    @Column(name = "USE_NEW_FORMAT")
    @AdminPresentation(friendlyName = "OfferImpl_Treat_As_New_Format",
        tab = Presentation.Tab.Name.Advanced, tabOrder = Presentation.Tab.Order.Advanced,
        group = Presentation.Group.Name.Advanced, groupOrder = Presentation.Group.Order.Advanced,
        visibility = VisibilityEnum.HIDDEN_ALL)
    protected Boolean treatAsNewFormat = false;
    
    @Column(name = "QUALIFYING_ITEM_MIN_TOTAL", precision=19, scale=5)
    @AdminPresentation(friendlyName="OfferImpl_Qualifying_Item_Subtotal",
        tab = Presentation.Tab.Name.Advanced, tabOrder = Presentation.Tab.Order.Advanced,
        group = Presentation.Group.Name.Advanced, groupOrder = Presentation.Group.Order.Advanced)
    protected BigDecimal qualifyingItemSubTotal;
    
    @Embedded
    protected ArchiveStatus archiveStatus = new ArchiveStatus();

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
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public OfferType getType() {
        return OfferType.getInstance(type);
    }

    @Override
    public void setType(OfferType type) {
        this.type = type.getType();
    }

    @Override
    public OfferDiscountType getDiscountType() {
        return OfferDiscountType.getInstance(discountType);
    }

    @Override
    public void setDiscountType(OfferDiscountType discountType) {
        this.discountType = discountType.getType();
    }
    
    @Override
    public OfferItemRestrictionRuleType getOfferItemQualifierRuleType() {
        OfferItemRestrictionRuleType returnType = OfferItemRestrictionRuleType.getInstance(offerItemQualifierRuleType);
        if (returnType == null) {
            return OfferItemRestrictionRuleType.NONE;
        } else {
            return returnType;
        }
    }

    @Override
    public void setOfferItemQualifierRuleType(OfferItemRestrictionRuleType restrictionRuleType) {
        this.offerItemQualifierRuleType = restrictionRuleType.getType();
    }
    
    @Override
    public OfferItemRestrictionRuleType getOfferItemTargetRuleType() {
        OfferItemRestrictionRuleType returnType = OfferItemRestrictionRuleType.getInstance(offerItemTargetRuleType);
        if (returnType == null) {
            return OfferItemRestrictionRuleType.NONE;
        } else {
            return returnType;
        }
    }

    @Override
    public void setOfferItemTargetRuleType(OfferItemRestrictionRuleType restrictionRuleType) {
        this.offerItemTargetRuleType = restrictionRuleType.getType();
    }

    @Override
    public BigDecimal getValue() {
        return value;
    }

    @Override
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public int getPriority() {
        // Treat null as the maximum value minus one to allow for someone to create a
        // priority that is even less than an unset priority.
        return priority == null ? Integer.MAX_VALUE - 1 : priority;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public Date getStartDate() {
        if ('Y'==getArchived()) {
            return null;
        }
        return startDate;
    }

    @Override
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public Date getEndDate() {
        return endDate;
    }

    @Override
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    @Override
    public String getTargetSystem() {
        return targetSystem;
    }

    @Override
    public void setTargetSystem(String targetSystem) {
        this.targetSystem = targetSystem;
    }

    @Override
    public boolean getApplyDiscountToSalePrice() {
        return applyToSalePrice == null ? false : applyToSalePrice;
    }

    @Override
    public void setApplyDiscountToSalePrice(boolean applyToSalePrice) {
        this.applyToSalePrice=applyToSalePrice;
    }

    /**
     * Returns true if this offer can be combined with other offers in the order.
     *
     * @return true if combinableWithOtherOffers, otherwise false
     */
    @Override
    public boolean isCombinableWithOtherOffers() {
        return combinableWithOtherOffers == null ? false : combinableWithOtherOffers;
    }

    /**
     * Sets the combinableWithOtherOffers value for this offer.
     *
     * @param combinableWithOtherOffers
     */
    @Override
    public void setCombinableWithOtherOffers(boolean combinableWithOtherOffers) {
        this.combinableWithOtherOffers = combinableWithOtherOffers;
    }

    @Deprecated
    @JsonIgnore
    public boolean getCombinableWithOtherOffers() {
        return combinableWithOtherOffers;
    }

    public boolean isAutomaticallyApplied() {
		return automaticallyApplied;
	}

	public void setAutomaticallyApplied(boolean automaticallyApplied) {
		this.automaticallyApplied = automaticallyApplied;
	}

	@Override
    public Long getMaxUsesPerCustomer() {
        return maxUsesPerCustomer == null ? 0 : maxUsesPerCustomer;
    }

    @Override
    public void setMaxUsesPerCustomer(Long maxUsesPerCustomer) {
        this.maxUsesPerCustomer = maxUsesPerCustomer;
    }
    
    @Override
    public boolean isUnlimitedUsePerCustomer() {
        return getMaxUsesPerCustomer() == 0;
    }
    
    @Override
    public boolean isLimitedUsePerCustomer() {
        return getMaxUsesPerCustomer() > 0;
    }

    @Override
    public int getMaxUsesPerOrder() {
        return maxUsesPerOrder == null ? 0 : maxUsesPerOrder;
    }

    @Override
    public void setMaxUsesPerOrder(int maxUsesPerOrder) {
        this.maxUsesPerOrder = maxUsesPerOrder;
    }
    
    @Override
    public boolean isUnlimitedUsePerOrder() {
        return getMaxUsesPerOrder() == 0;
    }
    
    @Override
    public boolean isLimitedUsePerOrder() {
        return getMaxUsesPerOrder() > 0;
    }

    @Override
    @Deprecated
    public int getMaxUses() {
        return getMaxUsesPerOrder();
    }

    @Override
    public void setMaxUses(int maxUses) {
        setMaxUsesPerOrder(maxUses);
    }

    @Override
    public String getMarketingMessage() {
        return DynamicTranslationProvider.getValue(this, "marketingMessage", marketingMessage);
    }

    @Override
    public void setMarketingMessage(String marketingMessage) {
        this.marketingMessage = marketingMessage;
    }

    @Override
    public Set<OfferItemCriteria> getQualifyingItemCriteria() {
        return qualifyingItemCriteria;
    }

    @Override
    public void setQualifyingItemCriteria(Set<OfferItemCriteria> qualifyingItemCriteria) {
        this.qualifyingItemCriteria = qualifyingItemCriteria;
    }

    @Override
    public Set<OfferItemCriteria> getTargetItemCriteria() {
        return targetItemCriteria;
    }

    @Override
    public void setTargetItemCriteria(Set<OfferItemCriteria> targetItemCriteria) {
        this.targetItemCriteria = targetItemCriteria;
    }

    @Override
    public Boolean isTotalitarianOffer() {
        if (totalitarianOffer == null) {
            return false;
        } else {
            return totalitarianOffer.booleanValue();
        }
    }

    @Override
    public void setTotalitarianOffer(Boolean totalitarianOffer) {
        if (totalitarianOffer == null) {
            this.totalitarianOffer = false;
        } else {
            this.totalitarianOffer = totalitarianOffer;
        }
    }

    @Override
    public Map<String, OfferRule> getOfferMatchRules() {
        if (offerMatchRules == null) {
            offerMatchRules = new HashMap<String, OfferRule>();
        }
        return offerMatchRules;
    }

    @Override
    public void setOfferMatchRules(Map<String, OfferRule> offerMatchRules) {
        this.offerMatchRules = offerMatchRules;
    }

    @Override
    public Boolean getTreatAsNewFormat() {
        return treatAsNewFormat;
    }

    @Override
    public void setTreatAsNewFormat(Boolean treatAsNewFormat) {
        this.treatAsNewFormat = treatAsNewFormat;
    }

    @Override
    public Character getArchived() {
        if (archiveStatus == null) {
            archiveStatus = new ArchiveStatus();
        }
        return archiveStatus.getArchived();
    }

    @Override
    public void setArchived(Character archived) {
        if (archiveStatus == null) {
            archiveStatus = new ArchiveStatus();
        }
        archiveStatus.setArchived(archived);
    }

    @Override
    public boolean isActive() {
        return DateUtil.isActive(startDate, endDate, true) && 'Y'!=getArchived();
    }
    
    @Override
    public Money getQualifyingItemSubTotal() {
        return qualifyingItemSubTotal == null ? null : new Money(qualifyingItemSubTotal);
    }

    @Override
    public void setQualifyingItemSubTotal(Money qualifyingItemSubTotal) {
        this.qualifyingItemSubTotal = Money.toAmount(qualifyingItemSubTotal);
    }

    @Override
    public Boolean getRequiresRelatedTargetAndQualifiers() {
        return requiresRelatedTargetAndQualifiers == null ? false : requiresRelatedTargetAndQualifiers;
    }
    
    @Override
    public void setRequiresRelatedTargetAndQualifiers(Boolean requiresRelatedTargetAndQualifiers) {
        this.requiresRelatedTargetAndQualifiers = requiresRelatedTargetAndQualifiers;
    }

    @Override
    public String getMainEntityName() {
        return getName();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(name)
            .append(startDate)
            .append(type)
            .append(value)
            .build();
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof OfferImpl) {
            OfferImpl that = (OfferImpl) o;
            return new EqualsBuilder()
                .append(this.id, that.id)
                .append(this.name, that.name)
                .append(this.startDate, that.startDate)
                .append(this.type, that.type)
                .append(this.value, that.value)
                .build();
        }
        
        return false;
    }

    public static class Presentation {
        public static class Tab {
            public static class Name {

                public static final String Codes = "OfferImpl_Codes_Tab";
                public static final String Advanced = "OfferImpl_Advanced_Tab";
            }
            
            public static class Order {

                public static final int Codes = 1000;
                public static final int Advanced = 2000;
            }
        }
            
        public static class Group {
            public static class Name {
                public static final String Description = "OfferImpl_Description";
                public static final String Amount = "OfferImpl_Amount";
                public static final String ActivityRange = "OfferImpl_Activity_Range";
                public static final String Qualifiers = "OfferImpl_Qualifiers";
                public static final String ItemTarget = "OfferImpl_Item_Target";
                public static final String Advanced = "OfferImpl_Advanced";
            }
            
            public static class Order {
                public static final int Description = 1000;
                public static final int Amount = 2000;
                public static final int ActivityRange = 3000;
                public static final int Qualifiers = 4000;
                public static final int ItemTarget = 5000;
                public static final int Advanced = 1000;
            }
        }
    }

}
