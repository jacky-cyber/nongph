package cn.globalph.common.web;

import org.springframework.security.web.util.AntPathRequestMatcher;
import org.springframework.security.web.util.RequestMatcher;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author Jeff Fischer
 */
public class BroadleafGWTModuleURLMappingResourceHttpRequestHandler extends ResourceHttpRequestHandler {

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> urlPatternDispatchMap = (Map<String, String>) getApplicationContext().getBean("blResourceUrlPatternRequestDispatchMap");
        for (Map.Entry<String, String> entry : urlPatternDispatchMap.entrySet()) {
            RequestMatcher matcher = new AntPathRequestMatcher(entry.getKey());
            if (matcher.matches(request)){
                request.getRequestDispatcher(entry.getValue()).forward(request, response);
                return;
            }
        }
        super.handleRequest(request, response);
    }

}
