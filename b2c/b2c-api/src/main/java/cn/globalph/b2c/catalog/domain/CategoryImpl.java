package cn.globalph.b2c.catalog.domain;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.b2c.catalog.extension.CategoryEntityExtensionManager;
import cn.globalph.b2c.inventory.service.type.InventoryType;
import cn.globalph.b2c.order.service.type.FulfillmentType;
import cn.globalph.b2c.product.domain.Product;
import cn.globalph.b2c.search.domain.CategorySearchFacet;
import cn.globalph.b2c.search.domain.CategorySearchFacetImpl;
import cn.globalph.common.admin.domain.AdminMainEntity;
import cn.globalph.common.cache.Hydrated;
import cn.globalph.common.cache.HydratedSetup;
import cn.globalph.common.cache.engine.CacheFactoryException;
import cn.globalph.common.extensibility.jpa.clone.ClonePolicyAdornedTargetCollection;
import cn.globalph.common.extensibility.jpa.clone.ClonePolicyMap;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;
import cn.globalph.common.i18n.service.DynamicTranslationProvider;
import cn.globalph.common.media.domain.Media;
import cn.globalph.common.media.domain.MediaImpl;
import cn.globalph.common.persistence.ArchiveStatus;
import cn.globalph.common.persistence.Status;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationAdornedTargetCollection;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.AdminPresentationMap;
import cn.globalph.common.presentation.AdminPresentationMapKey;
import cn.globalph.common.presentation.AdminPresentationToOneLookup;
import cn.globalph.common.presentation.ValidationConfiguration;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.common.presentation.client.VisibilityEnum;
import cn.globalph.common.util.DateUtil;
import cn.globalph.common.util.UrlUtil;
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
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="NPH_CATEGORY")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blCategories")
@AdminPresentationClass(friendlyName = "CategoryImpl_baseCategory")
@SQLDelete(sql="UPDATE NPH_CATEGORY SET ARCHIVED = 'Y' WHERE CATEGORY_ID = ?")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps = true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX_PRECLONE_INFORMATION),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_CATALOG)
})
public class CategoryImpl implements Category, Status, AdminMainEntity, Locatable {

    private static final long serialVersionUID = 1L;
    private static final Log LOG = LogFactory.getLog(CategoryImpl.class);

    private static String buildLink(Category category, boolean ignoreTopLevel) {
        Category myCategory = category;
        StringBuilder linkBuffer = new StringBuilder(50);
        while (myCategory != null) {
            if (!ignoreTopLevel || myCategory.getDefaultParentCategory() != null) {
                if (linkBuffer.length() == 0) {
                    linkBuffer.append(myCategory.getUrlKey());
                } else if(myCategory.getUrlKey() != null && !"/".equals(myCategory.getUrlKey())){
                    linkBuffer.insert(0, myCategory.getUrlKey() + '/');
                }
            }
            myCategory = myCategory.getDefaultParentCategory();
        }

        return linkBuffer.toString();
    }

    private static void fillInURLMapForCategory(Map<String, List<Long>> categoryUrlMap, Category category, String startingPath, List<Long> startingCategoryList) throws CacheFactoryException {
        String urlKey = category.getUrlKey();
        if (urlKey == null) {
            throw new CacheFactoryException("Cannot create childCategoryURLMap - the urlKey for a category("+category.getId()+") was null");
        }

        String currentPath = "";
        if (! "/".equals(category.getUrlKey())) {
            currentPath = startingPath + "/" + category.getUrlKey();
        }

        List<Long> newCategoryList = new ArrayList<Long>(startingCategoryList);
        newCategoryList.add(category.getId());

        categoryUrlMap.put(currentPath, newCategoryList);
        for (CategoryXref currentCategory : category.getChildCategoryXrefs()) {
            fillInURLMapForCategory(categoryUrlMap, currentCategory.getSubCategory(), currentPath, newCategoryList);
        }
    }

    @Id
    @GeneratedValue(generator= "CategoryId")
    @GenericGenerator(
        name="CategoryId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="CategoryImpl"),
            @Parameter(name="entity_name", value="cn.globalph.b2c.catalog.domain.CategoryImpl")
        }
    )
    @Column(name = "CATEGORY_ID")
    @AdminPresentation(friendlyName = "CategoryImpl_Category_ID", visibility = VisibilityEnum.HIDDEN_ALL)
    protected Long id;

    @Column(name = "NAME", nullable=false)
    @Index(name="CATEGORY_NAME_INDEX", columnNames={"NAME"})
    @AdminPresentation( friendlyName = "CategoryImpl_Category_Name", order = 1000,
                        group = Presentation.Group.Name.General, 
                        groupOrder = Presentation.Group.Order.General,
                        prominent = true, 
                        gridOrder = 1, 
                        columnWidth = "300px",
                        translatable = true )
    protected String name;

    @Column(name = "URL")
    @AdminPresentation(friendlyName = "CategoryImpl_Category_Url", order = 2000,
                       group = Presentation.Group.Name.General, 
                       groupOrder = Presentation.Group.Order.General,
                       prominent = true, 
                       gridOrder = 2, 
                       columnWidth = "300px",
                       validationConfigurations = { @ValidationConfiguration(validationImplementation = "blUriPropertyValidator") })
    @Index(name="CATEGORY_URL_INDEX", columnNames={"URL"})
    protected String url;

    @Column(name = "URL_KEY")
    @Index(name="CATEGORY_URLKEY_INDEX", columnNames={"URL_KEY"})
    @AdminPresentation(friendlyName = "CategoryImpl_Category_Url_Key",
                       tab = Presentation.Tab.Name.Advanced, 
                       tabOrder = Presentation.Tab.Order.Advanced,
                       group = Presentation.Group.Name.Advanced, 
                       groupOrder = Presentation.Group.Order.Advanced,
                       excluded = true)
    protected String urlKey;

    @Column(name = "DESCRIPTION")
    @AdminPresentation(friendlyName = "CategoryImpl_Category_Description",
                       group = Presentation.Group.Name.General, 
                       groupOrder = Presentation.Group.Order.General,
                       largeEntry = true,
                       excluded = true,
                       translatable = true)
    protected String description;

    @Column(name = "ACTIVE_START_DATE")
    @AdminPresentation(friendlyName = "CategoryImpl_Category_Active_Start_Date", 
                       order = 1000,
                       group = Presentation.Group.Name.ActiveDateRange, 
                       groupOrder = Presentation.Group.Order.ActiveDateRange)
    protected Date activeStartDate;

    @Column(name = "ACTIVE_END_DATE")
    @AdminPresentation(friendlyName = "CategoryImpl_Category_Active_End_Date", 
                       order = 2000,
                       group = Presentation.Group.Name.ActiveDateRange, 
                       groupOrder = Presentation.Group.Order.ActiveDateRange)
    protected Date activeEndDate;

    @Column(name = "DISPLAY_TEMPLATE")
    @AdminPresentation(friendlyName = "CategoryImpl_Category_Display_Template", 
                       order = 1000,
                       tab = Presentation.Tab.Name.Advanced, 
                       tabOrder = Presentation.Tab.Order.Advanced,
                       group = Presentation.Group.Name.Advanced, 
                       groupOrder = Presentation.Group.Order.Advanced)
    protected String displayTemplate;

    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    @Column(name = "LONG_DESCRIPTION", length = Integer.MAX_VALUE - 1)
    @AdminPresentation(friendlyName = "CategoryImpl_Category_Long_Description", 
                       order = 3000,
                       group = Presentation.Group.Name.General, 
                       groupOrder = Presentation.Group.Order.General,
                       largeEntry = true,
                       fieldType = SupportedFieldType.HTML_BASIC,
                       translatable = true)
    protected String longDescription;

    @ManyToOne(targetEntity = CategoryImpl.class)
    @JoinColumn(name = "DEFAULT_PARENT_CATEGORY_ID")
    @Index(name="CATEGORY_PARENT_INDEX", columnNames={"DEFAULT_PARENT_CATEGORY_ID"})
    @AdminPresentation(friendlyName = "CategoryImpl_defaultParentCategory", 
                       order = 4000,
                       group = Presentation.Group.Name.General, 
                       groupOrder = Presentation.Group.Order.General)
    @AdminPresentationToOneLookup()
    protected Category defaultParentCategory;

    @OneToMany(targetEntity = CategoryXrefImpl.class, mappedBy = "category", orphanRemoval = true)
    @Cascade(value={org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST})
    @OrderBy(value="displayOrder")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blCategories")
    @BatchSize(size = 50)
    @AdminPresentationAdornedTargetCollection(
                       targetObjectProperty = "subCategory",
                       parentObjectProperty = "category",
                       friendlyName = "allChildCategoriesTitle",
                       sortProperty = "displayOrder",
                       tab = Presentation.Tab.Name.Advanced, 
                       tabOrder = Presentation.Tab.Order.Advanced,
                       gridVisibleFields = { "name" })
    @ClonePolicyAdornedTargetCollection(unowned = true)
    protected List<CategoryXref> allChildCategoryXrefs = new ArrayList<CategoryXref>(10);

    @OneToMany(targetEntity = CategoryXrefImpl.class, mappedBy = "subCategory", orphanRemoval = true)
    @Cascade(value={org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST})
    @OrderBy(value="displayOrder")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blCategories")
    @BatchSize(size = 50)
    @AdminPresentationAdornedTargetCollection(
            				targetObjectProperty = "category",
            				parentObjectProperty = "subCategory",
            				friendlyName = "allParentCategoriesTitle",
            				tab = Presentation.Tab.Name.Advanced, 
            				tabOrder = Presentation.Tab.Order.Advanced,
            				gridVisibleFields = { "name" } )
    @ClonePolicyAdornedTargetCollection(unowned = true)
    protected List<CategoryXref> allParentCategoryXrefs = new ArrayList<CategoryXref>(10);

    @OneToMany(targetEntity = CategoryProductXrefImpl.class, mappedBy = "category", orphanRemoval = true)
    @Cascade(value={org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST})
    @OrderBy(value="displayOrder")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blCategories")
    @BatchSize(size = 50)
    @AdminPresentationAdornedTargetCollection(
            				targetObjectProperty = "product",
            				parentObjectProperty = "category",
            				friendlyName = "allProductsTitle",
            				sortProperty = "displayOrder",
            				tab = Presentation.Tab.Name.Products, 
            				tabOrder = Presentation.Tab.Order.Products,
            				gridVisibleFields = { "name" } )
    @ClonePolicyAdornedTargetCollection(unowned = true)
    protected List<CategoryProductXref> allProductXrefs = new ArrayList<CategoryProductXref>(10);

    @ManyToMany(targetEntity = MediaImpl.class, cascade=CascadeType.ALL)
    @JoinTable(name = "NPH_CATEGORY_MEDIA_MAP",
    		   joinColumns = @JoinColumn(name="CATEGORY_ID", referencedColumnName="CATEGORY_ID"),
               inverseJoinColumns = @JoinColumn(name = "MEDIA_ID", referencedColumnName = "MEDIA_ID"))
    @MapKeyColumn(name = "MAP_KEY")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blCategories")
    @BatchSize(size = 50)
    @AdminPresentationMap(
            	friendlyName = "SkuImpl_Sku_Media",
            	tab = Presentation.Tab.Name.Media, 
            	tabOrder = Presentation.Tab.Order.Media,
            	keyPropertyFriendlyName = "SkuImpl_Sku_Media_Key",
            	deleteEntityUponRemove = true,
            	mediaField = "url",
            	keys = {
                    @AdminPresentationMapKey(keyName = "primary", friendlyKeyName = "mediaPrimary"),
                    @AdminPresentationMapKey(keyName = "alt1", friendlyKeyName = "mediaAlternate1"),
                    @AdminPresentationMapKey(keyName = "alt2", friendlyKeyName = "mediaAlternate2"),
                    @AdminPresentationMapKey(keyName = "alt3", friendlyKeyName = "mediaAlternate3"),
                    @AdminPresentationMapKey(keyName = "alt4", friendlyKeyName = "mediaAlternate4"),
                    @AdminPresentationMapKey(keyName = "alt5", friendlyKeyName = "mediaAlternate5"),
                    @AdminPresentationMapKey(keyName = "alt6", friendlyKeyName = "mediaAlternate6")
            	}
    )
    @ClonePolicyMap
    protected Map<String, Media> categoryMedia = new HashMap<String , Media>(10);

    @OneToMany(mappedBy = "category", targetEntity = FeaturedProductImpl.class, cascade=CascadeType.ALL, orphanRemoval=true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blCategories")
    @OrderBy(value="sequence")
    @BatchSize(size = 50)
    @AdminPresentationAdornedTargetCollection( friendlyName = "featuredProductsTitle", order = 1000,
								               tab = Presentation.Tab.Name.Marketing, 
								               tabOrder = Presentation.Tab.Order.Marketing,
								               targetObjectProperty = "product",
								               sortProperty = "sequence",
								               maintainedAdornedTargetFields = { "promotionMessage" },
								               gridVisibleFields = { "name", "promotionMessage" })
    @ClonePolicyAdornedTargetCollection
    protected List<FeaturedProduct> featuredProducts = new ArrayList<FeaturedProduct>(10);
    
    @OneToMany(mappedBy = "category", targetEntity = CrossSaleProductImpl.class, cascade = CascadeType.ALL, orphanRemoval=true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blCategories")
    @OrderBy(value="sequence")
    @AdminPresentationAdornedTargetCollection(
    		                    friendlyName = "crossSaleProductsTitle", 
    		                    order = 2000,
                                tab = Presentation.Tab.Name.Marketing, 
                                tabOrder = Presentation.Tab.Order.Marketing,
                                targetObjectProperty = "relatedSaleProduct",
                                sortProperty = "sequence",
                                maintainedAdornedTargetFields = { "promotionMessage" },
                                gridVisibleFields = { "name", "promotionMessage" } )
    @ClonePolicyAdornedTargetCollection
    protected List<RelatedProduct> crossSaleProducts = new ArrayList<RelatedProduct>();

    @OneToMany(mappedBy = "category", targetEntity = UpSaleProductImpl.class, cascade = CascadeType.ALL, orphanRemoval=true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blCategories")
    @OrderBy(value="sequence")
    @AdminPresentationAdornedTargetCollection(friendlyName = "upsaleProductsTitle", 
                                              order = 3000,
            								  tab = Presentation.Tab.Name.Marketing, 
            								  tabOrder = Presentation.Tab.Order.Marketing,
                                              targetObjectProperty = "relatedSaleProduct",
                                              sortProperty = "sequence",
                                              maintainedAdornedTargetFields = { "promotionMessage" },
                                              gridVisibleFields = { "name", "promotionMessage" })
    @ClonePolicyAdornedTargetCollection
    protected List<RelatedProduct> upSaleProducts  = new ArrayList<RelatedProduct>();
    
    @OneToMany(mappedBy = "category", targetEntity = CategorySearchFacetImpl.class, cascade = CascadeType.ALL, orphanRemoval=true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blCategories")
    @OrderBy(value="sequence")
    @AdminPresentationAdornedTargetCollection(friendlyName = "categoryFacetsTitle", 
                                              order = 1000,
                                              tab = Presentation.Tab.Name.SearchFacets, 
                                              tabOrder = Presentation.Tab.Order.SearchFacets,
                                              targetObjectProperty = "searchFacet",
                                              sortProperty = "sequence",
                                              gridVisibleFields = { "field", "label", "searchDisplayPriority" })
    @BatchSize(size = 50)
    @ClonePolicyAdornedTargetCollection
    protected List<CategorySearchFacet> searchFacets  = new ArrayList<CategorySearchFacet>();
    
    @OneToMany(mappedBy = "category", targetEntity = CategoryAttributeImpl.class, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blCategories")
    @MapKey(name="name")
    @BatchSize(size = 50)
    @AdminPresentationMap(friendlyName = "categoryAttributesTitle",
        				  tab = Presentation.Tab.Name.Advanced, 
        				  tabOrder = Presentation.Tab.Order.Advanced,
                          deleteEntityUponRemove = true, 
                          forceFreeFormKeys = true, 
                          keyPropertyFriendlyName = "ProductAttributeImpl_Attribute_Name")
    @ClonePolicyMap
    protected Map<String, CategoryAttribute> categoryAttributes = new HashMap<String, CategoryAttribute>();

    @Column(name = "INVENTORY_TYPE")
    @AdminPresentation( friendlyName = "CategoryImpl_Category_InventoryType", 
                        order = 2000,
            		    helpText = "inventoryTypeHelpText",
                        tab = Presentation.Tab.Name.Advanced, 
                        tabOrder = Presentation.Tab.Order.Advanced,
                        group = Presentation.Group.Name.Advanced, 
                        groupOrder = Presentation.Group.Order.Advanced,
                        fieldType = SupportedFieldType.BROADLEAF_ENUMERATION,
                        broadleafEnumeration = "cn.globalph.b2c.inventory.service.type.InventoryType")
    protected String inventoryType;
    
    @Column(name = "FULFILLMENT_TYPE")
    @AdminPresentation( friendlyName = "CategoryImpl_Category_FulfillmentType", 
                        order = 3000,
                        tab = Presentation.Tab.Name.Advanced, 
                        tabOrder = Presentation.Tab.Order.Advanced,
                        group = Presentation.Group.Name.Advanced, 
                        groupOrder = Presentation.Group.Order.Advanced,
                        fieldType = SupportedFieldType.BROADLEAF_ENUMERATION,
                        broadleafEnumeration = "cn.globalph.b2c.order.service.type.FulfillmentType")
    protected String fulfillmentType;

    @Embedded
    protected ArchiveStatus archiveStatus = new ArchiveStatus();

    @Transient
    @Deprecated
    protected Map<String, List<Long>> childCategoryURLMap;

    @Transient
    @Hydrated(factoryMethod = "createChildCategoryIds")
    protected List<Long> childCategoryIds;

    @Transient
    protected List<CategoryXref> childCategoryXrefs = new ArrayList<CategoryXref>(50);

    @Transient
    protected List<Category> legacyChildCategories = new ArrayList<Category>(50);

    @Transient
    protected List<Category> allLegacyChildCategories = new ArrayList<Category>(50);

    @Transient
    protected List<FeaturedProduct> filteredFeaturedProducts = null;
    
    @Transient
    protected List<RelatedProduct> filteredCrossSales = null;

    @Transient
    protected List<RelatedProduct> filteredUpSales = null;
    
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
    public String getUrl() {
        // if null return
        // if blank return
        // if startswith "/" return
        // if contains a ":" and no "?" or (contains a ":" before a "?") return
        // else "add a /" at the beginning
        if(url == null || url.equals("") || url.startsWith("/")) {
            return url;       
        } else if ((url.contains(":") && !url.contains("?")) || url.indexOf('?', url.indexOf(':')) != -1) {
            return url;
        } else {
            return "/" + url;
        }
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getUrlKey() {
        if ((urlKey == null || "".equals(urlKey.trim())) && name != null) {
            return UrlUtil.generateUrlKey(name);
        }
        return urlKey;
    }

    @Override
    public String getGeneratedUrl() {
        return buildLink(this, false);
    }

    @Override
    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    @Override
    public String getDescription() {
        return DynamicTranslationProvider.getValue(this, "description", description);
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Date getActiveStartDate() {
        if ('Y'==getArchived()) {
            return null;
        }
        return activeStartDate;
    }

    @Override
    public void setActiveStartDate(Date activeStartDate) {
        this.activeStartDate = (activeStartDate == null) ? null : new Date(activeStartDate.getTime());
    }

    @Override
    public Date getActiveEndDate() {
        return activeEndDate;
    }

    @Override
    public void setActiveEndDate(Date activeEndDate) {
        this.activeEndDate = (activeEndDate == null) ? null : new Date(activeEndDate.getTime());
    }

    @Override
    public boolean isActive() {
        if (LOG.isDebugEnabled()) {
            if (!DateUtil.isActive(activeStartDate, activeEndDate, true)) {
                LOG.debug("category, " + id + ", inactive due to date");
            }
            if ('Y'==getArchived()) {
                LOG.debug("category, " + id + ", inactive due to archived status");
            }
        }
        return DateUtil.isActive(activeStartDate, activeEndDate, true) && 'Y'!=getArchived();
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
    public String getLongDescription() {
        return DynamicTranslationProvider.getValue(this, "longDescription", longDescription);
    }

    @Override
    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    @Override
    public Category getDefaultParentCategory() {
        return defaultParentCategory;
    }

    @Override
    public void setDefaultParentCategory(Category defaultParentCategory) {
        this.defaultParentCategory = defaultParentCategory;
    }

    @Override
    public List<CategoryXref> getAllChildCategoryXrefs(){
        WebRequestContext context = WebRequestContext.getWebRequestContext();
        if (context != null && context.getAdditionalProperties().containsKey("blCategoryEntityExtensionManager")) {
            CategoryEntityExtensionManager extensionManager = (CategoryEntityExtensionManager) context.getAdditionalProperties().get("blCategoryEntityExtensionManager");
            ExtensionResultHolder<List<CategoryXref>> holder = new ExtensionResultHolder<List<CategoryXref>>();
            ExtensionResultStatusType result = extensionManager.getHandlerProxy().getAllChildCategoryXrefs(this, holder);
            if (ExtensionResultStatusType.HANDLED.equals(result)) {
                return holder.getResult();
            }
        }
        return allChildCategoryXrefs;
    }

    @Override
    public List<CategoryXref> getChildCategoryXrefs() {
        WebRequestContext context = WebRequestContext.getWebRequestContext();
        if (context != null && context.getAdditionalProperties().containsKey("blCategoryEntityExtensionManager")) {
            CategoryEntityExtensionManager extensionManager = (CategoryEntityExtensionManager) context.getAdditionalProperties().get("blCategoryEntityExtensionManager");
            ExtensionResultHolder<List<CategoryXref>> holder = new ExtensionResultHolder<List<CategoryXref>>();
            ExtensionResultStatusType result = extensionManager.getHandlerProxy().getChildCategoryXrefs(this, holder);
            if (ExtensionResultStatusType.HANDLED.equals(result)) {
                return holder.getResult();
            }
        }
        if (childCategoryXrefs.isEmpty()) {
            for (CategoryXref category : allChildCategoryXrefs) {
                if (category.getSubCategory().isActive()) {
                    childCategoryXrefs.add(category);
                }
            }
        }
        return Collections.unmodifiableList(childCategoryXrefs);
    }

    @Override
    public void setChildCategoryXrefs(List<CategoryXref> childCategories) {
        this.childCategoryXrefs.clear();
        for(CategoryXref category : childCategories){
            this.childCategoryXrefs.add(category);
        }
    }

    @Override
    public void setAllChildCategoryXrefs(List<CategoryXref> childCategories){
        allChildCategoryXrefs.clear();
        for(CategoryXref category : childCategories){
            allChildCategoryXrefs.add(category);
        }
    }

    @Override
    public boolean hasAllChildCategories(){
        WebRequestContext context = WebRequestContext.getWebRequestContext();
        if (context != null && context.getAdditionalProperties().containsKey("blCategoryEntityExtensionManager")) {
            CategoryEntityExtensionManager extensionManager = (CategoryEntityExtensionManager) context.getAdditionalProperties().get("blCategoryEntityExtensionManager");
            ExtensionResultHolder<Boolean> holder = new ExtensionResultHolder<Boolean>();
            ExtensionResultStatusType result = extensionManager.getHandlerProxy().hasAllChildCategories(this, holder);
            if (ExtensionResultStatusType.HANDLED.equals(result)) {
                return holder.getResult();
            }
        }
        return !allChildCategoryXrefs.isEmpty();
    }

    @Override
    public boolean hasChildCategories() {
        WebRequestContext context = WebRequestContext.getWebRequestContext();
        if (context != null && context.getAdditionalProperties().containsKey("blCategoryEntityExtensionManager")) {
            CategoryEntityExtensionManager extensionManager = (CategoryEntityExtensionManager) context.getAdditionalProperties().get("blCategoryEntityExtensionManager");
            ExtensionResultHolder<Boolean> holder = new ExtensionResultHolder<Boolean>();
            ExtensionResultStatusType result = extensionManager.getHandlerProxy().hasChildCategories(this, holder);
            if (ExtensionResultStatusType.HANDLED.equals(result)) {
                return holder.getResult();
            }
        }
        return !getChildCategoryXrefs().isEmpty();
    }

    @Override
    public List<Long> getChildCategoryIds() {
        WebRequestContext context = WebRequestContext.getWebRequestContext();
        if (context != null && context.getAdditionalProperties().containsKey("blCategoryEntityExtensionManager")) {
            CategoryEntityExtensionManager extensionManager = (CategoryEntityExtensionManager) context.getAdditionalProperties().get("blCategoryEntityExtensionManager");
            ExtensionResultHolder<List<Long>> holder = new ExtensionResultHolder<List<Long>>();
            ExtensionResultStatusType result = extensionManager.getHandlerProxy().getChildCategoryIds(this, holder);
            if (ExtensionResultStatusType.HANDLED.equals(result)) {
                return holder.getResult();
            }
        }
        if (childCategoryIds == null) {
            HydratedSetup.populateFromCache(this, "childCategoryIds");
        }
        return childCategoryIds;
    }

    @Override
    public void setChildCategoryIds(List<Long> childCategoryIds) {
        this.childCategoryIds = childCategoryIds;
    }

    public List<Long> createChildCategoryIds() {
        childCategoryIds = new ArrayList<Long>();
        for (CategoryXref category : allChildCategoryXrefs) {
            if (category.getSubCategory().isActive()) {
                childCategoryIds.add(category.getSubCategory().getId());
            }
        }
        return childCategoryIds;
    }

    public Map<String, List<Long>> createChildCategoryURLMap() {
        try {
            Map<String, List<Long>> newMap = new HashMap<String, List<Long>>(50);
            fillInURLMapForCategory(newMap, this, "", new ArrayList<Long>(10));
            return newMap;
        } catch (CacheFactoryException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public List<Category> buildFullCategoryHierarchy(List<Category> currentHierarchy) {
        if (currentHierarchy == null) { 
            currentHierarchy = new ArrayList<Category>();
            currentHierarchy.add(this);
        }
        
        List<Category> myParentCategories = new ArrayList<Category>();
        if (getDefaultParentCategory() != null) {
            myParentCategories.add(getDefaultParentCategory());
        }
        if (!CollectionUtils.isEmpty(getAllParentCategoryXrefs())) {
            for (CategoryXref parent : getAllParentCategoryXrefs()) {
                myParentCategories.add(parent.getCategory());
            }
        }

        for (Category category : myParentCategories) {
            if (!currentHierarchy.contains(category)) {
                currentHierarchy.add(category);
                category.buildFullCategoryHierarchy(currentHierarchy);
            }
        }
        
        return currentHierarchy;
    }
    
    @Override
    public List<Category> buildCategoryHierarchy(List<Category> currentHierarchy) {
        if (currentHierarchy == null) {
            currentHierarchy = new ArrayList<Category>();
            currentHierarchy.add(this);
        }
        if (getDefaultParentCategory() != null && ! currentHierarchy.contains(getDefaultParentCategory())) {
            currentHierarchy.add(getDefaultParentCategory());
            getDefaultParentCategory().buildCategoryHierarchy(currentHierarchy);
        }
        return currentHierarchy;
    }

    @Override
    public List<CategoryXref> getAllParentCategoryXrefs() {
        WebRequestContext context = WebRequestContext.getWebRequestContext();
        if (context != null && context.getAdditionalProperties().containsKey("blCategoryEntityExtensionManager")) {
            CategoryEntityExtensionManager extensionManager = (CategoryEntityExtensionManager) context.getAdditionalProperties().get("blCategoryEntityExtensionManager");
            ExtensionResultHolder<List<CategoryXref>> holder = new ExtensionResultHolder<List<CategoryXref>>();
            ExtensionResultStatusType result = extensionManager.getHandlerProxy().getAllParentCategoryXrefs(this, holder);
            if (ExtensionResultStatusType.HANDLED.equals(result)) {
                return holder.getResult();
            }
        }
        return allParentCategoryXrefs;
    }

    @Override
    public void setAllParentCategoryXrefs(List<CategoryXref> allParentCategories) {
        this.allParentCategoryXrefs.clear();
        allParentCategoryXrefs.addAll(allParentCategories);
    }

    @Override
    public List<FeaturedProduct> getFeaturedProducts() {
        WebRequestContext context = WebRequestContext.getWebRequestContext();
        if (context != null && context.getAdditionalProperties().containsKey("blCategoryEntityExtensionManager")) {
            CategoryEntityExtensionManager extensionManager = (CategoryEntityExtensionManager) context.getAdditionalProperties().get("blCategoryEntityExtensionManager");
            ExtensionResultHolder<List<FeaturedProduct>> holder = new ExtensionResultHolder<List<FeaturedProduct>>();
            ExtensionResultStatusType result = extensionManager.getHandlerProxy().getFeaturedProducts(this, holder);
            if (ExtensionResultStatusType.HANDLED.equals(result)) {
                return holder.getResult();
            }
        }
        return featuredProducts;
    }

    @Override
    public void setFeaturedProducts(List<FeaturedProduct> featuredProducts) {
        this.featuredProducts.clear();
        for(FeaturedProduct featuredProduct : featuredProducts){
            this.featuredProducts.add(featuredProduct);
        }
    }
    
    @Override
    public List<RelatedProduct> getCrossSaleProducts() {
        WebRequestContext context = WebRequestContext.getWebRequestContext();
        if (context != null && context.getAdditionalProperties().containsKey("blCategoryEntityExtensionManager")) {
            CategoryEntityExtensionManager extensionManager = (CategoryEntityExtensionManager) context.getAdditionalProperties().get("blCategoryEntityExtensionManager");
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
        if (context != null && context.getAdditionalProperties().containsKey("blCategoryEntityExtensionManager")) {
            CategoryEntityExtensionManager extensionManager = (CategoryEntityExtensionManager) context.getAdditionalProperties().get("blCategoryEntityExtensionManager");
            ExtensionResultHolder<List<RelatedProduct>> holder = new ExtensionResultHolder<List<RelatedProduct>>();
            ExtensionResultStatusType result = extensionManager.getHandlerProxy().getUpSaleProducts(this, holder);
            if (ExtensionResultStatusType.HANDLED.equals(result)) {
                return holder.getResult();
            }
        }
        return upSaleProducts;
    }
    
    @Override
    public List<RelatedProduct> getCumulativeCrossSaleProducts() {
        Set<RelatedProduct> returnProductsSet = new LinkedHashSet<RelatedProduct>();
                
        List<Category> categoryHierarchy = buildCategoryHierarchy(null);
        for (Category category : categoryHierarchy) {
            returnProductsSet.addAll(category.getCrossSaleProducts());
        }
        return new ArrayList<RelatedProduct>(returnProductsSet);
    }
    
    @Override
    public List<RelatedProduct> getCumulativeUpSaleProducts() {
        Set<RelatedProduct> returnProductsSet = new LinkedHashSet<RelatedProduct>();
        
        List<Category> categoryHierarchy = buildCategoryHierarchy(null);
        for (Category category : categoryHierarchy) {
            returnProductsSet.addAll(category.getUpSaleProducts());
        }
        return new ArrayList<RelatedProduct>(returnProductsSet);
    }

    @Override
    public List<FeaturedProduct> getCumulativeFeaturedProducts() {
        Set<FeaturedProduct> returnProductsSet = new LinkedHashSet<FeaturedProduct>();
        
        List<Category> categoryHierarchy = buildCategoryHierarchy(null);
        for (Category category : categoryHierarchy) {
            returnProductsSet.addAll(category.getFeaturedProducts());
        }
        return new ArrayList<FeaturedProduct>(returnProductsSet);
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
    public List<CategoryProductXref> getActiveProductXrefs() {
        WebRequestContext context = WebRequestContext.getWebRequestContext();
        if (context != null && context.getAdditionalProperties().containsKey("blCategoryEntityExtensionManager")) {
            CategoryEntityExtensionManager extensionManager = (CategoryEntityExtensionManager) context.getAdditionalProperties().get("blCategoryEntityExtensionManager");
            ExtensionResultHolder<List<CategoryProductXref>> holder = new ExtensionResultHolder<List<CategoryProductXref>>();
            ExtensionResultStatusType result = extensionManager.getHandlerProxy().getActiveProductXrefs(this, holder);
            if (ExtensionResultStatusType.HANDLED.equals(result)) {
                return holder.getResult();
            }
        }
        List<CategoryProductXref> result = new ArrayList<CategoryProductXref>();
        for (CategoryProductXref product : allProductXrefs) {
            if (product.getProduct().isActive()) {
                result.add(product);
            }
        }
        return Collections.unmodifiableList(result);
    }

    @Override
    public List<CategoryProductXref> getAllProductXrefs() {
        WebRequestContext context = WebRequestContext.getWebRequestContext();
        if (context != null && context.getAdditionalProperties().containsKey("blCategoryEntityExtensionManager")) {
            CategoryEntityExtensionManager extensionManager = (CategoryEntityExtensionManager) context.getAdditionalProperties().get("blCategoryEntityExtensionManager");
            ExtensionResultHolder<List<CategoryProductXref>> holder = new ExtensionResultHolder<List<CategoryProductXref>>();
            ExtensionResultStatusType result = extensionManager.getHandlerProxy().getAllProductXrefs(this, holder);
            if (ExtensionResultStatusType.HANDLED.equals(result)) {
                return holder.getResult();
            }
        }
        return allProductXrefs;
    }

    @Override
    public void setAllProductXrefs(List<CategoryProductXref> allProducts) {
        this.allProductXrefs.clear();
        allProductXrefs.addAll(allProducts);
    }

    @Override
    @Deprecated
    public List<Product> getActiveProducts() {
        WebRequestContext context = WebRequestContext.getWebRequestContext();
        if (context != null && context.getAdditionalProperties().containsKey("blCategoryEntityExtensionManager")) {
            CategoryEntityExtensionManager extensionManager = (CategoryEntityExtensionManager) context.getAdditionalProperties().get("blCategoryEntityExtensionManager");
            ExtensionResultHolder<List<Product>> holder = new ExtensionResultHolder<List<Product>>();
            ExtensionResultStatusType result = extensionManager.getHandlerProxy().getActiveProducts(this, holder);
            if (ExtensionResultStatusType.HANDLED.equals(result)) {
                return holder.getResult();
            }
        }
        List<Product> result = new ArrayList<Product>();
        for (CategoryProductXref product : allProductXrefs) {
            if (product.getProduct().isActive()) {
                result.add(product.getProduct());
            }
        }
        return Collections.unmodifiableList(result);
    }
    
    @Override
    @Deprecated
    public List<Product> getAllProducts() {
        WebRequestContext context = WebRequestContext.getWebRequestContext();
        if (context != null && context.getAdditionalProperties().containsKey("blCategoryEntityExtensionManager")) {
            CategoryEntityExtensionManager extensionManager = (CategoryEntityExtensionManager) context.getAdditionalProperties().get("blCategoryEntityExtensionManager");
            ExtensionResultHolder<List<Product>> holder = new ExtensionResultHolder<List<Product>>();
            ExtensionResultStatusType result = extensionManager.getHandlerProxy().getAllProducts(this, holder);
            if (ExtensionResultStatusType.HANDLED.equals(result)) {
                return holder.getResult();
            }
        }
        List<Product> result = new ArrayList<Product>();
        for (CategoryProductXref product : allProductXrefs) {
            result.add(product.getProduct());
        }
        return Collections.unmodifiableList(result);
    }

    @Override
    @Deprecated
    public void setAllProducts(List<Product> allProducts) {
        throw new UnsupportedOperationException("Not Supported - Use setAllProductXrefs()");
    }
    
    @Override
    public List<CategorySearchFacet> getSearchFacets() {
        return searchFacets;
    }

    @Override
    public void setSearchFacets(List<CategorySearchFacet> searchFacets) {
        this.searchFacets = searchFacets;
    }
    
    @Override
    public InventoryType getInventoryType() {
        return InventoryType.getInstance(this.inventoryType);
    }

    @Override
    public void setInventoryType(InventoryType inventoryType) {
        this.inventoryType = inventoryType.getType();
    }
    
    @Override
    public FulfillmentType getFulfillmentType() {
        return FulfillmentType.getInstance(this.fulfillmentType);
    }
    
    @Override
    public void setFulfillmentType(FulfillmentType fulfillmentType) {
        this.fulfillmentType = fulfillmentType.getType();
    }

    @Override
    public List<CategorySearchFacet> getCumulativeSearchFacets() {
        final List<CategorySearchFacet> returnFacets = new ArrayList<CategorySearchFacet>();
        returnFacets.addAll( getSearchFacets() );
        Collections.sort(returnFacets, facetPositionComparator);
        
        return returnFacets;
    }

    @Override
    public Map<String, Media> getCategoryMedia() {
        return categoryMedia;
    }

    @Override
    public void setCategoryMedia(Map<String, Media> categoryMedia) {
        this.categoryMedia.clear();
        for(Map.Entry<String, Media> me : categoryMedia.entrySet()) {
            this.categoryMedia.put(me.getKey(), me.getValue());
        }
    }
    
    @Override
    public Map<String, CategoryAttribute> getCategoryAttributesMap() {
        return categoryAttributes;
    }
    
    @Override
    public void setCategoryAttributesMap(Map<String, CategoryAttribute> categoryAttributes) {
        this.categoryAttributes = categoryAttributes;
    }

    @Override
    public List<CategoryAttribute> getCategoryAttributes() {
        List<CategoryAttribute> ca = new ArrayList<CategoryAttribute>(categoryAttributes.values());
        return Collections.unmodifiableList(ca);
    }

    @Override
    public void setCategoryAttributes(List<CategoryAttribute> categoryAttributes) {
        this.categoryAttributes = new HashMap<String, CategoryAttribute>();
        for (CategoryAttribute categoryAttribute : categoryAttributes) {
            this.categoryAttributes.put(categoryAttribute.getName(), categoryAttribute);
        }
    }
    
    @Override
    public CategoryAttribute getCategoryAttributeByName(String name) {
        for (CategoryAttribute attribute : getCategoryAttributes()) {
            if (attribute.getName().equals(name)) {
                return attribute;
            }
        }
        return null;
    }

    @Override
    public Map<String, CategoryAttribute> getMappedCategoryAttributes() {
        Map<String, CategoryAttribute> map = new HashMap<String, CategoryAttribute>();
        for (CategoryAttribute attr : getCategoryAttributes()) {
            map.put(attr.getName(), attr);
        }
        return map;
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
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result + (url == null ? 0 : url.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CategoryImpl other = (CategoryImpl) obj;

        if (id != null && other.id != null) {
            return id.equals(other.id);
        }

        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (url == null) {
            if (other.url != null) {
                return false;
            }
        } else if (!url.equals(other.url)) {
            return false;
        }
        return true;
    }
    
    protected static Comparator<CategorySearchFacet> facetPositionComparator = new Comparator<CategorySearchFacet>() {
        @Override
        public int compare(CategorySearchFacet o1, CategorySearchFacet o2) {
            return o1.getSequence().compareTo(o2.getSequence());
        }
    };

    public static class Presentation {
        public static class Tab {
            public static class Name {
                public static final String Marketing = "CategoryImpl_Marketing_Tab";
                public static final String Media = "CategoryImpl_Media_Tab";
                public static final String Advanced = "CategoryImpl_Advanced_Tab";
                public static final String Products = "CategoryImpl_Products_Tab";
                public static final String SearchFacets = "CategoryImpl_categoryFacetsTab";
            }

            public static class Order {
                public static final int Marketing = 2000;
                public static final int Media = 3000;
                public static final int Advanced = 4000;
                public static final int Products = 5000;
                public static final int SearchFacets = 3500;
            }
        }

        public static class Group {
            public static class Name {
                public static final String General = "CategoryImpl_Category_Description";
                public static final String ActiveDateRange = "CategoryImpl_Active_Date_Range";
                public static final String Advanced = "CategoryImpl_Advanced";
            }

            public static class Order {
                public static final int General = 1000;
                public static final int ActiveDateRange = 2000;
                public static final int Advanced = 1000;
            }
        }
    }

    @Override
    public String getMainEntityName() {
        return getName();
    }

    @Override
    public String getLocation() {
        return getUrl();
    }

}