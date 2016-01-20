package cn.globalph.common.sitemap.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.sitemap.service.SiteMapService;

import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller to generate and retrieve site map files.
 * 
 * @author Joshua Skorton (jskorton)
 */
public class BroadleafSiteMapController {

    private static final Log LOG = LogFactory.getLog(BroadleafSiteMapController.class);

    @Resource(name = "blSiteMapService")
    protected SiteMapService siteMapService;

    /**
     * Retrieves a site map index file in XML format
     * 
     * @param request
     * @param response
     * @param model
     * @param fileName
     * @return
     */
    public InputStreamResource retrieveSiteMapFile(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        InputStream siteMapFile = siteMapService.getSiteMapFile(getRequestURIWithoutContext(request));
        if (siteMapFile == null) {
            response.setStatus(404);
            return null;
        }
        return new InputStreamResource(siteMapFile);
    }

    protected String getRequestURIWithoutContext(HttpServletRequest request) {
        if (request.getContextPath() != null) {
            return request.getRequestURI().substring(request.getContextPath().length());
        } else {
            return request.getRequestURI();
        }
    }

}
