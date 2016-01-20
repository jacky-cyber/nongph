package cn.globalph.b2c.product.dao;

import cn.globalph.b2c.product.domain.ProductOption;
import cn.globalph.b2c.product.domain.ProductOptionValue;

import java.util.List;

/**
 * 
 * @author Phillip Verheyden
 *
 */
public interface ProductOptionDao {
    
    public List<ProductOption> readAllProductOptions();
    
    public ProductOption readProductOptionById(Long id);
    
    public ProductOption saveProductOption(ProductOption option);
    
    public ProductOptionValue readProductOptionValueById(Long id);
    
}
