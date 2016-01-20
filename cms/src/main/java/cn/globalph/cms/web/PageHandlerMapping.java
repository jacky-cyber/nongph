package cn.globalph.cms.web;

import cn.globalph.cms.page.service.PageService;
import cn.globalph.cms.web.controller.BasicPageController;
import cn.globalph.common.RequestDTO;
import cn.globalph.common.TimeDTO;
import cn.globalph.common.page.dto.NullPageDTO;
import cn.globalph.common.page.dto.PageDTO;
import cn.globalph.common.time.SystemTime;
import cn.globalph.common.web.BLCAbstractHandlerMapping;
import cn.globalph.common.web.WebRequestContext;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * This handler mapping works with the Page entity to determine if a page has been configured for
 * the passed in URL.   
 * 
 * If the URL represents a valid PageUrl, then this mapping returns 
 * 
 * Allows configuration of the controller name to use if a Page was found.
 *
 * @see cn.globalph.cms.page.domain.Page
 * @see BasicPageController
 */
public class PageHandlerMapping extends BLCAbstractHandlerMapping {
    
    private String controllerName="blPageController";
    public static final String BLC_RULE_MAP_PARAM = "blRuleMap";

    // The following attribute is set in BroadleafProcessURLFilter
    public static final String REQUEST_DTO = "blRequestDTO";
    
    @Resource(name = "blPageService")
    private PageService pageService;
    
    public static final String PAGE_ATTRIBUTE_NAME = "BLC_PAGE";        

    @Override
    protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
        WebRequestContext context = WebRequestContext.getWebRequestContext();
        if (context != null && context.getRequestURIWithoutContext() != null) {
            PageDTO page = pageService.findPageByURI(context.getRequestURIWithoutContext(), buildMvelParameters(request), context.isSecure());

            if (page != null && ! (page instanceof NullPageDTO)) {
                context.getRequest().setAttribute(PAGE_ATTRIBUTE_NAME, page);
                return controllerName;
            }
        }
        return null;
    }
    
     /**
     * MVEL is used to process the content targeting rules.
     *
     *
     * @param request
     * @return
     */
    private Map<String,Object> buildMvelParameters(HttpServletRequest request) {
        TimeDTO timeDto = new TimeDTO(SystemTime.asCalendar());
        RequestDTO requestDto = (RequestDTO) request.getAttribute(REQUEST_DTO);

        Map<String, Object> mvelParameters = new HashMap<String, Object>();
        mvelParameters.put("time", timeDto);
        mvelParameters.put("request", requestDto);

        Map<String,Object> blcRuleMap = (Map<String,Object>) request.getAttribute(BLC_RULE_MAP_PARAM);
        if (blcRuleMap != null) {
            for (String mapKey : blcRuleMap.keySet()) {
                mvelParameters.put(mapKey, blcRuleMap.get(mapKey));
            }
        }

        return mvelParameters;
    }
}
