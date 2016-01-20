package cn.globalph.b2c.product.domain.wrapper;

import cn.globalph.b2c.catalog.domain.RelatedProduct;
import cn.globalph.b2c.product.domain.Product;
import cn.globalph.b2c.product.domain.ProductAttribute;
import cn.globalph.b2c.product.domain.ProductOption;
import cn.globalph.b2c.product.domain.wrap.ProductAttributeWrap;
import cn.globalph.b2c.product.domain.wrap.ProductOptionWrap;
import cn.globalph.b2c.product.domain.wrap.ProductWrap;
import cn.globalph.b2c.product.domain.wrap.RelatedProductWrap;
import cn.globalph.common.domain.wrapper.APIWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductWrapper implements APIWrapper<Product, ProductWrap> {
	
	private ProductOptionWrapper productOptionWrapper;
	private RelatedProductWrapper relatedProductWrapper;
	private ProductAttributeWrapper productAttributeWrapper;
    @Override
    public ProductWrap wrapDetails(Product model) {
    	ProductWrap wrap = new ProductWrap();
    	wrap.setId( model.getId() );
    	wrap.setName( model.getName() );
    	wrap.setManufacturer( model.getManufacturer() );
    	wrap.setModel( model.getModel() );
    	wrap.setPromoMessage( model.getPromoMessage() );
    	wrap.setActive( model.isActive() );
    	wrap.setUrl( model.getUrl() );
    	wrap.setFeaturedProduct( model.isFeaturedProduct() );
           
        /*
        if (model instanceof ProductBundle) {

            ProductBundle bundle = (ProductBundle) model;
            this.priority = bundle.getPriority();
            this.bundleItemsRetailPrice = bundle.getBundleItemsRetailPrice();
            this.bundleItemsSalePrice = bundle.getBundleItemsSalePrice();

            if (bundle.getSkuBundleItems() != null) {
                this.skuBundleItems = new ArrayList<SkuBundleItemWrapper>();
                List<SkuBundleItem> bundleItems = bundle.getSkuBundleItems();
                for (SkuBundleItem item : bundleItems) {
                    SkuBundleItemWrapper skuBundleItemsWrapper = (SkuBundleItemWrapper) context.getBean(SkuBundleItemWrapper.class.getName());
                    skuBundleItemsWrapper.wrapSummary(item, request);
                    this.skuBundleItems.add(skuBundleItemsWrapper);
                	}
            	}
           } 
        */
        if (model.getProductOptions() != null && !model.getProductOptions().isEmpty()) {
            List<ProductOptionWrap> productOptions = new ArrayList<ProductOptionWrap>();
            for (ProductOption option : model.getProductOptions()) {
            	productOptions.add( productOptionWrapper.wrapSummary(option) );
            }
            wrap.setProductOptions( productOptions );
        }
    
        if (model.getUpSaleProducts() != null && !model.getUpSaleProducts().isEmpty()) {
            List<RelatedProductWrap> upsaleProducts = new ArrayList<RelatedProductWrap>();
            for (RelatedProduct p : model.getUpSaleProducts()) {
                upsaleProducts.add( relatedProductWrapper.wrapSummary(p) );
            }
            wrap.setUpsaleProducts( upsaleProducts );
        }

        if (model.getCrossSaleProducts() != null && !model.getCrossSaleProducts().isEmpty()) {
            List<RelatedProductWrap> crossSaleProducts = new ArrayList<RelatedProductWrap>();
            for (RelatedProduct p : model.getCrossSaleProducts()) {
                crossSaleProducts.add( relatedProductWrapper.wrapSummary(p) );
            }
            wrap.setCrossSaleProducts(crossSaleProducts);
        }

        if (model.getProductAttributes() != null && !model.getProductAttributes().isEmpty()) {
            List<ProductAttributeWrap> productAttributes = new ArrayList<ProductAttributeWrap>();
            if (model.getProductAttributes() != null) {
                for (Map.Entry<String, ProductAttribute> entry : model.getProductAttributes().entrySet()) {
                    productAttributes.add( productAttributeWrapper.wrapSummary( entry.getValue() ) );
                }
            }
            wrap.setProductAttributes( productAttributes );
        }
        return wrap;
    }

    @Override
    public ProductWrap wrapSummary(Product model) {
    	ProductWrap wrap = new ProductWrap();
    	wrap.setId( model.getId() );
    	wrap.setName( model.getName() );
    	wrap.setActive( model.isActive() );
    	wrap.setUrl( model.getUrl() );
    	wrap.setFeaturedProduct( model.isFeaturedProduct() );
           /*
        if (model instanceof ProductBundle) {

            ProductBundle bundle = (ProductBundle) model;
            this.priority = bundle.getPriority();
            this.bundleItemsRetailPrice = bundle.getBundleItemsRetailPrice();
            this.bundleItemsSalePrice = bundle.getBundleItemsSalePrice();
           } 
           */
        if (model.getProductOptions() != null && !model.getProductOptions().isEmpty()) {
            List<ProductOptionWrap> productOptions = new ArrayList<ProductOptionWrap>();
            for (ProductOption option :  model.getProductOptions()) {
                productOptions.add( productOptionWrapper.wrapSummary(option) );
            }
            wrap.setProductOptions( productOptions );
        }
        return wrap;
    }
}
