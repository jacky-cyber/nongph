package cn.globalph.b2c.product.domain;

import cn.globalph.b2c.catalog.domain.Category;
import cn.globalph.b2c.catalog.domain.CategoryProductXref;
import cn.globalph.b2c.catalog.domain.RelatedProduct;
import cn.globalph.common.media.domain.Media;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Implementations of this interface are used to hold data for a Product.  A product is a general description
 * of an item that can be sold (for example: a hat).  Products are not sold or added to a cart.  {@link Sku}s
 * which are specific items (for example: a XL Blue Hat) are sold or added to a cart.
 * <br>
 * <br>
 * You should implement this class if you want to make significant changes to how the
 * Product is persisted.  If you just want to add additional fields then you should extend {@link ProductImpl}.
 *
 * @author btaylor
 * @see {@link ProductImpl},{@link Sku}, {@link Category}
 */
public interface Product extends Serializable {

    /**
     * The id of the Product.
     *
     * @return the id of the Product
     */
    public Long getId();

    /**
     * Sets the id of the Product.
     *
     * @param id - the id of the product
     */
    public void setId(Long id);

    /**
     * Returns the name of the product that is used for display purposes.
     * <br />
     * <br />
     * <b>Note:</b> this is a convenience method that merely serves as
     * a pass-through to the same method via {@link getDefaultSku()}
     * 
     * @return the name of the product
     */
    public String getName();

    /**
     * Sets the name of the product that is used for display purposes.
     * <br />
     * <br />
     * <b>Note:</b> this is a convenience method that merely serves as
     * a pass-through to the same method via {@link getDefaultSku()}
     * 
     * @param name - the name of the Product
     */
    public void setName(String name);

    /**
     * Returns a boolean that indicates if the product is currently active.
     * <br />
     * <br />
     * <b>Note:</b> this is a convenience method that merely serves as
     * a pass-through to the same method via {@link getDefaultSku()}
     * 
     * @return a boolean indicates if the product is active.
     */
    public boolean isActive();
    
    /**
     * Returns a list of {@link Sku}s filtered by whether the Skus are active or not.
     * This list does not contain the {@link #getDefaultSku()} and filters by {@link Sku#isActive()}.
     *
     * @return a list of active Skus from {@link #getAdditionalSkus()} for this Product
     */
    public List<Sku> getActiveSkus();

    Provider getProvider();

    void setProvider(Provider provider);

    /**
     * Returns all the {@link Sku}s that are associated with this Product (including {@link #getDefaultSku()})
     * regardless of whether or not the {@link Sku}s are active or not
     * <br />
     * <br />
     * Note: in the event that the default Sku was added to the list of {@link #getAdditionalSkus()}, it is filtered out
     * so that only a single instance of {@link #getDefaultSku()} is contained in the resulting list
     * 
     * @return all the Skus associated to this Product
     */
    public List<Sku> getAllSkus();
    
    /**
     *设置所有SKU 
     * @param allSkus
     */
    public void setAllSkus(List<Sku> allSkus);

    /**
     * Convenience method for returning all of the media associated with this Product by adding
     * all the media in {@link #getDefaultSku()} as well as all the media in the Skus represented by
     * {@link #getAdditionalSkus()}
     * 
     * @return all of the Media for all of the Skus for this Product
     */
    public Map<String, Media> getAllSkuMedia();
     
    /**
     * Returns the model number of the product
     * @return the model number
     */
    public String getModel();

    /**
     * Sets the model number of the product
     * @param model
     */
    public void setModel(String model);

    /**
     * 获得产地
     * @return the manufacture name
     */
    public String getManufacturer();

    /**
     * 设置参地
     * @param manufacturer
     */
    public void setManufacturer(String manufacturer);
    
    /**
     * 获得品种
     * @return
     */
    public String getBreed();
    
    /**
     * 设置品种
     * @param breed
     */
    public void setBreed(String breed);
    
    /**
     * 获得等级
     * @return
     */
    public String getGrade();
    
    /**
     * 设置等级
     */
    public void setGrade(String grade);
    
    /**
     * Returns a List of this product's related Cross Sales
     * @return
     */
    public List<RelatedProduct> getCrossSaleProducts();

    /**
     * Sets the related Cross Sales
     * @param crossSaleProducts
     */
    public void setCrossSaleProducts(List<RelatedProduct> crossSaleProducts);

    /**
     * Returns a List of this product's related Up Sales
     * @return
     */
    public List<RelatedProduct> getUpSaleProducts();

    /**
     * Sets the related Up Sales
     * @param upSaleProducts
     */
    public void setUpSaleProducts(List<RelatedProduct> upSaleProducts);

    /**
     * Returns whether or not the product is featured
     * @return isFeaturedProduct as Boolean
     */
    public boolean isFeaturedProduct();

    /**
     * Sets whether or not the product is featured
     * @param isFeaturedProduct
     */
    public void setFeaturedProduct(boolean isFeaturedProduct);

    /**
     * Generic key-value pair of attributes to associate to this Product for maximum
     * extensibility.
     *
     * @return the attributes for this Product
     */
    public Map<String, ProductAttribute> getProductAttributes();

    /**
     * Sets a generic list of key-value pairs for Product
     * @param productAttributes
     */
    public void setProductAttributes(Map<String, ProductAttribute> productAttributes);

    /**
     * Gets the promotional message for this Product. For instance, this could be a limited-time
     * Product
     * 
     * @return the Product's promotional message
     */
    public String getPromoMessage();

    /**
     * Sets the promotional message for this Product
     * 
     * @param promoMessage
     */
    public void setPromoMessage(String promoMessage);

    /**
     * The available {@link ProductOption}s for this Product.  For instance, if this
     * Product is a T-Shirt, you might be able to specify a size and color. This would
     * be modeled by 2 {@link ProductOption}s, each that could have multiple {@link ProductOptionValue}s 
     * (which could be "small" "medium" "large", "blue", "yellow", "green").  For specific pricing or
     * inventory needs on a per-value basis, multiple Skus can be associated to this Product based
     * off of the {@link ProductOptionValue}s
     *
     * @deprecated use getProductOptionXrefs instead
     * @return the {@link ProductOption}s for this Product
     * @see Product#getAdditionalSkus(), {@link ProductOption}, {@link ProductOptionValue}
     */
    @Deprecated
    public List<ProductOption> getProductOptions();

    public List<ProductOptionXref> getProductOptionXrefs();

    public void setProductOptionXrefs(List<ProductOptionXref> productOptions);

    /**
     * A product can have a designated URL.   When set, the ProductHandlerMapping will check for this
     * URL and forward this user to the {@link #getDisplayTemplate()}. 
     * 
     * Alternatively, most sites will rely on the {@link Product#getGeneratedUrl()} to define the
     * url for a product page. 
     * 
     * @see cn.globalph.b2c.web.catalog.ProductHandlerMapping
     * @return
     */
    public String getUrl();

    /**
     * Sets the URL that a customer could type in to reach this product.
     * 
     * @param url
     */
    public void setUrl(String url);
    
    /**
     * Sets a url-fragment.  By default, the system will attempt to create a unique url-fragment for 
     * this product by taking the {@link Product.getName()} and removing special characters and replacing
     * dashes with spaces.
     */ 
    public String getUrlKey();

    /**
     * Sets a url-fragment to be used with this product.  By default, the system will attempt to create a 
     * unique url-fragment for this product by taking the {@link Product.getName()} and removing special characters and replacing
     * dashes with spaces.
     */
    public void setUrlKey(String url);

    /**
     * Returns the name of a display template that is used to render this product.   Most implementations have a default
     * template for all products.    This allows for the user to define a specific template to be used by this product.
     * 
     * @return
     */
    public String getDisplayTemplate();

    /**
     * Sets the name of a display template that is used to render this product.   Most implementations have a default
     * template for all products.    This allows for the user to define a specific template to be used by this product.
     * @param displayTemplate
     */
    public void setDisplayTemplate(String displayTemplate);
    
    /**
     * Generates a URL that can be used to access the product.  
     * Builds the url by combining the url of the default category with the getUrlKey() of this product.
     */
    public String getGeneratedUrl();
    
    /** 
     * Returns a list of the cross sale products for this product as well
     * all cross sale products in all parent categories of this product.
     * 
     * @return the cumulative cross sale products
     */
    public List<RelatedProduct> getCumulativeCrossSaleProducts();
    
    /** 
     * Returns a list of the upsale products for this product as well as
     * all upsale products in all parent categories of this product.
     * 
     * @return the cumulative upsale products
     */
    public List<RelatedProduct> getCumulativeUpSaleProducts();

    /**
     * Removes any currently stored dynamic pricing
     */
    public void clearDynamicPrices();

    public List<CategoryProductXref> getAllParentCategoryXrefs();

    public void setAllParentCategoryXrefs(List<CategoryProductXref> allParentCategories);

    /**
     * Returns all parent {@link Category}(s) this product is associated with.
     *
     * @deprecated Use getAllParentCategoryXrefs() instead.
     * @return the all parent categories for this product
     */
    @Deprecated
    public List<Category> getAllParentCategories();

    /**
     * Sets all parent {@link Category}s this product is associated with.
     *
     * @deprecated Use setAllParentCategoryXrefs() instead.
     * @param allParentCategories - a List of all parent {@link Category}(s) to associate this product with
     */
    @Deprecated
    public void setAllParentCategories(List<Category> allParentCategories);
}
