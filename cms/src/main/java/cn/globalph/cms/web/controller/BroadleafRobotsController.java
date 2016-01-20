package cn.globalph.cms.web.controller;

import cn.globalph.cms.page.service.PageService;
import cn.globalph.common.RequestDTO;
import cn.globalph.common.TimeDTO;
import cn.globalph.common.page.dto.PageDTO;
import cn.globalph.common.storage.service.GlobalphFileUtils;
import cn.globalph.common.time.SystemTime;
import cn.globalph.common.web.BaseUrlResolver;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class serves up the Robots.txt file.    The default contents can be overridden by 
 * adding a Page named "/robots.txt" in the BLC admin or DB. 
 *
 * @author bpolster
 */
public class BroadleafRobotsController {

    public static final String BLC_RULE_MAP_PARAM = "blRuleMap";

    // The following attribute is set in BroadleafProcessURLFilter
    public static final String REQUEST_DTO = "blRequestDTO";

    @Resource(name = "blBaseUrlResolver")
    private BaseUrlResolver baseUrlResolver;

    @Resource(name = "blPageService")
    private PageService pageService;

    public String getRobotsFile(HttpServletRequest request, HttpServletResponse response) {

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        PageDTO page = pageService.findPageByURI("/robots.txt", buildMvelParameters(request), isSecure(request));

        if (page != null && page.getPageFields().containsKey("body")) {
            String body = page.getPageFields().get("body");
            body = body.replace("${siteBaseUrl}", baseUrlResolver.getSiteBaseUrl());
            return body;
        } else {
            return getDefaultRobotsTxt();
        }
    }
    
    public boolean isSecure(HttpServletRequest request) {
        boolean secure = false;
        if (request != null) {
             secure = ("HTTPS".equalsIgnoreCase(request.getScheme()) || request.isSecure());
        }
        return secure;
    }

    /**
     * Used to produce a working but simple robots.txt.    Can be overridden in code or by defining a page
     * managed in the Broadleaf CMS named  "/robots.txt"
     * 
     * @return
     */
    protected String getDefaultRobotsTxt() {
        StringBuilder sb = new StringBuilder();
        sb.append("# Using default Broadleaf Commerce robots.txt file").append("\n");
        sb.append("User-agent: *").append("\n");
        sb.append("Disallow:").append("\n");
        String fileLoc = GlobalphFileUtils.appendUnixPaths(baseUrlResolver.getSiteBaseUrl(), "/sitemap.xml.gz");

        sb.append("Sitemap:").append(fileLoc);
        return sb.toString();
    }

    /**
    *
    * @param request
    * @return
    */
    private Map<String, Object> buildMvelParameters(HttpServletRequest request) {
        TimeDTO timeDto = new TimeDTO(SystemTime.asCalendar());
        RequestDTO requestDto = (RequestDTO) request.getAttribute(REQUEST_DTO);

        Map<String, Object> mvelParameters = new HashMap<String, Object>();
        mvelParameters.put("time", timeDto);
        mvelParameters.put("request", requestDto);

        Map<String, Object> blcRuleMap = (Map<String, Object>) request.getAttribute(BLC_RULE_MAP_PARAM);
        if (blcRuleMap != null) {
            for (String mapKey : blcRuleMap.keySet()) {
                mvelParameters.put(mapKey, blcRuleMap.get(mapKey));
            }
        }

        return mvelParameters;
    }
}
