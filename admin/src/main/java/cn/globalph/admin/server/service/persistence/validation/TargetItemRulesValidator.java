package cn.globalph.admin.server.service.persistence.validation;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.b2c.offer.service.type.OfferType;
import cn.globalph.openadmin.dto.BasicFieldMetadata;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.server.service.persistence.validation.PropertyValidationResult;
import cn.globalph.openadmin.server.service.persistence.validation.PropertyValidator;
import cn.globalph.openadmin.server.service.persistence.validation.RequiredPropertyValidator;

import org.springframework.stereotype.Component;


/**
 * Validator that ensures that an offer of type {@link OfferType#ORDER_ITEM} has at least one rule for the target criteria
 *
 * @author Phillip Verheyden (phillipuniverse)
 */
@Component("blTargetItemRulesValidator")
public class TargetItemRulesValidator implements PropertyValidator {

    @Override
    public PropertyValidationResult validate(Entity entity, Serializable instance, Map<String, FieldMetadata> entityFieldMetadata, Map<String, String> validationConfiguration, BasicFieldMetadata propertyMetadata, String propertyName, String value) {
        Offer offer = (Offer)instance;
        if (OfferType.ORDER_ITEM.equals(offer.getType())) {
            return new PropertyValidationResult(CollectionUtils.isNotEmpty(offer.getTargetItemCriteria()), RequiredPropertyValidator.ERROR_MESSAGE);
        } else {
            return new PropertyValidationResult(true);
        }
    }
}
