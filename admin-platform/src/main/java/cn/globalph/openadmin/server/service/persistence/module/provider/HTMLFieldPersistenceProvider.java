package cn.globalph.openadmin.server.service.persistence.module.provider;

import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.common.web.WebRequestContext;
import cn.globalph.openadmin.dto.Property;
import cn.globalph.openadmin.server.service.persistence.PersistenceOperationException;
import cn.globalph.openadmin.server.service.persistence.module.provider.request.ExtractValueRequest;
import cn.globalph.openadmin.server.service.persistence.module.provider.request.PopulateValueRequest;
import cn.globalph.openadmin.server.service.type.FieldProviderResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * @author felix.wu
 */
@Component("blHTMLFieldPersistenceProvider")
@Scope("prototype")
public class HTMLFieldPersistenceProvider extends FieldPersistenceProviderAdapter {

    @Value("${asset.server.url.prefix.internal}")
    protected String staticAssetUrlPrefix;

    protected boolean canHandlePersistence(PopulateValueRequest populateValueRequest, Serializable instance) {
        return populateValueRequest.getMetadata().getFieldType() == SupportedFieldType.HTML ||
                populateValueRequest.getMetadata().getFieldType() == SupportedFieldType.HTML_BASIC;
    }

    protected boolean canHandleExtraction(ExtractValueRequest extractValueRequest, Property property) {
        return extractValueRequest.getMetadata().getFieldType() == SupportedFieldType.HTML ||
                extractValueRequest.getMetadata().getFieldType() == SupportedFieldType.HTML_BASIC;
    }

    @Override
    public FieldProviderResponse populateValue(PopulateValueRequest populateValueRequest, Serializable instance) throws PersistenceOperationException {
        if (!canHandlePersistence(populateValueRequest, instance)) {
            return FieldProviderResponse.NOT_HANDLED;
        }

        try {
            String requestedValue = populateValueRequest.getRequestedValue();
            String fixedValue = fixAssetPathsForStorage(requestedValue);

            boolean dirty = checkDirtyState(populateValueRequest, instance, fixedValue);
            populateValueRequest.getProperty().setIsDirty(dirty);

            populateValueRequest.getFieldManager().setFieldValue(instance,
                    populateValueRequest.getProperty().getName(), fixedValue);

        } catch (Exception e) {
            throw new PersistenceOperationException(e);
        }
        return FieldProviderResponse.HANDLED_BREAK;
    }

    @Override
    public FieldProviderResponse extractValue(ExtractValueRequest extractValueRequest, Property property) throws PersistenceOperationException {
        if (!canHandleExtraction(extractValueRequest, property)) {
            return FieldProviderResponse.NOT_HANDLED;
        }

        if (extractValueRequest.getRequestedValue() != null) {
            String val = extractValueRequest.getRequestedValue().toString();
            if (val != null) {
                if (val.contains(staticAssetUrlPrefix)) {
                    val = fixAssetPathsForDisplay(val);
                }
            }
            property.setValue(val);
            property.setDisplayValue(extractValueRequest.getDisplayVal());
        }
        return FieldProviderResponse.HANDLED_BREAK;
    }

    /**
     * Stores the image paths at the root (e.g. no Servlet Context).   
     * @param val
     * @return
     */
    public String fixAssetPathsForStorage(String val) {
        if (staticAssetUrlPrefix != null) {
            String tmpPrefix = staticAssetUrlPrefix;
            if (tmpPrefix.startsWith("/")) {
                tmpPrefix = tmpPrefix.substring(1);
            }
            return val.replaceAll("(?<=src=\").*?(?=" + tmpPrefix + ")", "/");
        }
        return val;
    }

    /**
     * 
     * @param val
     * @return
     */
    public String fixAssetPathsForDisplay(String val) {
        String contextPath = "/";
        WebRequestContext brc = WebRequestContext.getWebRequestContext();
        if (brc != null) {                        
            HttpServletRequest request = brc.getRequest();
            if (request != null) {
                contextPath = request.getContextPath();
            }
        }
        
        if (!contextPath.endsWith("/")) {
            contextPath = contextPath + "/";
        }
        
        if (staticAssetUrlPrefix != null) {
            String tmpPrefix = staticAssetUrlPrefix;
            if (tmpPrefix.startsWith("/")) {
                tmpPrefix = tmpPrefix.substring(1);
            }
            return val.replaceAll("(?<=src=\").*?(?=" + tmpPrefix + ")", contextPath);
        }
        
        return val;
    }

}
