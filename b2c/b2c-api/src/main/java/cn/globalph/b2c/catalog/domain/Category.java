package cn.globalph.b2c.catalog.domain;

import cn.globalph.b2c.inventory.service.type.InventoryType;
import cn.globalph.b2c.order.service.type.FulfillmentType;
import cn.globalph.b2c.product.domain.Product;
import cn.globalph.b2c.search.domain.CategorySearchFacet;
import cn.globalph.common.media.domain.Media;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Implementations of this interface are used to hold data about a Category.  A category is a group of products.
 * <br>
 * <br>
 * You should implement this class if you want to make significant changes to how the
 * Category is persisted.  If you just want to add additional fields then you should extend {@link CategoryImpl}.
 *
 * @see {@link CategoryImpl}
 * 
 */
public interface Category extends Serializable {

    /**
     * Gets the primary key.
     * 
     * @return the primary key
     */
    @Nullable
    public Long getId();

    /**
     * Sets the primary key.
     * 
     * @param id the new primary key
     */
    public void setId(@Nullable Long id);

    /**
     * Gets the name.
     * 
     * @return the name
     */
    @Nonnull
    public String getName();

    /**
     * Sets the name.
     * 
     * @param name the new name
     */
    public void setName(@Nonnull String name);

    /**
     * Gets the default parent category.
     * 
     * @return the default parent category
     */
    @Nullable
    public Category getDefaultParentCategory();

    /**
     * Sets the default parent category.
     * 
     * @param defaultParentCategory the new default parent category
     */
    public void setDefaultParentCategory(@Nullable Category defaultParentCategory);

    /**
     * Gets the url. The url represents the presentation layer destination for
     * this category. For example, if using Spring MVC, you could send the user
     * to this destination by returning {@code "redirect:"+currentCategory.getUrl();}
     * from a controller.
     * 
     * @return the url for the presentation layer component for this category
     */
    @Nullable
    public String getUrl();

    /**
     * Sets the url. The url represents the presentation layer destination for
     * this category. For example, if using Spring MVC, you could send the user
     * to this destination by returning {@code "redirect:"+currentCategory.getUrl();}
     * from a controller.
     * 
     * @param url the new url for the presentation layer component for this category
     */
    public void setUrl(@Nullable String url);

    /**
     * Gets the url key. The url key is used as part of SEO url generation for this
     * category. Each segment of the url leading to a category is comprised of the url
     * keys of the various associated categories in a hierarchy leading to this one. If
     * the url key is null, the the name for the category formatted with dashes for spaces.
     * 
     * @return the url key for this category to appear in the SEO url
     */
    @Nullable
    public String getUrlKey();

    /**
     * Creates the SEO url starting from this category and recursing up the
     * hierarchy of default parent categories until the topmost category is
     * reached. The url key for each category is used for each segment
     * of the SEO url.
     * 
     * @return the generated SEO url for this category
     */
    @Nullable
    public String getGeneratedUrl();

    /**
     * Sets the url key. The url key is used as part of SEO url generation for this
     * category. Each segment of the url leading to a category is comprised of the url
     * keys of the various associated categories in a hierarchy leading to this one.
     * 
     * @param urlKey the new url key for this category to appear in the SEO url
     */
    public void setUrlKey(@Nullable String urlKey);

    /**
     * Gets the description.
     * 
     * @return the description
     */
    @Nullable
    public String getDescription();

    /**
     * Sets the description.
     * 
     * @param description the new description
     */
    public void setDescription(@Nullable String description);

    /**
     * Gets the active start date. If the current date is before activeStartDate,
     * then this category will not be visible on the site.
     * 
     * @return the active start date
     */
    @Nullable
    public Date getActiveStartDate();

    /**
     * Sets the active start date. If the current date is before activeStartDate,
     * then this category will not be visible on the site.
     * 
     * @param activeStartDate the new active start date
     */
    public void setActiveStartDate(@Nullable Date activeStartDate);

    /**
     * Gets the active end date. If the current date is after activeEndDate,
     * the this category will not be visible on the site.
     * 
     * @return the active end date
     */
    @Nullable
    public Date getActiveEndDate();

    /**
     * Sets the active end date. If the current date is after activeEndDate,
     * the this category will not be visible on the site.
     * 
     * @param activeEndDate the new active end date
     */
    public void setActiveEndDate(@Nullable Date activeEndDate);

    /**
     * Checks if is active. Returns true if the startDate is null or if the current
     * date is after the start date, or if the endDate is null or if the current date
     * is before the endDate.
     * 
     * @return true, if is active
     */
    public boolean isActive();

    /**
     * Gets the display template. The display template can be used to help create a unique key
     * that drives the presentation layer destination for this category. For example, if
     * using Spring MVC, you might derive the view destination in this way:
     *
     * {@code view = categoryTemplatePrefix + currentCategory.getDisplayTemplate();}
     * 
     * @return the display template
     */
    @Nullable
    public String getDisplayTemplate();

    /**
     * Sets the display template. The display template can be used to help create a unique key
     * that drives the presentation layer destination for this category. For example, if
     * using Spring MVC, you might derive the view destination in this way:
     *
     * {@code view = categoryTemplatePrefix + currentCategory.getDisplayTemplate();}
     * 
     * @param displayTemplate the new display template
     */
    public void setDisplayTemplate(@Nullable String displayTemplate);

    /**
     * Gets the category media map. The key is of arbitrary meaning
     * and the {@code Media} instance stores information about the
     * media itself (image url, etc...)
     * 
     * @return the category Media
     */
    @Nonnull
    public Map<String, Media> getCategoryMedia() ;

    /**
     * Sets the category media. The key is of arbitrary meaning
     * and the {@code Media} instance stores information about the
     * media itself (image url, etc...)
     * 
     * @param categoryMedia the category media
     */
    public void setCategoryMedia(@Nonnull Map<String, Media> categoryMedia);

    /**
     * Gets the long description.
     * 
     * @return the long description
     */
    @Nullable
    public String getLongDescription();

    /**
     * Sets the long description.
     * 
     * @param longDescription the new long description
     */
    public void setLongDescription(@Nullable String longDescription);

    /**
     * Gets the featured products. Featured products are a special list
     * of products you would like to showcase for this category.
     * 
     * @return the featured products
     */
    @Nonnull
    public List<FeaturedProduct> getFeaturedProducts();

    /**
     * Sets the featured products. Featured products are a special list
     * of products you would like to showcase for this category.
     * 
     * @param featuredProducts the featured products
     */
    public void setFeaturedProducts(@Nonnull List<FeaturedProduct> featuredProducts);

    /**
     * Returns a list of cross sale products that are related to this category.
     * 
     * @return a list of cross sale products
     */
    public List<RelatedProduct> getCrossSaleProducts();

    /**
     * Sets the cross sale products that are related to this category.
     * 
     * @see #getCrossSaleProducts()
     * @param crossSaleProducts
     */
    public void setCrossSaleProducts(List<RelatedProduct> crossSaleProducts);

    /**
     * Returns a list of cross sale products that are related to this category.
     * 
     * @return a list of cross sale products
     */
    public List<RelatedProduct> getUpSaleProducts();

    /**
     * Sets the upsale products that are related to this category.
     * 
     * @see #getUpSaleProducts()
     * @param upSaleProducts
     */
    public void setUpSaleProducts(List<RelatedProduct> upSaleProducts);

    /** 
     * Returns a list of the cross sale products in this category as well as
     * all cross sale products in all parent categories of this category.
     * 
     * @return the cumulative cross sale products
     */
    public List<RelatedProduct> getCumulativeCrossSaleProducts();
    
    /** 
     * Returns a list of the upsale products in this category as well as
     * all upsale products in all parent categories of this category.
     * 
     * @return the cumulative upsale products
     */
    public List<RelatedProduct> getCumulativeUpSaleProducts();

    /**
     * Returns a list of the featured products in this category as well as
     * all featured products in all parent categories of this category.
     * 
     * @return the cumulative featured products
     */
    public List<FeaturedProduct> getCumulativeFeaturedProducts();

    /**
     * Returns all of the SearchFacets that are directly associated with this Category
     * 
     * @return related SearchFacets
     */
    public List<CategorySearchFacet> getSearchFacets();

    /**
     * Sets the SearchFacets that are directly associated with this Category
     * 
     * @param searchFacets
     */
    public void setSearchFacets(List<CategorySearchFacet> searchFacets);

    /**
     * Returns a list of CategorySearchFacets that takes into consideration the search facets for this Category,
     * the search facets for all parent categories, and the search facets that should be excluded from this 
     * Category. This method will order the resulting list based on the {@link CategorySearchFacet#getPosition()}
     * method for each category level. That is, the facets on this Category will be ordered by their position
     * relative to each other with the ordered parent facets after that, etc.
     * 
     * @return the current active search facets for this category and all parent categories
     */
    public List<CategorySearchFacet> getCumulativeSearchFacets();
    
    /**
     * Build category hierarchy by walking the default category tree up to the root category.
     * If the passed in tree is null then create the initial list.
     * 
     * @param currentHierarchy
     * @return
     */
    
    public List<Category> buildCategoryHierarchy(List<Category> currentHierarchy);
    
    /**
     * Build the full category hierarchy by walking up the default category tree and the all parent
     * category tree.
     * 
     * @param currentHierarchy
     * @return the full hierarchy
     */
    public List<Category> buildFullCategoryHierarchy(List<Category> currentHierarchy);

    /**
     * Gets the attributes for this {@link Category}. In smaller sites, using these attributes might be preferred to
     * extending the domain object itself.
     * 
     * @return
     * @see {@link #getMappedCategoryAttributes()}
     */
    public Map<String, CategoryAttribute> getCategoryAttributesMap();

    public void setCategoryAttributesMap(Map<String, CategoryAttribute> categoryAttributes);
    
    /**
     * Gets the attributes for this {@link Category}. In smaller sites, using these attributes might be preferred to
     * extending the domain object itself.
     * 
     * @return
     * @see {@link #getMappedCategoryAttributes()}
     * @deprecated This will be replaced with {@link #getCategoryAttributesMap()} in 3.1.0.
     */
    @Deprecated
    public List<CategoryAttribute> getCategoryAttributes();

    /**
     * Sets the attributes for this {@link Category}. In smaller sites, using these attributes might be preferred to
     * extending the domain object and creating a new table to store custom properties.
     * 
     * @return
     * @deprecated This will be replaced with {@link #setCategoryAttributesMap()} in 3.1.0.
     */
    @Deprecated
    public void setCategoryAttributes(List<CategoryAttribute> categoryAttributes);

    /**
     * Convenience method to get a {@link CategoryAttribute} by name
     * 
     * @param name
     * @return
     * @see {@link #getCategoryAttributes()}, {@link #getMappedCategoryAttributes()}
     * @deprecated This will be removed in 3.1.0
     */
    @Deprecated
    public CategoryAttribute getCategoryAttributeByName(String name);

    /**
     * Convenience method to return the {@link CategoryAttribute}s for the {@link Category} in an easily-consumable
     * form
     * 
     * @return
     * @deprecated This will be removed in 3.1.0
     */
    @Deprecated
    public Map<String, CategoryAttribute> getMappedCategoryAttributes();

    /**
     * Used to determine availability for all of the products/skus in this category
     * @return the {@link InventoryType} for this category
     */
    public InventoryType getInventoryType();

    /**
     * Sets the type of inventory for this category
     * @param inventoryType the {@link InventoryType} for this category
     */
    public void setInventoryType(InventoryType inventoryType);
    
    /**
     * Returns the default fulfillment type for skus in this category. May be null.
     * @return
     */
    public FulfillmentType getFulfillmentType();
    
    /**
     * Sets the default fulfillment type for skus in this category. May return null.
     * @param fulfillmentType
     */
    public void setFulfillmentType(FulfillmentType fulfillmentType);

    /**
     * Checks for child categories.
     *
     * @return true, if this category has any children (active or not)
     */
    public boolean hasAllChildCategories();

    /**
     * Gets the child category ids. If child categories has not been previously
     * set, then the list of active only categories will be returned. This method
     * is optimized with Hydrated cache, which means that the algorithm required
     * to harvest active child categories will not need to be rebuilt as long
     * as the parent category (this category) is not evicted from second level cache.
     *
     * @return the list of active child category ids
     */
    @Nonnull
    public List<Long> getChildCategoryIds();

    /**
     * Sets the all child category ids. This should be a list
     * of active only child categories.
     *
     * @param childCategoryIds the list of active child category ids.
     */
    public void setChildCategoryIds(@Nonnull List<Long> childCategoryIds);

    /**
     * Checks for child categories.
     *
     * @return true, if this category contains any active child categories.
     */
    public boolean hasChildCategories();

    public List<CategoryXref> getAllChildCategoryXrefs();

    public List<CategoryXref> getChildCategoryXrefs();

    public void setChildCategoryXrefs(List<CategoryXref> childCategories);

    public void setAllChildCategoryXrefs(List<CategoryXref> childCategories);


    public List<CategoryXref> getAllParentCategoryXrefs();

    public void setAllParentCategoryXrefs(List<CategoryXref> allParentCategories);

    public List<CategoryProductXref> getActiveProductXrefs();

    public List<CategoryProductXref> getAllProductXrefs();

    public void setAllProductXrefs(List<CategoryProductXref> allProducts);

    /**
     * Convenience method to retrieve all of this {@link Category}'s {@link Product}s filtered by
     * active. If you want all of the {@link Product}s (whether inactive or not) consider using
     * {@link #getAllProducts()}.
     *
     * @deprecated Use getActiveProductXrefs() instead.
     * @return the list of active {@link Product}s for this {@link Category}
     * @see {@link Product#isActive()}
     */
    @Deprecated
    public List<Product> getActiveProducts();

    /**
     * Retrieve all the {@code Product} instances associated with this
     * category.
     * <br />
     * <b>Note:</b> this method does not take into account whether or not the {@link Product}s are active or not. If
     * you need this functionality, use {@link #getActiveProducts()}
     * @deprecated Use getAllProductXrefs() instead.
     * @return the list of products associated with this category.
     */
    @Deprecated
    @Nonnull
    public List<Product> getAllProducts();

    /**
     * Set all the {@code Product} instances associated with this
     * category.
     *
     * @deprecated Use setAllProductXrefs() instead.
     * @param allProducts the list of products to associate with this category
     */
    @Deprecated
    public void setAllProducts(@Nonnull List<Product> allProducts);
}
