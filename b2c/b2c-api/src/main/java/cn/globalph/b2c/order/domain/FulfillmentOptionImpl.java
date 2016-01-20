package cn.globalph.b2c.order.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import cn.globalph.b2c.order.service.type.FulfillmentType;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.i18n.service.DynamicTranslationProvider;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.override.AdminPresentationMergeEntry;
import cn.globalph.common.presentation.override.AdminPresentationMergeOverride;
import cn.globalph.common.presentation.override.AdminPresentationMergeOverrides;
import cn.globalph.common.presentation.override.PropertyType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "NPH_FULFILLMENT_OPTION")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blOrderElements")
@AdminPresentationMergeOverrides(
    {
        @AdminPresentationMergeOverride(name = "", mergeEntries =
            @AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.READONLY,
                                            booleanOverrideValue = true))
    }
)
@AdminPresentationClass(friendlyName = "Base Fulfillment Option")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps=true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_SITE)
})
public class FulfillmentOptionImpl implements FulfillmentOption {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator= "FulfillmentOptionId")
    @GenericGenerator(
        name="FulfillmentOptionId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="FulfillmentOptionImpl"),
            @Parameter(name="entity_name", value="cn.globalph.b2c.order.domain.FulfillmentOptionImpl")
        }
    )
    @Column(name = "FULFILLMENT_OPTION_ID")
    protected Long id;
    
    @Column(name = "NAME")
    @AdminPresentation(friendlyName = "FulfillmentOptionImpl_name",
            order = Presentation.FieldOrder.NAME, prominent = true, gridOrder = 1000, translatable = true)
    protected String name;

    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    @Column(name = "LONG_DESCRIPTION", length = Integer.MAX_VALUE - 1)
    @AdminPresentation(friendlyName = "FulfillmentOptionImpl_longDescription",
            order = Presentation.FieldOrder.DESCRIPTION, translatable = true)
    protected String longDescription;

    @Column(name = "USE_FLAT_RATES")
    @AdminPresentation(friendlyName = "FulfillmentOptionImpl_useFlatRates",
        order = Presentation.FieldOrder.FLATRATES)
    protected Boolean useFlatRates = true;

    @Column(name = "FULFILLMENT_TYPE", nullable = false)
    protected String fulfillmentType;

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
        return DynamicTranslationProvider.getValue(this, "name", name);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getLongDescription() {
        return DynamicTranslationProvider.getValue(this, "longDescription", longDescription);
    }

    @Override
    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    @Override
    public Boolean getUseFlatRates() {
        return useFlatRates;
    }

    @Override
    public void setUseFlatRates(Boolean useFlatRates) {
        this.useFlatRates = useFlatRates;
    }

    @Override
    public FulfillmentType getFulfillmentType() {
        return FulfillmentType.getInstance(fulfillmentType);
    }

    @Override
    public void setFulfillmentType(FulfillmentType fulfillmentType) {
        this.fulfillmentType = (fulfillmentType == null) ? null : fulfillmentType.getType();
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public static class Presentation {
        public static class Group {
            public static class Name {
            }

            public static class Order {
            }
        }

        public static class FieldOrder {
            public static final int NAME = 1000;
            public static final int DESCRIPTION = 2000;
            public static final int FLATRATES = 9000;
        }
    }
}
