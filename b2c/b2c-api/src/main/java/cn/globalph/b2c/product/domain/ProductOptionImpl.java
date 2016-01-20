package cn.globalph.b2c.product.domain;

import cn.globalph.b2c.product.extension.ProductOptionEntityExtensionManager;
import cn.globalph.common.admin.domain.AdminMainEntity;
import cn.globalph.common.extensibility.jpa.clone.ClonePolicyCollection;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;
import cn.globalph.common.i18n.service.DynamicTranslationProvider;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.AdminPresentationCollection;
import cn.globalph.common.presentation.PopulateToOneFieldsEnum;
import cn.globalph.common.presentation.client.AddMethodType;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.common.web.WebRequestContext;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "NPH_PRODUCT_OPTION")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blStandardElements")
@AdminPresentationClass(friendlyName = "ProductOptionImpl_baseProductOption", populateToOneFields=PopulateToOneFieldsEnum.TRUE)
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps=true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX_PRECLONE_INFORMATION),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_CATALOG)
})
public class ProductOptionImpl implements ProductOption, AdminMainEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator= "ProductOptionId")
    @GenericGenerator(
        name="ProductOptionId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="ProductOptionImpl"),
            @Parameter(name="entity_name", value="cn.globalph.b2c.product.domain.ProductOptionImpl")
        }
    )
    @Column(name = "PRODUCT_OPTION_ID")
    protected Long id;
    
    @Column(name = "OPTION_TYPE")
    @AdminPresentation(friendlyName = "productOption_Type", fieldType = SupportedFieldType.BROADLEAF_ENUMERATION, broadleafEnumeration = "cn.globalph.b2c.product.domain.ProductOptionType")
    protected String type;
    
    @Column(name = "ATTRIBUTE_NAME")
    @AdminPresentation(friendlyName = "productOption_name", helpText = "productOption_nameHelp")
    protected String attributeName;
    
    @Column(name = "LABEL")
    @AdminPresentation(friendlyName = "productOption_Label", helpText = "productOption_labelHelp", 
        prominent = true,
        translatable = true)
    protected String label;

    @Column(name = "REQUIRED")
    @AdminPresentation(friendlyName = "productOption_Required", prominent = true)
    protected Boolean required;

    @Column(name = "USE_IN_SKU_GENERATION")
    @AdminPresentation(friendlyName = "productOption_UseInSKUGeneration")
    private Boolean useInSkuGeneration;

    @Column(name = "DISPLAY_ORDER")
    @AdminPresentation(friendlyName = "productOption_displayOrder")
    protected Integer displayOrder;

    @Column(name = "VALIDATION_STRATEGY_TYPE")
    @AdminPresentation(friendlyName = "productOption_validationStrategyType", group = "productOption_validation", fieldType = SupportedFieldType.BROADLEAF_ENUMERATION, broadleafEnumeration = "cn.globalph.b2c.product.domain.ProductOptionValidationStrategyType")
    private String productOptionValidationStrategyType;

    @Column(name = "VALIDATION_TYPE")
    @AdminPresentation(friendlyName = "productOption_validationType", group = "productOption_validation", fieldType = SupportedFieldType.BROADLEAF_ENUMERATION, broadleafEnumeration = "cn.globalph.b2c.product.domain.ProductOptionValidationType")
    private String productOptionValidationType;

    @Column(name = "VALIDATION_STRING")
    @AdminPresentation(friendlyName = "productOption_validationSring", group = "productOption_validation")
    protected String validationString;

    @Column(name = "ERROR_CODE")
    @AdminPresentation(friendlyName = "productOption_errorCode", group = "productOption_validation")
    protected String errorCode;

    @Column(name = "ERROR_MESSAGE")
    @AdminPresentation(friendlyName = "productOption_errorMessage", group = "productOption_validation", translatable = true)
    protected String errorMessage;

    @OneToMany(mappedBy = "productOption", targetEntity = ProductOptionValueImpl.class, cascade = {CascadeType.ALL})
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blStandardElements")
    @OrderBy(value = "displayOrder")
    @AdminPresentationCollection(addType = AddMethodType.PERSIST, friendlyName = "ProductOptionImpl_Allowed_Values")
    @ClonePolicyCollection
    protected List<ProductOptionValue> allowedValues = new ArrayList<ProductOptionValue>();

    @OneToMany(targetEntity = ProductOptionXrefImpl.class, mappedBy = "productOption")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blStandardElements")
    @BatchSize(size = 50)
    protected List<ProductOptionXref> products = new ArrayList<ProductOptionXref>();
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public ProductOptionType getType() {
        return ProductOptionType.getInstance(type);
    }

    @Override
    public void setType(ProductOptionType type) {
        this.type = type == null ? null : type.getType();
    }
    
    @Override
    public String getAttributeName() {
        return attributeName;
    }

    @Override
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
    
    @Override
    public String getLabel() {
        return DynamicTranslationProvider.getValue(this, "label", label);
    }
    
    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public Boolean getRequired() {
        return required;
    }

    @Override
    public void setRequired(Boolean required) {
        this.required = required;
    }

    @Override
    public Integer getDisplayOrder() {
        return displayOrder;
    }

    @Override
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    @Override
    public List<ProductOptionXref> getProductXrefs() {
        WebRequestContext context = WebRequestContext.getWebRequestContext();
        if (context != null && context.getAdditionalProperties().containsKey("blProductOptionEntityExtensionManager")) {
            ProductOptionEntityExtensionManager extensionManager = (ProductOptionEntityExtensionManager) context.getAdditionalProperties().get("blProductOptionEntityExtensionManager");
            ExtensionResultHolder<List<ProductOptionXref>> holder = new ExtensionResultHolder<List<ProductOptionXref>>();
            ExtensionResultStatusType result = extensionManager.getHandlerProxy().getProductXrefs(this, holder);
            if (ExtensionResultStatusType.HANDLED.equals(result)) {
                return holder.getResult();
            	}
           }

        return products;
    }

    @Override
    public void setProductXrefs(List<ProductOptionXref> xrefs) {
        this.products = xrefs;
    }

    @Override
    public List<Product> getProducts() {
        List<Product> response = new ArrayList<Product>();
        for (ProductOptionXref xref : products) {
            response.add(xref.getProduct());
        }
        return Collections.unmodifiableList(response);
    }

    @Override
    public void setProducts(List<Product> products){
        throw new UnsupportedOperationException("Use setProductOptionXrefs(..) instead");
    }

    @Override
    public List<ProductOptionValue> getAllowedValues() {
        return allowedValues;
    }

    @Override
    public void setAllowedValues(List<ProductOptionValue> allowedValues) {
        this.allowedValues = allowedValues;
    }

    @Override
    public Boolean getUseInSkuGeneration() {
        return (useInSkuGeneration == null) ? true : useInSkuGeneration;
    }

    @Override
    public void setUseInSkuGeneration(Boolean useInSkuGeneration) {
        this.useInSkuGeneration = useInSkuGeneration;
    }

    @Override
    public ProductOptionValidationStrategyType getProductOptionValidationStrategyType() {
        return ProductOptionValidationStrategyType.getInstance(productOptionValidationStrategyType);
    }

    @Override
    public void setProductOptionValidationStrategyType(ProductOptionValidationStrategyType productOptionValidationStrategyType) {
        this.productOptionValidationStrategyType = productOptionValidationStrategyType == null ? null : productOptionValidationStrategyType.getType();
    }

    @Override
    public ProductOptionValidationType getProductOptionValidationType() {
        return ProductOptionValidationType.getInstance(productOptionValidationType);
    }

    @Override
    public void setProductOptionValidationType(ProductOptionValidationType productOptionValidationType) {
        this.productOptionValidationType = productOptionValidationType == null ? null : productOptionValidationType.getType();
    }

    @Override
    public String getValidationString() {
        return validationString;
    }

    @Override
    public void setValidationString(String validationString) {
        this.validationString = validationString;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getErrorMessage() {
        return DynamicTranslationProvider.getValue(this, "errorMessage", errorMessage);
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMainEntityName() {
        return getLabel();
    }

}
