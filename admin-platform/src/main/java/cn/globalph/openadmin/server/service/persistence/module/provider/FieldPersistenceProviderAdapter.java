package cn.globalph.openadmin.server.service.persistence.module.provider;

import cn.globalph.common.web.WebRequestContext;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.Property;
import cn.globalph.openadmin.server.service.persistence.module.criteria.FilterMapping;
import cn.globalph.openadmin.server.service.persistence.module.provider.request.AddFilterPropertiesRequest;
import cn.globalph.openadmin.server.service.persistence.module.provider.request.AddSearchMappingRequest;
import cn.globalph.openadmin.server.service.persistence.module.provider.request.ExtractValueRequest;
import cn.globalph.openadmin.server.service.persistence.module.provider.request.PopulateValueRequest;
import cn.globalph.openadmin.server.service.type.FieldProviderResponse;

import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Jeff Fischer
 */
public class FieldPersistenceProviderAdapter extends AbstractFieldPersistenceProvider {

    @Override
    public FieldProviderResponse addSearchMapping(AddSearchMappingRequest addSearchMappingRequest, List<FilterMapping> filterMappings) {
        return FieldProviderResponse.NOT_HANDLED;
    }

    @Override
    public FieldProviderResponse populateValue(PopulateValueRequest populateValueRequest, Serializable instance) {
        return FieldProviderResponse.NOT_HANDLED;
    }

    @Override
    public FieldProviderResponse extractValue(ExtractValueRequest extractValueRequest, Property property) {
        return FieldProviderResponse.NOT_HANDLED;
    }

    @Override
    public FieldProviderResponse filterProperties(AddFilterPropertiesRequest addFilterPropertiesRequest, Map<String, FieldMetadata> properties) {
        return FieldProviderResponse.NOT_HANDLED;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    protected boolean checkDirtyState(PopulateValueRequest request, Object instance, Object checkValue) throws Exception {
        boolean dirty = !(instance == null && checkValue == null) && (instance == null || checkValue == null);
        if (!dirty) {
            Object value = request.getFieldManager().getFieldValue(instance, request.getProperty().getName());
            if (checkValue instanceof String) {
                checkValue = ((String) checkValue).trim();
            }
            if (value instanceof String) {
                value = ((String) value).trim();
            }
            if (value instanceof BigDecimal) {
                checkValue = ((BigDecimal) checkValue).setScale(((BigDecimal) value).scale(), RoundingMode.HALF_UP);
            }
            dirty = value == null || !value.equals(checkValue);
        }
        return dirty;
    }

    protected void setNonDisplayableValues(PopulateValueRequest request) {
        WebRequestContext context = WebRequestContext.getWebRequestContext();
        MessageSource messages = context.getMessageSource();
        String label = "(" + messages.getMessage("Workflow_not_displayable", null, "Not Displayable", Locale.CHINA) + ")";
        request.getProperty().setDisplayValue(label);
        request.getProperty().setOriginalDisplayValue(label);
    }
}
