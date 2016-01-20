package cn.globalph.b2c.catalog.service;

import cn.globalph.b2c.catalog.dao.CategoryDao;
import cn.globalph.b2c.catalog.domain.Category;
import cn.globalph.b2c.product.dao.ProductDao;
import cn.globalph.b2c.product.dao.ProductOptionDao;
import cn.globalph.b2c.product.dao.SkuDao;
import cn.globalph.b2c.product.domain.Product;
import cn.globalph.b2c.product.domain.ProductOption;
import cn.globalph.b2c.product.domain.ProductOptionValue;
import cn.globalph.b2c.product.domain.ProductType;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.product.domain.SkuFee;
import cn.globalph.b2c.search.domain.SkuSearchCriteria;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.annotation.Resource;

@Service("blCatalogService")
public class CatalogServiceImpl implements CatalogService {

    @Resource(name="blCategoryDao")
    protected CategoryDao categoryDao;

    @Resource(name="blProductDao")
    protected ProductDao productDao;

    @Resource(name="blSkuDao")
    protected SkuDao skuDao;
    
    @Resource(name="blProductOptionDao")
    protected ProductOptionDao productOptionDao;

    @Resource(name = "blCatalogServiceExtensionManager")
    protected CatalogServiceExtensionManager extensionManager;

    @Override
    public Product findProductById(Long productId) {
        return productDao.readProductById(productId);
    }

    @Override
    public List<Product> findProductsByName(String searchName) {
        return productDao.readProductsByName(searchName);
    }

    @Override
    public List<Product> findProductsByName(String searchName, int limit, int offset) {
        return productDao.readProductsByName(searchName, limit, offset);
    }

    @Override
    public List<Product> findActiveProductsByCategory(Category category) {
        return productDao.readActiveProductsByCategory(category.getId());
    }

    @Override
    public List<Sku> findActiveSkusByCategory(Category category, SkuSearchCriteria searchCriteria) {
        return productDao.readFilteredActiveSkusByCategory(category.getId(), searchCriteria);
    }

    @Override
    public List<Sku> findActiveSkusByQuery(String query, SkuSearchCriteria searchCriteria) {
        return productDao.readFilteredActiveSkusByQuery(query, searchCriteria);
    }

    @Override
    public List<Product> findActiveProductsByCategory(Category category, int limit, int offset) {
        return productDao.readActiveProductsByCategory(category.getId(), limit, offset);
    }

    @Override
    @Transactional("blTransactionManager")
    public Product saveProduct(Product product) {
        return productDao.save(product);
    }

    @Override
    public Category findCategoryById(Long categoryId) {
        return categoryDao.readCategoryById(categoryId);
    }

    @Override
    public List<Category> findCategoriesByName(String categoryName) {
        return categoryDao.readCategoriesByName(categoryName);
    }

    @Override
    public List<Category> findCategoriesByName(String categoryName, int limit, int offset) {
        return categoryDao.readCategoriesByName(categoryName, limit, offset);
    }

    @Override
    @Transactional("blTransactionManager")
    public Category saveCategory(Category category) {
        return categoryDao.save(category);
    }
    
    @Override
    @Transactional("blTransactionManager")
    public void removeCategory(Category category){
        categoryDao.delete(category);
    }

    @Override
    @Transactional("blTransactionManager")
    public void removeProduct(Product product) {
        productDao.delete(product);
    }

    @Override
    public List<Category> findAllCategories() {
        return categoryDao.readAllCategories();
    }

    @Override
    public List<Category> findAllCategories(int limit, int offset) {
        return categoryDao.readAllCategories(limit, offset);
    }
    
    @Override
    public List<Category> findAllParentCategories() {
        return categoryDao.readAllParentCategories();
    }

    @Override
    public List<Category> findAllSubCategories(Category category) {
        return categoryDao.readAllSubCategories(category);
    }

    @Override
    public List<Category> findAllSubCategories(Category category, int limit, int offset) {
        return categoryDao.readAllSubCategories(category, limit, offset);
    }

    @Override
    public List<Category> findActiveSubCategoriesByCategory(Category category) {
        return categoryDao.readActiveSubCategoriesByCategory(category);
    }

    @Override
    public List<Category> findActiveSubCategoriesByCategory(Category category, int limit, int offset) {
        return categoryDao.readActiveSubCategoriesByCategory(category, limit, offset);
    }

    @Override
    public List<Product> findAllProducts() {
        return categoryDao.readAllProducts();
    }

    @Override
    public List<Product> findAllProducts(int limit, int offset) {
        return categoryDao.readAllProducts(limit, offset);
    }

    @Override
    public List<Sku> findAllSkus() {
        return skuDao.readAllSkus();
    }

    @Override
    public Sku findSkuById(Long skuId) {
        return skuDao.readSkuById(skuId);
    }

    @Override
    @Transactional("blTransactionManager")
    public Sku saveSku(Sku sku) {
        return skuDao.save(sku);
    }
    
    @Override
    @Transactional("blTransactionManager")
    public SkuFee saveSkuFee(SkuFee fee) {
        return skuDao.saveSkuFee(fee);
    }
    
    @Override
    public List<Sku> findSkusByIds(List<Long> ids) {
        return skuDao.readSkusById(ids);
    }

    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    public void setSkuDao(SkuDao skuDao) {
        this.skuDao = skuDao;
    }

    @Override
    public List<Product> findProductsForCategory(Category category) {
        return productDao.readProductsByCategory(category.getId());
    }

    @Override
    public List<Product> findProductsForCategory(Category category, int limit, int offset) {
        return productDao.readProductsByCategory(category.getId(), limit, offset);
    }

    public void setCategoryDao(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }
    
    @Override
    public Category createCategory() {
        return categoryDao.create();
    }
    
    @Override
    public Sku createSku() {
        return skuDao.create();
    }
    
    @Override
    public Product createProduct(ProductType productType) {
        return productDao.create(productType);
    }

    @Override
    public List<ProductOption> readAllProductOptions() {
        return productOptionDao.readAllProductOptions();
    }
    
    @Override
    @Transactional("blTransactionManager")
    public ProductOption saveProductOption(ProductOption option) {
        return productOptionDao.saveProductOption(option);
    }
    
    @Override
    public ProductOption findProductOptionById(Long productOptionId) {
        return productOptionDao.readProductOptionById(productOptionId);
    }

    @Override
    public ProductOptionValue findProductOptionValueById(Long productOptionValueId) {
        return productOptionDao.readProductOptionValueById(productOptionValueId);
    }

    @Override
    public Category findCategoryByURI(String uri) {
        if (extensionManager != null) {
            ExtensionResultHolder<Category> holder = new ExtensionResultHolder<Category>();
            ExtensionResultStatusType result = extensionManager.getHandlerProxy().findCategoryByURI(uri, holder);
            if (ExtensionResultStatusType.HANDLED.equals(result)) {
                return holder.getResult();
            	}
           }
        return categoryDao.findCategoryByURI(uri);
     }

    @Override
    public Sku findSkuByURI(String uri, Long skuId) {
        if (extensionManager != null) {
            ExtensionResultHolder<Sku> holder = new ExtensionResultHolder<Sku>();
            ExtensionResultStatusType result = extensionManager.getHandlerProxy().findProductByURI(uri, holder);
            if (ExtensionResultStatusType.HANDLED.equals(result)) {
                return holder.getResult();
            }
        }
        return skuDao.findSkuByProductURIAndId(uri, skuId);
    }
}
