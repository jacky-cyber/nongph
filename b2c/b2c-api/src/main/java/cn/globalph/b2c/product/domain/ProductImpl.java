package cn.globalph.b2c.product.domain;

import cn.globalph.common.presentation.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.b2c.catalog.domain.Category;
import cn.globalph.b2c.catalog.domain.CategoryImpl;
import cn.globalph.b2c.catalog.domain.CategoryProductXref;
import cn.globalph.b2c.catalog.domain.CategoryProductXrefImpl;
import cn.globalph.b2c.catalog.domain.CrossSaleProductImpl;
import cn.globalph.b2c.catalog.domain.RelatedProduct;
import cn.globalph.b2c.catalog.domain.UpSaleProductImpl;
import cn.globalph.b2c.product.extension.ProductEntityExtensionManager;
import cn.globalph.common.admin.domain.AdminMainEntity;
import cn.globalph.common.extensibility.jpa.clone.ClonePolicyAdornedTargetCollection;
import cn.globalph.common.extensibility.jpa.clone.ClonePolicyCollection;
import cn.globalph.common.extensibility.jpa.clone.ClonePolicyMap;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;
import cn.globalph.common.media.domain.Media;
import cn.globalph.common.persistence.ArchiveStatus;
import cn.globalph.common.persistence.Status;
import cn.globalph.common.presentation.client.VisibilityEnum;
import cn.globalph.common.web.Locatable;
import cn.globalph.common.web.WebRequestContext;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.*;

/**
 * The Class ProductImpl is the default implementation of {@link Product}. A
 * product is a general description of an item that can be sold (for example: a
 * hat). Products are not sold or added to a cart. {@link Sku}s which are
 * specific items (for example: a XL Blue Hat) are sold or added to a cart. <br>
 * <br>
 * If you want to add fields specific to your implementation 
 * you should extend this class and add your fields. If you
 * need to make significant changes to the ProductImpl then you should implement
 * your own version of {@link Product}. <br>
 * <br>
 * This implementation uses a Hibernate implementation of JPA configured through
 * annotations. The Entity references the following tables: BLC_PRODUCT,
 * BLC_PRODUCT_SKU_XREF, BLC_PRODUCT_IMAGE
 * @author btaylor
 * @see {@link Product}, {@link SkuImpl}, {@link CategoryImpl}
 */
@Entity
@javax.persistence.Table(name="NPH_PRODUCT")
@org.hibernate.annotations.Table(appliesTo = "NPH_PRODUCT", indexes = {
    @Index(name = "PRODUCT_URL_INDEX",
            columnNames = {"URL","URL_KEY"}
    )
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blProducts")
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "baseProduct")
@SQLDelete(sql="UPDATE NPH_PRODUCT SET ARCHIVED = 'Y' WHERE PRODUCT_ID = ?")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps=true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX_PRECLONE_INFORMATION),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_CATALOG)
})
public class ProductImpl implements Product, Status, AdminMainEntity, Locatable {

    private static final Log LOG = LogFactory.getLog(ProductImpl.class);
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The id. */
    @Id
    @GeneratedValue(generator= "ProductId")
    @GenericGenerator(
        name="ProductId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="ProductImpl"),
            @Parameter(name="entity_name", value="cn.globalph.b2c.product.domain.ProductImpl")
        }
    )
    @Column(name = "PRODUCT_ID")
    @AdminPresentation( friendlyName = "ProductImpl_Product_ID", 
                        visibility = VisibilityEnum.HIDDEN_ALL )
    protected Long id;
    
    @Column(name = "PRODUCT_NAME")
    @AdminPresentation(friendlyName = "ProductImpl_Product_Name", 
                       order = ProductImpl.Presentation.FieldOrder.NAME,
                       group = ProductImpl.Presentation.Group.Name.General, 
                       groupOrder = ProductImpl.Presentation.Group.Order.General,
                       prominent = true, 
                       gridOrder = 1, 
                       columnWidth = "260px",
                       translatable = true)
    protected String name;
    
    @Column(name = "URL")
    @AdminPresentation(friendlyName = "ProductImpl_Product_Url", 
                       order = Presentation.FieldOrder.URL,
                       group = Presentation.Group.Name.General, 
                       groupOrder = Presentation.Group.Order.General, 
                       prominent = true, 
                       gridOrder = 3, 
                       columnWidth = "200px",
                       requiredOverride = RequiredOverride.REQUIRED,
                       validationConfigurations = { @ValidationConfiguration(validationImplementation = "blUriPropertyValidator") })
    protected String url;

    @Column(name = "URL_KEY")
    @AdminPresentation(friendlyName = "ProductImpl_Product_UrlKey",
                       tab = Presentation.Tab.Name.Advanced, 
                       tabOrder = Presentation.Tab.Order.Advanced,
                       group = Presentation.Group.Name.General, 
                       groupOrder = Presentation.Group.Order.General, 
                       excluded = true)
    protected String urlKey;

    @Column(name = "DISPLAY_TEMPLATE")
    @AdminPresentation(friendlyName = "ProductImpl_Product_Display_Template", 
                       tab = Presentation.Tab.Name.Advanced, 
                       tabOrder = Presentation.Tab.Order.Advanced,
                       group = Presentation.Group.Name.Advanced, 
                       groupOrder = Presentation.Group.Order.Advanced)
    protected String displayTemplate;

    @Column(name = "MODEL")
    @AdminPresentation(friendlyName = "ProductImpl_Product_Model",
                       tab = Presentation.Tab.Name.Advanced, 
                       tabOrder = Presentation.Tab.Order.Advanced,
                       group = Presentation.Group.Name.Advanced, 
                       groupOrder = Presentation.Group.Order.Advanced)
    protected String model;

    @Column(name = "MANUFACTURE")
    @AdminPresentation(friendlyName = "ProductImpl_Product_manufacturer", 
                       order = Presentation.FieldOrder.MANUFACTURER,
                       group = Presentation.Group.Name.General, 
                       groupOrder = Presentation.Group.Order.General, 
                       prominent = true, 
                       gridOrder = 4 )
    protected String manufacturer;
    
    @Column(name = "BREED")
    @AdminPresentation(friendlyName = "ProductImpl_Product_breed", 
                       order = Presentation.FieldOrder.BREED,
                       group = Presentation.Group.Name.General, 
                       groupOrder = Presentation.Group.Order.General, 
                       prominent = true, 
                       gridOrder = 4 )
    protected String breed;
    
    @Column(name = "GRADE")
    @AdminPresentation(friendlyName = "ProductImpl_Product_grade", 
                       order = Presentation.FieldOrder.GRADE,
                       group = Presentation.Group.Name.General, 
                       groupOrder = Presentation.Group.Order.General, 
                       prominent = true, 
                       gridOrder = 4 )
    protected String grade;
    
    @Column(name = "IS_FEATURED_PRODUCT", nullable=false)
    @AdminPresentation(friendlyName = "ProductImpl_Is_Featured_Product", 
                       requiredOverride = RequiredOverride.NOT_REQUIRED,
                       tab = Presentation.Tab.Name.Marketing, 
                       tabOrder = Presentation.Tab.Order.Marketing,
                       group = Presentation.Group.Name.Badges, 
                       groupOrder = Presentation.Group.Order.Badges)
    protected Boolean isFeaturedProduct = false;
    
    @Transient
    protected List<Sku> activeSkus = new ArrayList<Sku>();
    
    @Transient
    protected String promoMessage;

    @OneToMany(mappedBy="product", targetEntity=CrossSaleProductImpl.class, cascade =CascadeType.ALL, orphanRemoval=true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blProducts")
    @OrderBy(value="sequence")
    @AdminPresentationAdornedTargetCollection(
    		           friendlyName = "crossSaleProductsTitle", 
    		           order = 1000,
                       tab = Presentation.Tab.Name.Marketing, 
                       tabOrder = Presentation.Tab.Order.Marketing,
                       targetObjectProperty = "relatedSaleProduct", 
                       sortProperty = "sequence", 
                       maintainedAdornedTargetFields = { "promotionMessage" }, 
                       gridVisibleFields = { "name", "promotionMessage" })
    @ClonePolicyAdornedTargetCollection
    protected List<RelatedProduct> crossSaleProducts = new ArrayList<RelatedProduct>();

    @OneToMany(mappedBy = "product", targetEntity = UpSaleProductImpl.class, cascade=CascadeType.ALL, orphanRemoval=true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blProducts")
    @OrderBy(value="sequence")
    @AdminPresentationAdornedTargetCollection(
    				   friendlyName = "upsaleProductsTitle", 
    				   order = 2000,
                       tab = Presentation.Tab.Name.Marketing, 
                       tabOrder = Presentation.Tab.Order.Marketing,
                       targetObjectProperty = "relatedSaleProduct", 
                       sortProperty = "sequence",
                       maintainedAdornedTargetFields = { "promotionMessage" }, 
                       gridVisibleFields = { "name", "promotionMessage" })
    @ClonePolicyAdornedTargetCollection
    protected List<RelatedProduct> upSaleProducts  = new ArrayList<RelatedProduct>();

    @OneToMany(fetch = FetchType.LAZY, targetEntity = SkuImpl.class, mappedBy="product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blProducts")
    @BatchSize(size = 50)
    @AdminPresentationCollection(
    		           friendlyName="ProductImpl_Additional_Skus", 
    		           order = 1000,
                       tab = Presentation.Tab.Name.ProductSKU, 
                       tabOrder = Presentation.Tab.Order.ProductOptions)
    @ClonePolicyCollection
    protected List<Sku> allSkus = new ArrayList<Sku>();

    @OneToMany(targetEntity = CategoryProductXrefImpl.class, mappedBy = "product", orphanRemoval = true)
    @Cascade(value={org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST})
    @OrderBy(value="displayOrder")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blProducts")
    @BatchSize(size = 50)
    @AdminPresentationAdornedTargetCollection(
    					friendlyName = "allParentCategoriesTitle", 
    					order = 3000,
                        tab = Presentation.Tab.Name.Marketing, 
                        tabOrder = Presentation.Tab.Order.Marketing,
                        joinEntityClass = "cn.globalph.b2c.catalog.domain.CategoryProductXrefImpl",
                        targetObjectProperty = "category",
                        parentObjectProperty = "product",
                        gridVisibleFields = { "name" })
    @ClonePolicyAdornedTargetCollection(unowned = true)
    protected List<CategoryProductXref> allParentCategoryXrefs = new ArrayList<CategoryProductXref>();

    @OneToMany(mappedBy = "product", targetEntity = ProductAttributeImpl.class, cascade = { CascadeType.ALL }, orphanRemoval = true)
    @Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="blProducts")
    @MapKey(name="name")
    @BatchSize(size = 50)
    @AdminPresentationMap(friendlyName = "productAttributesTitle",
                          tab = Presentation.Tab.Name.Advanced, 
                          tabOrder = Presentation.Tab.Order.Advanced,
                          deleteEntityUponRemove = true, 
                          forceFreeFormKeys = true, 
                          keyPropertyFriendlyName = "ProductAttributeImpl_Attribute_Name" )
    @ClonePolicyMap
    protected Map<String, ProductAttribute> productAttributes = new HashMap<String, ProductAttribute>();

    @OneToMany(targetEntity = ProductOptionXrefImpl.class, mappedBy = "product")
    @Cascade(value={org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST})
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blProducts")
    @BatchSize(size = 50)
    @AdminPresentationAdornedTargetCollection(
    		              friendlyName = "productOptionsTitle",
                          tab = Presentation.Tab.Name.ProductSKU, 
                          tabOrder = Presentation.Tab.Order.ProductOptions,
                          joinEntityClass = "cn.globalph.b2c.product.domain.ProductOptionXrefImpl",
                          targetObjectProperty = "productOption",
                          parentObjectProperty = "product",
                          gridVisibleFields = {"label", "required"})
    protected List<ProductOptionXref> productOptions = new ArrayList<ProductOptionXref>();

    @Embedded
    protected ArchiveStatus archiveStatus = new ArchiveStatus();
     
    @Column(name = "ACTIVE_START_DATE")
    protected Date activeStartDate;

    @Column(name = "ACTIVE_END_DATE")
    protected Date activeEndDate;

    @ManyToOne(targetEntity = ProviderImpl.class, optional = false)
    @JoinColumn(name = "PROVIDER_ID")
    @AdminPresentation(friendlyName = "ProductImpl_Product_Provider",
            requiredOverride = RequiredOverride.REQUIRED,
            group = Presentation.Group.Name.General,
            groupOrder = Presentation.Group.Order.General,
            prominent = true,
            gridOrder = 4)
    @AdminPresentationToOneLookup(lookupDisplayProperty = "name")
    protected Provider provider;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean isActive() {
    	return getActiveSkus().isEmpty();
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String getManufacturer() {
        return manufacturer;
    }

    @Override
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    
    public String getBreed() {
		return breed;
	}

	public void setBreed(String breed) {
		this.breed = breed;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	@Override
    public boolean isFeaturedProduct() {
        return isFeaturedProduct;
    }

    @Override
    public void setFeaturedProduct(boolean isFeaturedProduct) {
        this.isFeaturedProduct = isFeaturedProduct;
    }

    @Override
    public String getPromoMessage() {
        return promoMessage;
    }

    @Override
    public void setPromoMessage(String promoMessage) {
        this.promoMessage = promoMessage;
    }

    @Override
    public Provider getProvider() {
        return provider;
    }

    @Override
    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    @Override
    public List<Sku> getAllSkus() {
        return allSkus;
    }

    @Override
    public List<Sku> getActiveSkus() {
        if (activeSkus.size() == 0) {
            for (Sku sku : allSkus) {
                if (sku.isActive()) {
                    activeSkus.add(sku);
                }
            }
        }
        return activeSkus;
     }
    
    @Override
    public void setAllSkus(List<Sku> skus) {
        this.allSkus.clear();
        for(Sku sku : skus){
            this.allSkus.add(sku);
           }
     }

    
    @Override
    public Map<String, Media> getAllSkuMedia() {
        Map<String, Media> result = new HashMap<String, Media>();
        for (Sku additionalSku : allSkus) {
            result.putAll(additionalSku.getSkuMedia());
           }
        return result;
    }

    @Override
    public List<CategoryProductXref> getAllParentCategoryXrefs() {
        WebRequestContext context = WebRequestContext.getWebRequestContext();
        if (context != null && context.getAdditionalProperties().containsKey("blProductEntityExtensionManager")) {
            ProductEntityExtensionManager extensionManager = (ProductEntityExtensionManager) context.getAdditionalProperties().get("blProductEntityExtensionManager");
            ExtensionResultHolder<List<CategoryProductXref>> holder = new ExtensionResultHolder<List<CategoryProductXref>>();
            ExtensionResultStatusType result = extensionManager.getHandlerProxy().getAllParentCategoryXrefs(this, holder);
            if (ExtensionResultStatusType.HANDLED.equals(result)) {
                return holder.getResult();
            }
        }
        return allParentCategoryXrefs;
    }

    @Override
    public void setAllParentCategoryXrefs(List<CategoryProductXref> allParentCategories) {
        this.allParentCategoryXrefs.clear();
        allParentCategoryXrefs.addAll(allParentCategories);
    }

    @Override
    @Deprecated
    public List<Category> getAllParentCategories() {
        WebRequestContext context = WebRequestContext.getWebRequestContext();
        if (context != null && context.getAdditionalProperties().containsKey("blProductEntityExtensionManager")) {
            ProductEntityExtensionManager extensionManager = (ProductEntityExtensionManager) context.getAdditionalProperties().get("blProductEntityExtensionManager");
            ExtensionResultHolder<List<Category>> holder = new ExtensionResultHolder<List<Category>>();
            ExtensionResultStatusType result = extensionManager.getHandlerProxy().getAllParentCategories(this, holder);
            if (ExtensionResultStatusType.HANDLED.equals(result)) {
                return holder.getResult();
            }
        }
        List<Category> parents = new ArrayList<Category>();
        for (CategoryProductXref xref : allParentCategoryXrefs) {
            parents.add(xref.getCategory());
        }
        return Collections.unmodifiableList(parents);
    }

    @Override
    @Deprecated
    public void setAllParentCategories(List<Category> allParentCategories) {
        throw new UnsupportedOperationException("Not Supported - Use setAllParentCategoryXrefs()");
    }

    @Override
    public List<RelatedProduct> getCrossSaleProducts() {
        WebRequestContext context = WebRequestContext.getWebRequestContext();
        if (context != null && context.getAdditionalProperties().containsKey("blProductEntityExtensionManager")) {
            ProductEntityExtensionManager extensionManager = (ProductEntityExtensionManager) context.getAdditionalProperties().get("blProductEntityExtensionManager");
            ExtensionResultHolder<List<RelatedProduct>> holder = new ExtensionResultHolder<List<RelatedProduct>>();
            ExtensionResultStatusType result = extensionManager.getHandlerProxy().getCrossSaleProducts(this, holder);
            if (ExtensionResultStatusType.HANDLED.equals(result)) {
                return holder.getResult();
            }
        }
        return crossSaleProducts;
    }

    @Override
    public void setCrossSaleProducts(List<RelatedProduct> crossSaleProducts) {
        this.crossSaleProducts.clear();
        for(RelatedProduct relatedProduct : crossSaleProducts){
            this.crossSaleProducts.add(relatedProduct);
        }       
    }

    @Override
    public List<RelatedProduct> getUpSaleProducts() {
        WebRequestContext context = WebRequestContext.getWebRequestContext();
        if (context != null && context.getAdditionalProperties().containsKey("blProductEntityExtensionManager")) {
            ProductEntityExtensionManager extensionManager = (ProductEntityExtensionManager) context.getAdditionalProperties().get("blProductEntityExtensionManager");
            ExtensionResultHolder<List<RelatedProduct>> holder = new ExtensionResultHolder<List<RelatedProduct>>();
            ExtensionResultStatusType result = extensionManager.getHandlerProxy().getUpSaleProducts(this, holder);
            if (ExtensionResultStatusType.HANDLED.equals(result)) {
                return holder.getResult();
            }
        }
        return upSaleProducts;
    }

    @Override
    public void setUpSaleProducts(List<RelatedProduct> upSaleProducts) {
        this.upSaleProducts.clear();
        for(RelatedProduct relatedProduct : upSaleProducts){
            this.upSaleProducts.add(relatedProduct);
        }
        this.upSaleProducts = upSaleProducts;
    }
    
    @Override
    public List<RelatedProduct> getCumulativeCrossSaleProducts() {
        List<RelatedProduct> returnProducts = getCrossSaleProducts();
       
        Iterator<RelatedProduct> itr = returnProducts.iterator();
        while(itr.hasNext()) {
            RelatedProduct relatedProduct = itr.next();
            if (relatedProduct.getRelatedProduct().equals(this)) {
                itr.remove();
            	}
           }
        return returnProducts;
     }
    
    @Override
    public List<RelatedProduct> getCumulativeUpSaleProducts() {
        List<RelatedProduct> returnProducts = getUpSaleProducts();
        Iterator<RelatedProduct> itr = returnProducts.iterator();
        while(itr.hasNext()) {
            RelatedProduct relatedProduct = itr.next();
            if (relatedProduct.getRelatedProduct().equals(this)) {
                itr.remove();
            	}
           }
        return returnProducts;
    }

    @Override
    public Map<String, ProductAttribute> getProductAttributes() {
        return productAttributes;
    }

    @Override
    public void setProductAttributes(Map<String, ProductAttribute> productAttributes) {
        this.productAttributes = productAttributes;
    }

    @Override
    public List<ProductOptionXref> getProductOptionXrefs() {
        WebRequestContext context = WebRequestContext.getWebRequestContext();
        if (context != null && context.getAdditionalProperties().containsKey("blProductEntityExtensionManager")) {
            ProductEntityExtensionManager extensionManager = (ProductEntityExtensionManager) context.getAdditionalProperties().get("blProductEntityExtensionManager");
            ExtensionResultHolder<List<ProductOptionXref>> holder = new ExtensionResultHolder<List<ProductOptionXref>>();
            ExtensionResultStatusType result = extensionManager.getHandlerProxy().getProductOptionXrefs(this, holder);
            if (ExtensionResultStatusType.HANDLED.equals(result)) {
                return holder.getResult();
            }
        }
        return productOptions;
    }

    @Override
    public void setProductOptionXrefs(List<ProductOptionXref> productOptions) {
        this.productOptions = productOptions;
    }

    @Override
    public List<ProductOption> getProductOptions() {
        WebRequestContext context = WebRequestContext.getWebRequestContext();
        if (context != null && context.getAdditionalProperties().containsKey("blProductEntityExtensionManager")) {
            ProductEntityExtensionManager extensionManager = (ProductEntityExtensionManager) context.getAdditionalProperties().get("blProductEntityExtensionManager");
            ExtensionResultHolder<List<ProductOption>> holder = new ExtensionResultHolder<List<ProductOption>>();
            ExtensionResultStatusType result = extensionManager.getHandlerProxy().getProductOptions(this, holder);
            if (ExtensionResultStatusType.HANDLED.equals(result)) {
                return holder.getResult();
            }
        }
        List<ProductOption> response = new ArrayList<ProductOption>();
        for (ProductOptionXref xref : productOptions) {
            response.add(xref.getProductOption());
        }
        return Collections.unmodifiableList(response);
    }
    
    @Override
    public String getUrl() {
        if (url == null) {
            return getGeneratedUrl();
        } else {
            return url;
        }
    }
    
    @Override
    public void setUrl(String url) {
        this.url = url;
    }
    
    @Override
    public String getDisplayTemplate() {
        return displayTemplate;
    }
    
    @Override
    public void setDisplayTemplate(String displayTemplate) {
        this.displayTemplate = displayTemplate;
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
    
     
    public Date getActiveStartDate() {
		  return activeStartDate;
	 }

	public void setActiveStartDate(Date activeStartDate) {
		this.activeStartDate = activeStartDate;
	}

	public Date getActiveEndDate() {
		return activeEndDate;
	}

	public void setActiveEndDate(Date activeEndDate) {
		this.activeEndDate = activeEndDate;
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((activeSkus == null) ? 0 : activeSkus.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProductImpl other = (ProductImpl) obj;

        if (id != null && other.id != null) {
            return id.equals(other.id);
        }

        if (activeSkus == null) {
            if (other.activeSkus != null)
                return false;
        } else if (!activeSkus.equals(other.activeSkus))
            return false;
        return true;
    }

    @Override
    public String getUrlKey() {
        if (urlKey != null) {
            return urlKey;
        } else {
            if (getName() != null) {
                String returnKey = getName().toLowerCase();
                
                returnKey = returnKey.replaceAll(" ","-");
                return returnKey.replaceAll("[^A-Za-z0-9/-]", "");
            }
        }
        return null;
    }

    @Override
    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    @Override
    public String getGeneratedUrl() {       
        return "product/" + getUrlKey();
    }

    @Override
    public void clearDynamicPrices() {
        for (Sku sku : getAllSkus()) {
            sku.clearDynamicPrices();
        }
    }
    
    @Override
    public String getMainEntityName() {
        String manufacturer = getManufacturer();
        return StringUtils.isBlank(manufacturer) ? getName() : manufacturer + " " + getName();
    }
    
    public static class Presentation {
        public static class Tab {
            public static class Name {
                public static final String Marketing = "ProductImpl_Marketing_Tab";
                public static final String Description = "SkuImpl_Description_Tab";
                public static final String Media = "SkuImpl_Media_Tab";
                public static final String ProductSKU = "ProductImpl_Product_SKU_Tab";
                public static final String Inventory = "ProductImpl_Inventory_Tab";
                public static final String Shipping = "ProductImpl_Shipping_Tab";
                public static final String Advanced = "ProductImpl_Advanced_Tab";
                public static final String SencondsKill = "秒杀";

            }
            
            public static class Order {
                public static final int Marketing = 2000;
                public static final int Description = 2500;
                public static final int Media = 3000;
                public static final int ProductOptions = 4000;
                public static final int Inventory = 5000;
                public static final int Shipping = 6000;
                public static final int Advanced = 7000;
            }
        }
            
        public static class Group {
            public static class Name {
                public static final String General = "ProductImpl_Product_Description";
                public static final String Price = "SkuImpl_Price";
                public static final String ActiveDateRange = "ProductImpl_Product_Active_Date_Range";
                public static final String Advanced = "ProductImpl_Advanced";
                public static final String Inventory = "SkuImpl_Sku_Inventory";
                public static final String Badges = "ProductImpl_Badges";
                public static final String Shipping = "ProductWeight_Shipping";
                public static final String Financial = "ProductImpl_Financial";
                public static final String SecondsKill = "秒杀";
            }
            
            public static class Order {
                public static final int General = 1000;
                public static final int Price = 2000;
                public static final int ActiveDateRange = 3000;
                public static final int Advanced = 1000;
                public static final int Inventory = 1000;
                public static final int Badges = 1000;
                public static final int Shipping = 1000;
            }

        }

        public static class FieldOrder {
            public static final int NAME = 1000;
            public static final int SHORT_DESCRIPTION = 2000;
            public static final int PRIMARY_MEDIA = 3000;
            public static final int LONG_DESCRIPTION = 4001;
            public static final int PARAM_DESCRIPTION = 4002;
            public static final int ORIGIN_DESCRIPTION = 4003;
            public static final int DEFAULT_CATEGORY = 5000;
            public static final int MANUFACTURER = 6000;
            public static final int BREED = 6001;
            public static final int GRADE = 6002;
            public static final int URL = 7000;     
        }
    }

    @Override
    public String getLocation() {
        return getUrl();
    }

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

}