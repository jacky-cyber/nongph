package cn.globalph.cms.web.controller;

import org.apache.commons.lang3.StringUtils;

import cn.globalph.cms.web.PageHandlerMapping;
import cn.globalph.common.page.dto.PageDTO;
import cn.globalph.common.template.TemplateType;
import cn.globalph.common.web.TemplateTypeAware;
import cn.globalph.common.web.WebRequestContext;
import cn.globalph.common.web.controller.BasicController;
import cn.globalph.common.web.deeplink.DeepLinkService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class works in combination with the PageHandlerMapping which finds a page based upon
 * the request URL.
 */
public class BasicPageController extends BasicController implements Controller, TemplateTypeAware {

    protected static String MODEL_ATTRIBUTE_NAME="page";    
    
    @Autowired(required = false)
    @Qualifier("blPageDeepLinkService")
    protected DeepLinkService<PageDTO> deepLinkService;


    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView model = new ModelAndView();
        PageDTO page = (PageDTO) request.getAttribute(PageHandlerMapping.PAGE_ATTRIBUTE_NAME);
        assert page != null;

        model.addObject(MODEL_ATTRIBUTE_NAME, page);

        String plainTextStr = page.getPageFields().get("plainText");

        if (!StringUtils.isEmpty(plainTextStr)) {
            if (Boolean.valueOf(plainTextStr)) {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/plain");
            }
        }

        model.setViewName(page.getTemplatePath());
        addDeepLink(model, deepLinkService, page);
        return model;
    }

    @Override
    public String getExpectedTemplateName(HttpServletRequest request) {
        WebRequestContext context = WebRequestContext.getWebRequestContext();
        if (context != null) {
            PageDTO page = (PageDTO) request.getAttribute(PageHandlerMapping.PAGE_ATTRIBUTE_NAME);
            return page.getTemplatePath();
        }
        return "";
    }

    @Override
    public TemplateType getTemplateType(HttpServletRequest request) {
        return TemplateType.PAGE;
    }

}
