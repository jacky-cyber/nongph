package cn.globalph.common.web.filter;

import cn.globalph.common.i18n.service.TranslationConsiderationContext;
import cn.globalph.common.i18n.service.TranslationService;
import cn.globalph.common.util.BLCSystemProperty;
import cn.globalph.common.web.AbstractRequestProcessor;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.Resource;

/**
 * This processor is responsible for setting up the translation context.   It is intended to be used
 * by both typical Web applications and called from a ServletFilter (such as "TranslationFilter") or 
 * from a portletFilter (such as "TranslationInterceptor")
 * 
 * @author bpolster
 */
@Component("blTranslationRequestProcessor")
public class TranslationRequestProcessor extends AbstractRequestProcessor {
    
    @Resource(name = "blTranslationService")
    protected TranslationService translationService;
    
    protected boolean getTranslationEnabled() {
        return BLCSystemProperty.resolveBooleanSystemProperty("i18n.translation.enabled");
    }

    @Override
    public void process(WebRequest request) {
        TranslationConsiderationContext.setTranslationConsiderationContext(getTranslationEnabled());
        TranslationConsiderationContext.setTranslationService(translationService);
    }
}
