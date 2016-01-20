package cn.globalph.b2c.product.service;

import cn.globalph.b2c.catalog.domain.PromotableProduct;
import cn.globalph.b2c.catalog.domain.RelatedProductDTO;

import java.util.List;

/**
 * Interface for finding related products.   Could be extended to support more complex
 * related product functions.    
 * 
 * @author bpolster
 *
 */
public interface RelatedProductsService {   
    
    /**
     * Uses the data in the passed in DTO to return a list of relatedProducts.
     * 
     * For example, upSale, crossSale, or featured products.
     * 
     * @param relatedProductDTO
     * @return
     */
    public List<? extends PromotableProduct> findRelatedProducts(RelatedProductDTO relatedProductDTO);
}
