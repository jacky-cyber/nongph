package cn.globalph.b2c.catalog.service;

import cn.globalph.b2c.catalog.domain.Category;
import cn.globalph.b2c.product.domain.Product;
import cn.globalph.b2c.product.domain.ProductOption;
import cn.globalph.b2c.product.domain.ProductOptionValue;
import cn.globalph.b2c.product.domain.ProductType;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.product.domain.SkuFee;
import cn.globalph.b2c.search.domain.SkuSearchCriteria;

import java.util.List;

public interface CatalogService {

    public Product saveProduct(Product product);

    public Product findProductById(Long productId);

    public List<Product> findProductsByName(String searchName);

    /**
     * Find a subset of {@code Product} instances whose name starts with
     * or is equal to the passed in search parameter.  Res
     * @param searchName
     * @param limit the maximum number of results
     * @param offset the starting point in the record set
     * @return the list of product instances that fit the search criteria
     */
    public List<Product> findProductsByName(String searchName, int limit, int offset);

    public List<Product> findActiveProductsByCategory(Category category);

    /**
     * Given a category and a ProudctSearchCriteria, returns the appropriate matching products
     * 
     * @param category
     * @param searchCriteria
     * @return the matching products
     */
    public List<Sku> findActiveSkusByCategory(Category category, SkuSearchCriteria searchCriteria);

    /**
     * Given a search query and a ProductSearchCriteria, returns the appropriate matching products
     * 
     * @param query
     * @param searchCriteria
     * @return the matching products
     */
    public List<Sku> findActiveSkusByQuery(String query, SkuSearchCriteria searchCriteria);

    /**
     * Same as {@link #findActiveProductsByCategory(Category)} but allowing for pagination.
     * 
     * @param category
     * @param limit
     * @param offset
     * @return
     */
    public List<Product> findActiveProductsByCategory(Category category, int limit, int offset);

    public Category saveCategory(Category category);
    
    public void removeCategory(Category category);

    public void removeProduct(Product product);

    public Category findCategoryById(Long categoryId);

    /**
     * Retrieve a list of {@code Category} instance based on the name
     * property.
     *
     * @param categoryName the category name to search by
     * @return the list of matching Category instances
     */
    public List<Category> findCategoriesByName(String categoryName);

    /**
     * Retrieve a list of {@code Category} instances based on the search criteria
     *
     * @param categoryName the name of the category to search by
     * @param limit the maximum number of results to return
     * @param offset the starting point of the records to return
     * @return a list of category instances that match the search criteria
     */
    public List<Category> findCategoriesByName(String categoryName, int limit, int offset);

    public List<Category> findAllCategories();

    public List<Category> findAllCategories(int limit, int offset);

    public List<Product> findAllProducts();

    public List<Product> findAllProducts(int limit, int offset);

    public List<Product> findProductsForCategory(Category category);

    public List<Product> findProductsForCategory(Category category, int limit, int offset);

    public Sku saveSku(Sku sku);
    
    public SkuFee saveSkuFee(SkuFee fee);

    public List<Sku> findAllSkus();

    public List<Sku> findSkusByIds(List<Long> ids);

    public Sku findSkuById(Long skuId);

    public Category createCategory();
    
    public Sku createSku();
    
    public Product createProduct(ProductType productType);

    public List<Category> findAllParentCategories();
    
    public List<Category> findAllSubCategories(Category category);

    public List<Category> findAllSubCategories(Category category, int limit, int offset);

    public List<Category> findActiveSubCategoriesByCategory(Category category);

    public List<Category> findActiveSubCategoriesByCategory(Category category, int limit, int offset);
    
    public List<ProductOption> readAllProductOptions();
    
    public ProductOption saveProductOption(ProductOption option);
    
    public ProductOption findProductOptionById(Long productOptionId);
    
    public ProductOptionValue findProductOptionValueById(Long productOptionValueId);
    
    /**
     * Returns a category associated with the passed in URI or null if no Category is
     * mapped to this URI.
     * 
     * @param uri
     * @return
     */
    public Category findCategoryByURI(String uri);
    
    /**
     * Returns a SKU associated with the passed in URI or null if no Sku is
     * mapped to this URI.
     * 
     * @param uri
     * @return
     */    
    public Sku findSkuByURI(String uri, Long skuId);

}
