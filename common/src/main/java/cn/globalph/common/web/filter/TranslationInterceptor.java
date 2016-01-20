package cn.globalph.common.web.filter;

import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import javax.annotation.Resource;

/**
 * Interceptor for use with portlets that calls the {@link TranslationRequestProcessor}.
 * 
 * @author bpolster
 */
public class TranslationInterceptor implements WebRequestInterceptor {
    
    @Resource(name = "blTranslationRequestProcessor")
    protected TranslationRequestProcessor translationRequestProcessor;

    @Override
    public void preHandle(WebRequest request) throws Exception {
        translationRequestProcessor.process(request);
    }

    @Override
    public void postHandle(WebRequest request, ModelMap model) throws Exception {
        translationRequestProcessor.postProcess(request);
    }

    @Override
    public void afterCompletion(WebRequest request, Exception ex) throws Exception {
        // unimplemented
    }
}
