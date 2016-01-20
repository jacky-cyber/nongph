package cn.globalph.openadmin.server.service.persistence.validation;

import cn.globalph.common.money.Money;
import cn.globalph.openadmin.server.service.persistence.module.FieldNotAvailableException;
import cn.globalph.openadmin.server.service.persistence.module.provider.request.PopulateValueRequest;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Collection;
import java.util.Map;


/**
 * Validates that values are actually of their required types before trying to populate it. Integers should be integers,
 * dates should parse correctly, etc.
 *
 * @author Phillip Verheyden (phillipuniverse)
 */
@Component("blBasicFieldTypeValidator")
public class BasicFieldTypeValidator implements PopulateValueRequestValidator {

    @Override
    public PropertyValidationResult validate(PopulateValueRequest populateValueRequest, Serializable instance) {
        switch(populateValueRequest.getMetadata().getFieldType()) {
            case INTEGER:
                try {
                    if (int.class.isAssignableFrom(populateValueRequest.getReturnType()) || Integer.class.isAssignableFrom(populateValueRequest.getReturnType())) {
                        Integer.parseInt(populateValueRequest.getRequestedValue());
                    } else if (byte.class.isAssignableFrom(populateValueRequest.getReturnType()) || Byte.class.isAssignableFrom(populateValueRequest.getReturnType())) {
                        Byte.parseByte(populateValueRequest.getRequestedValue());
                    } else if (short.class.isAssignableFrom(populateValueRequest.getReturnType()) || Short.class.isAssignableFrom(populateValueRequest.getReturnType())) {
                        Short.parseShort(populateValueRequest.getRequestedValue());
                    } else if (long.class.isAssignableFrom(populateValueRequest.getReturnType()) || Long.class.isAssignableFrom(populateValueRequest.getReturnType())) {
                        Long.parseLong(populateValueRequest.getRequestedValue());
                    }
                } catch (NumberFormatException e) {
                    return new PropertyValidationResult(false, "Field must be an valid number");
                }
                break;
            case DECIMAL:
                try {
                    if (BigDecimal.class.isAssignableFrom(populateValueRequest.getReturnType())) {
                        new BigDecimal(populateValueRequest.getRequestedValue());
                    } else {
                        Double.parseDouble(populateValueRequest.getRequestedValue());
                    }
                } catch (NumberFormatException e) {
                    return new PropertyValidationResult(false, "Field must be a valid decimal");
                }
                break;
            case MONEY:
                try {
                    if (BigDecimal.class.isAssignableFrom(populateValueRequest.getReturnType()) || Money.class.isAssignableFrom(populateValueRequest.getReturnType())) {
                        new BigDecimal(populateValueRequest.getRequestedValue());
                    } else if (Double.class.isAssignableFrom(populateValueRequest.getReturnType())) {
                        Double.parseDouble(populateValueRequest.getRequestedValue());
                    }
                } catch (NumberFormatException e) {
                    return new PropertyValidationResult(false, "Field must be a valid number");
                }
                break;
            case DATE:
                try {
                    populateValueRequest.getDataFormatProvider().getSimpleDateFormatter().parse(populateValueRequest.getRequestedValue());
                } catch (ParseException e) {
                    return new PropertyValidationResult(false, "Field must be a date of the format: " + populateValueRequest.getDataFormatProvider().getSimpleDateFormatter().toPattern());
                }
                break;
            case FOREIGN_KEY:
            case ADDITIONAL_FOREIGN_KEY:
                if (Collection.class.isAssignableFrom(populateValueRequest.getReturnType())) {
                    Collection collection;
                    try {
                        collection = (Collection) populateValueRequest.getFieldManager().getFieldValue(instance, populateValueRequest.getProperty().getName());
                    } catch (FieldNotAvailableException e) {
                        return new PropertyValidationResult(false, "External entity cannot be added to the specified collection at " + populateValueRequest.getProperty().getName());
                    } catch (IllegalAccessException e) {
                        return new PropertyValidationResult(false, "External entity cannot be added to the specified collection at " + populateValueRequest.getProperty().getName());
                    }
                } else if (Map.class.isAssignableFrom(populateValueRequest.getReturnType())) {
                    return new PropertyValidationResult(false, "External entity cannot be added to a map at " + populateValueRequest.getProperty().getName());
                }
            case ID:
                if (populateValueRequest.getSetId()) {
                    switch (populateValueRequest.getMetadata().getSecondaryType()) {
                        case INTEGER:
                            Long.valueOf(populateValueRequest.getRequestedValue());
                            break;
                        default:
                            //do nothing
                    }
                }
            default:
                return new PropertyValidationResult(true);
        }
        return new PropertyValidationResult(true);
    }

}
