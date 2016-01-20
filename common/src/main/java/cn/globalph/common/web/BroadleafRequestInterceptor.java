package cn.globalph.common.web;

import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import javax.annotation.Resource;


/**
 * <p>Interceptor responsible for setting up the WebRequestContext for the life of the request. This interceptor
 * should be the very first one in the list, as other interceptors might also use {@link WebRequestContext}.</p>
 * 
 * <p>Note that in Servlet applications you should be using the {@link WebRequestFilter}.</p>
 * 
 * @see {@link WebRequestContextSupportRequestProcessor}
 * @see {@link WebRequestContext}
 */
public class BroadleafRequestInterceptor implements WebRequestInterceptor {

    @Resource(name = "blRequestProcessor")
    protected WebRequestContextSupportRequestProcessor requestProcessor;

    @Override
    public void preHandle(WebRequest request) throws Exception {
        requestProcessor.process(request);
    }

    @Override
    public void postHandle(WebRequest request, ModelMap model) throws Exception {
        //unimplemented
    }

    @Override
    public void afterCompletion(WebRequest request, Exception ex) throws Exception {
        requestProcessor.postProcess(request);
    }

}
