package cn.globalph.b2c.search.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Lightweight bean representation of
 * <p>
 * (1) All the immediate parent categories for a given product
 * (2) All the immediate parent categories for a give category and
 * (3) All the child products for a given category
 * </p>
 *
 */
public class CatalogStructure implements Serializable {

    Map<Long, Set<Long>> parentCategoriesByProduct = new HashMap<Long, Set<Long>>();
    Map<Long, Set<Long>> parentCategoriesByCategory = new HashMap<Long, Set<Long>>();
    Map<Long, List<Long>> productsByCategory = new HashMap<Long, List<Long>>();
    Map<String, BigDecimal> displayOrdersByCategoryProduct = new HashMap<String, BigDecimal>();

    public Map<Long, Set<Long>> getParentCategoriesByProduct() {
        return parentCategoriesByProduct;
    }

    public void setParentCategoriesByProduct(Map<Long, Set<Long>> parentCategoriesByProduct) {
        this.parentCategoriesByProduct = parentCategoriesByProduct;
    }

    public Map<Long, Set<Long>> getParentCategoriesByCategory() {
        return parentCategoriesByCategory;
    }

    public void setParentCategoriesByCategory(Map<Long, Set<Long>> parentCategoriesByCategory) {
        this.parentCategoriesByCategory = parentCategoriesByCategory;
    }

    public Map<Long, List<Long>> getProductsByCategory() {
        return productsByCategory;
    }

    public void setProductsByCategory(Map<Long, List<Long>> productsByCategory) {
        this.productsByCategory = productsByCategory;
    }

    public Map<String, BigDecimal> getDisplayOrdersByCategoryProduct() {
        return displayOrdersByCategoryProduct;
    }

    public void setDisplayOrdersByCategoryProduct(Map<String, BigDecimal> displayOrdersByCategoryProduct) {
        this.displayOrdersByCategoryProduct = displayOrdersByCategoryProduct;
    }

}
