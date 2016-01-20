package cn.globalph.b2c.web.processor;

import cn.globalph.b2c.catalog.domain.Category;
import cn.globalph.b2c.catalog.domain.CategoryImpl;
import cn.globalph.b2c.catalog.domain.PromotableProduct;
import cn.globalph.b2c.catalog.service.CatalogService;
import cn.globalph.b2c.product.domain.Product;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.search.domain.SkuSearchCriteria;
import cn.globalph.b2c.web.checkout.model.ShippingInfoForm;
import cn.globalph.common.web.dialect.AbstractModelVariableModifierProcessor;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.standard.expression.Expression;
import org.thymeleaf.standard.expression.StandardExpressions;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

/**
 * A Thymeleaf processor that will find category's skus. A category name must be specified.
 *  Takes in the following parameters
 * <ul>
 *    <li>categoryId - categoryId to find skus.</li>
 *    <li>quantity - quantity for fetch.</li>
 *    <li>skusResultVar - if specified, adds the products to the model keyed by this var.   Otherwise, uses "products" as the model identifier.
 * </ul>
 * 
 * The output from this operation returns a list of sku. 
 */
public class CategorySkuProcessor extends AbstractModelVariableModifierProcessor {
    
	@Resource(name = "blCatalogService")
    protected CatalogService catalogService;

    /**
     * Sets the name of this processor to be used in Thymeleaf template
     */
    public CategorySkuProcessor() {
        super("category_skus");
    }
    
    @Override
    public int getPrecedence() {
        return 11000;
    }

    @Override
    /**
     * Controller method for the processor that readies the service call and adds the results to the model.
     */
    protected void modifyModelAttributes(Arguments arguments, Element element) {
    	
    	String categoryId = element.getAttributeValue("categoryId"); 
        String quantity = element.getAttributeValue("quantity"); 
        
        Expression expression = (Expression) StandardExpressions.getExpressionParser(arguments.getConfiguration())
                .parseExpression(arguments.getConfiguration(), arguments, categoryId);
        Long catId  = (Long) expression.execute(arguments.getConfiguration(), arguments);
        
        Category category = new CategoryImpl();
        category.setId( catId );
        
        SkuSearchCriteria searchCriteria = new SkuSearchCriteria();
        searchCriteria.setPageSize( Integer.valueOf(quantity) );
        
        List<Sku> relatedSkus = catalogService.findActiveSkusByCategory(category, searchCriteria);;
        
        addToModel(arguments, getRelatedSkusResultVar(element), relatedSkus);
    }

    protected List<Product> buildProductList(List<? extends PromotableProduct> relatedProducts) {
        List<Product> productList = new ArrayList<Product>();
        if (relatedProducts != null) {
            for (PromotableProduct promProduct : relatedProducts) {
                productList.add(promProduct.getRelatedProduct());
            }
        }
        return productList;
    }
    
    protected List<Product> convertRelatedProductsToProducts(List<? extends PromotableProduct> relatedProducts) {
        List<Product> products = new ArrayList<Product>();
        if (relatedProducts != null) {
            for (PromotableProduct product : relatedProducts) {
                products.add(product.getRelatedProduct());
            }
        }
        return products;        
    }
    
    private String getRelatedSkusResultVar(Element element) {
        String resultVar = element.getAttributeValue("relatedSkusResultVar");       
        if (resultVar == null) {
            resultVar = "relatedSkus";
        }
        return resultVar;
    }
}
