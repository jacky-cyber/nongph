package cn.globalph.cms.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(PreviewTemplateController.REQUEST_MAPPING_PREFIX + "**")
public class PreviewTemplateController {
    private String templatePathPrefix = "templates";
    public static final String REQUEST_MAPPING_PREFIX = "/preview/";
    
    @RequestMapping
    public String displayPreview(HttpServletRequest httpServletRequest) {
        String requestURIPrefix = httpServletRequest.getContextPath() + REQUEST_MAPPING_PREFIX;
        String templatePath = httpServletRequest.getRequestURI().substring(requestURIPrefix.length() - 1);
        return templatePathPrefix + templatePath;
    }
    
}
