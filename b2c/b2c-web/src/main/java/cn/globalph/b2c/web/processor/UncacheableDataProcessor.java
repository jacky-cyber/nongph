package cn.globalph.b2c.web.processor;

import org.apache.commons.lang3.StringUtils;

import cn.globalph.b2c.inventory.service.InventoryService;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.domain.SkuAccessor;
import cn.globalph.b2c.product.domain.Product;
import cn.globalph.b2c.product.domain.ProductOptionXref;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.web.order.CartState;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.security.service.ExploitProtectionService;
import cn.globalph.common.util.StringUtil;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.web.core.CustomerState;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Macro;
import org.thymeleaf.dom.Node;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.element.AbstractElementProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

/**
 * This processor outputs a SCRIPT tag with JSON data that can be used to update a mostly cached page followed by
 * a call to a javascript function.  The function name is "updateUncacheableData()" by default.
 * 
 * Broadleaf provides this example but most clients with typical customizations will need to create a similar processor
 * to meet their dynamic data caching needs.
 * 
 * The Broadleaf processor works with the sample javacript function in HeatClinic found in heatClinic-UncacheableData.js work 
 * together to update the "In Cart", "Out of Stock", "Welcome {name}", and "Cart Qty" messages.   By doing this, the 
 * category and product pages in HeatClinic can be aggressively cached using the {@link BroadleafCacheProcessor}. 
 * 
 * Example usage on cached pages with dynamic data.   This would generally go after the footer for the page.
 * <pre>
 *  {@code
 *      <blc:uncacheableData />  
 *  }
 * </pre>
 * @author bpolster
 */
public class UncacheableDataProcessor extends AbstractElementProcessor {

    @Resource(name = "blInventoryService")
    protected InventoryService inventoryService;

    @Resource(name = "blExploitProtectionService")
    protected ExploitProtectionService eps;

    private String defaultCallbackFunction = "updateUncacheableData(params)";

    /**
     * Sets the name of this processor to be used in Thymeleaf template
     */
    public UncacheableDataProcessor() {
        super("uncacheabledata");
    }

    @Override
    public int getPrecedence() {
        return 100;
    }


    @Override
    protected ProcessorResult processElement(Arguments arguments, Element element) {
        StringBuffer sb = new StringBuffer();
        sb.append("<SCRIPT>\n");
        sb.append("  var params = \n  ");
        sb.append(buildContentMap(arguments)).append(";\n  ");
        sb.append(getUncacheableDataFunction(arguments, element)).append(";\n");
        sb.append("</SCRIPT>");
                
        // Add contentNode to the document
        Node contentNode = new Macro(sb.toString());
        element.clearChildren();
        element.getParent().insertAfter(element, contentNode);
        element.getParent().removeChild(element);

        // Return OK
        return ProcessorResult.OK;

    }

    protected String buildContentMap(Arguments arguments) {
        Map<String, Object> attrMap = new HashMap<String, Object>();
        addCartData(attrMap);
        addCustomerData(attrMap);
        addProductInventoryData(attrMap, arguments);

        try {
            attrMap.put("csrfToken", eps.getCSRFToken());
            attrMap.put("csrfTokenParameter", eps.getCsrfTokenParameter());
        } catch (ServiceException e) {
            throw new RuntimeException("Could not get a CSRF token for this session", e);
        }
        return StringUtil.getMapAsJson(attrMap)  ;      
    }

    protected void addProductInventoryData(Map<String, Object> attrMap, Arguments arguments) {
        List<Long> outOfStockProducts = new ArrayList<Long>();
        Set<Product> products = (Set<Product>) ((Map<String, Object>) arguments.getExpressionEvaluationRoot()).get("blcAllDisplayedProducts");
        if (products != null) {
            for (Product product : products) {
            	/*
                    Boolean qtyAvailable = inventoryService.isAvailable(product.getDefaultSku(), 1);
                    if (qtyAvailable != null && !qtyAvailable) {
                        outOfStockProducts.add(product.getId());
                    	   }
                 */
            	}
           }
        attrMap.put("outOfStockProducts", outOfStockProducts);
      }

    protected void addCartData(Map<String, Object> attrMap) {
        Order cart = CartState.getCart();
        int cartQty = 0;
        List<Long> cartItemIdsWithOptions = new ArrayList<Long>();
        List<Long> cartItemIdsWithoutOptions = new ArrayList<Long>();

        if (cart != null && cart.getOrderItems() != null) {
            cartQty = cart.getItemCount();

            for (OrderItem item : cart.getOrderItems()) {
                if (item instanceof SkuAccessor) {
                    Sku sku = ((SkuAccessor) item).getSku();
                    if (sku != null && sku.getProduct() != null) {
                        Product product = sku.getProduct();
                        List<ProductOptionXref> optionXrefs = product.getProductOptionXrefs();
                        if (optionXrefs == null || optionXrefs.isEmpty()) {
                            cartItemIdsWithoutOptions.add(product.getId());
                        } else {
                            cartItemIdsWithOptions.add(product.getId());
                        }
                    }
                }
            }
        }

        attrMap.put("cartItemCount", cartQty);
        attrMap.put("cartItemIdsWithOptions", cartItemIdsWithOptions);
        attrMap.put("cartItemIdsWithoutOptions", cartItemIdsWithoutOptions);
    }
    
    protected void addCustomerData(Map<String, Object> attrMap) {
        Customer customer = CustomerState.getCustomer();
        String firstName = "";
        String lastName = "";
        boolean anonymous = false;

        if (customer != null) {
            if (!StringUtils.isEmpty(customer.getName())) {
                firstName = customer.getName();
            }

            if (customer.isAnonymous()) {
                anonymous = true;
            }
        }
        
        attrMap.put("firstName", firstName);
        attrMap.put("lastName", lastName);
        attrMap.put("anonymous", anonymous);
    }
    
    public String getUncacheableDataFunction(Arguments arguments, Element element) {
        if (element.hasAttribute("callback")) {
            return element.getAttributeValue("callback");
        } else {
            return getDefaultCallbackFunction();
        }
    }

    public String getDefaultCallbackFunction() {
        return defaultCallbackFunction;
    }
    
    public void setDefaultCallbackFunction(String defaultCallbackFunction) {
        this.defaultCallbackFunction = defaultCallbackFunction;
    }
}
