package cn.globalph.b2c.web.processor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.attr.AbstractAttrProcessor;
import org.thymeleaf.standard.expression.Expression;
import org.thymeleaf.standard.expression.StandardExpressions;

import cn.globalph.b2c.product.domain.ProductOptionValue;

import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;

public class ProductOptionValueProcessor extends AbstractAttrProcessor  {

    private static final Log LOG = LogFactory.getLog(ProductOptionValueProcessor.class);
    
    public ProductOptionValueProcessor() {
        super("product_option_value");
    }
    
    @Override
    protected ProcessorResult processAttribute(Arguments arguments, Element element, String attributeName) {
        
        Expression expression = (Expression) StandardExpressions.getExpressionParser(arguments.getConfiguration())
                .parseExpression(arguments.getConfiguration(), arguments, element.getAttributeValue(attributeName));
        ProductOptionValue productOptionValue = (ProductOptionValue) expression.execute(arguments.getConfiguration(), arguments);

        ProductOptionValueDTO dto = new ProductOptionValueDTO();
        dto.setOptionId(productOptionValue.getProductOption().getId());
        dto.setValueId(productOptionValue.getId());
        dto.setValueName(productOptionValue.getAttributeValue());
        if (productOptionValue.getPriceAdjustment() != null) {
            dto.setPriceAdjustment(productOptionValue.getPriceAdjustment().getAmount());
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            Writer strWriter = new StringWriter();
            mapper.writeValue(strWriter, dto);
            element.setAttribute("data-product-option-value", strWriter.toString());
            element.removeAttribute(attributeName);
            return ProcessorResult.OK;
        } catch (Exception ex) {
            LOG.error("There was a problem writing the product option value to JSON", ex);
        }
        
        return null;
        
    }

    @Override
    public int getPrecedence() {
        return 10000;
    }

    private class ProductOptionValueDTO {
        private Long optionId;
        private Long valueId;
        private String valueName;
        private BigDecimal priceAdjustment;
        @SuppressWarnings("unused")
        public Long getOptionId() {
            return optionId;
        }
        public void setOptionId(Long optionId) {
            this.optionId = optionId;
        }
        @SuppressWarnings("unused")
        public Long getValueId() {
            return valueId;
        }
        public void setValueId(Long valueId) {
            this.valueId = valueId;
        }
        @SuppressWarnings("unused")
        public String getValueName() {
            return valueName;
        }
        public void setValueName(String valueName) {
            this.valueName = valueName;
        }
        @SuppressWarnings("unused")
        public BigDecimal getPriceAdjustment() {
            return priceAdjustment;
        }
        public void setPriceAdjustment(BigDecimal priceAdjustment) {
            this.priceAdjustment = priceAdjustment;
        }
    }

}
