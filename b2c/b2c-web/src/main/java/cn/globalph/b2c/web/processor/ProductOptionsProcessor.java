package cn.globalph.b2c.web.processor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.b2c.catalog.service.CatalogService;
import cn.globalph.b2c.product.domain.Product;
import cn.globalph.b2c.product.domain.ProductOption;
import cn.globalph.b2c.product.domain.ProductOptionValue;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.common.currency.util.BroadleafCurrencyUtils;
import cn.globalph.common.money.Money;
import cn.globalph.common.web.dialect.AbstractModelVariableModifierProcessor;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.util.LRUMap;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.standard.expression.Expression;
import org.thymeleaf.standard.expression.StandardExpressions;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * This processor will add the following information to the model, available for consumption by a template:
 * -pricing for a sku based on the product option values selected
 * -the complete set of product options and values for a given product
 */
public class ProductOptionsProcessor extends AbstractModelVariableModifierProcessor {
    
    @Resource(name = "blCatalogService")
    protected CatalogService catalogService;

    private static final Log LOG = LogFactory.getLog(ProductOptionsProcessor.class);
    protected static final Map<Object, String> JSON_CACHE = Collections.synchronizedMap(new LRUMap<Object, String>(100, 500));

    public ProductOptionsProcessor() {
        super("product_options");
    }

    @Override
    public int getPrecedence() {
        return 10000;
    }

    @Override
    protected void modifyModelAttributes(Arguments arguments, Element element) {
        Expression expression = (Expression) StandardExpressions.getExpressionParser(arguments.getConfiguration())
                .parseExpression(arguments.getConfiguration(), arguments, element.getAttributeValue("productId"));
        Long productId = (Long) expression.execute(arguments.getConfiguration(), arguments);
        Product product = catalogService.findProductById(productId);
        if (product != null) {
            addAllProductOptionsToModel(arguments, product);
            addProductOptionPricingToModel(arguments, product);
        }
    }
    
    private void addProductOptionPricingToModel(Arguments arguments, Product product) {
        List<Sku> skus = product.getAllSkus();
        List<ProductOptionPricingDTO> skuPricing = new ArrayList<ProductOptionPricingDTO>();
        for (Sku sku : skus) {
            List<Long> productOptionValueIds = new ArrayList<Long>();
            List<ProductOptionValue> productOptionValues = sku.getProductOptionValues();
            for (ProductOptionValue productOptionValue : productOptionValues) {
                productOptionValueIds.add(productOptionValue.getId());
            }
            
            Long[] values = new Long[productOptionValueIds.size()];
            productOptionValueIds.toArray(values);
            
            ProductOptionPricingDTO dto = new ProductOptionPricingDTO();
            dto.setSkuId(sku.getId());
            dto.setSelectedOptions(values);
            skuPricing.add(dto);
        }
        writeJSONToModel(arguments, "skuPricing", skuPricing);
    }
    
    private void addAllProductOptionsToModel(Arguments arguments, Product product) {
        List<ProductOption> productOptions = product.getProductOptions();
        List<ProductOptionDTO> dtos = new ArrayList<ProductOptionDTO>();
        for (ProductOption option : productOptions) {
            ProductOptionDTO dto = new ProductOptionDTO();
            dto.setId(option.getId());
            dto.setType(option.getType().getType());
            Map<Long, String> values = new HashMap<Long, String>();
            for (ProductOptionValue value : option.getAllowedValues()) {
                values.put(value.getId(), value.getAttributeValue());
            }
            dto.setValues(values);
            dtos.add(dto);
        }
        writeJSONToModel(arguments, "allProductOptions", dtos);
    }
    
    private void writeJSONToModel(Arguments arguments, String modelKey, Object o) {
        try {
            if (!JSON_CACHE.containsKey(o)) {
                ObjectMapper mapper = new ObjectMapper();
                Writer strWriter = new StringWriter();
                mapper.writeValue(strWriter, o);
                JSON_CACHE.put(o, strWriter.toString());
            }
            addToModel(arguments, modelKey, JSON_CACHE.get(o));
        } catch (Exception ex) {
            LOG.error("There was a problem writing the product option map to JSON", ex);
        }
    }

    private String formatPrice(Money price){
        if (price == null){
            return null;
           }
        return BroadleafCurrencyUtils.getNumberFormatFromCache().format( price.getAmount() );
    }
    
    private class ProductOptionDTO {
        private Long id;
        private String type;
        private Map<Long, String> values;
        private String selectedValue;
        @SuppressWarnings("unused")
        public Long getId() {
            return id;
        }
        public void setId(Long id) {
            this.id = id;
        }
        @SuppressWarnings("unused")
        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }
        @SuppressWarnings("unused")
        public Map<Long, String> getValues() {
            return values;
        }
        public void setValues(Map<Long, String> values) {
            this.values = values;
        }
        @SuppressWarnings("unused")
        public String getSelectedValue() {
            return selectedValue;
        }
        @SuppressWarnings("unused")
        public void setSelectedValue(String selectedValue) {
            this.selectedValue = selectedValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ProductOptionDTO)) return false;

            ProductOptionDTO that = (ProductOptionDTO) o;

            if (id != null ? !id.equals(that.id) : that.id != null) return false;
            if (selectedValue != null ? !selectedValue.equals(that.selectedValue) : that.selectedValue != null)
                return false;
            if (type != null ? !type.equals(that.type) : that.type != null) return false;
            if (values != null ? !values.equals(that.values) : that.values != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (type != null ? type.hashCode() : 0);
            result = 31 * result + (values != null ? values.hashCode() : 0);
            result = 31 * result + (selectedValue != null ? selectedValue.hashCode() : 0);
            return result;
        }
    }
    
    private class ProductOptionPricingDTO {
    	private Long skuId;
        private Long[] skuOptions;
        public Long getSkuId(){
        	return this.skuId;
        }
        public void setSkuId(Long skuId){
        	this.skuId = skuId;
        }
        @SuppressWarnings("unused")
        public Long[] getSelectedOptions() {
            return skuOptions;
        }
        public void setSelectedOptions(Long[] skuOptions) {
            this.skuOptions = skuOptions;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ProductOptionPricingDTO)) return false;

            ProductOptionPricingDTO that = (ProductOptionPricingDTO) o;

            if (skuId != null ? !skuId.equals(that.skuId) : that.skuId != null) return false;
            if (!Arrays.equals(skuOptions, that.skuOptions)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = skuOptions != null ? Arrays.hashCode(skuOptions) : 0;
            result = 31 * result + (skuId != null ? skuId.hashCode() : 0);
            return result;
        }
    }

}
