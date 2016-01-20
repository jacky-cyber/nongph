package cn.globalph.b2c.order.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.b2c.order.service.exception.ProductOptionValidationException;
import cn.globalph.b2c.product.domain.ProductOption;
import cn.globalph.b2c.product.domain.ProductOptionValidationType;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service("blProductOptionValidationService")
public class ProductOptionValidationServiceImpl implements ProductOptionValidationService  {

    private static final Log LOG = LogFactory.getLog(ProductOptionValidationServiceImpl.class);


    /* (non-Javadoc)
     * @see cn.globalph.b2c.order.service.ProductOptionValidationService#validate(cn.globalph.b2c.product.domain.ProductOption, java.lang.String)
     */
    @Override
    public Boolean validate(ProductOption productOption, String value) {
        if (productOption.getProductOptionValidationType() == ProductOptionValidationType.REGEX) {
            if (!validateRegex(productOption.getValidationString(), value))
            {
                LOG.error(productOption.getErrorMessage() + ". Value [" + value + "] does not match regex string [" + productOption.getValidationString() + "]");
                String exceptionMessage = productOption.getAttributeName() + " " + productOption.getErrorMessage() + ". Value [" + value + "] does not match regex string [" + productOption.getValidationString() + "]";
                throw new ProductOptionValidationException(exceptionMessage, productOption.getErrorCode(), productOption.getAttributeName(), value, productOption.getValidationString(), productOption.getErrorMessage());
            }
        }
        return true;
    }
    
    protected Boolean validateRegex(String regex, String value) {
        if (value == null) {
            return false;
        }
        return Pattern.matches(regex, value);
    }
    

}
