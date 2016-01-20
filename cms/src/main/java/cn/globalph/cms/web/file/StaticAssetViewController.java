package cn.globalph.cms.web.file;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.cms.common.AssetNotFoundException;
import cn.globalph.cms.file.service.StaticAssetCacheService;
import cn.globalph.common.classloader.release.ThreadLocalManager;
import cn.globalph.common.web.SiteResolver;
import cn.globalph.common.web.WebRequestContext;

import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by felix.wu
 */
public class StaticAssetViewController extends AbstractController {

    private static final Log LOG = LogFactory.getLog(StaticAssetViewController.class);
    
    private String assetServerUrlPrefix;
    private String viewResolverName;

    @Resource(name="cmsStaticAssetCacheService")
    private StaticAssetCacheService staticAssetCacheService;

    @Resource(name = "blSiteResolver")
    private SiteResolver siteResolver;
    
    public String getAssetServerUrlPrefix() {
        return assetServerUrlPrefix;
    }
    public void setAssetServerUrlPrefix(String assetServerUrlPrefix) {        
        this.assetServerUrlPrefix = assetServerUrlPrefix;
    }

    public String getViewResolverName() {
        return viewResolverName;
    }
    public void setViewResolverName(String viewResolverName) {
        this.viewResolverName = viewResolverName;
    }

    /**
     * Process the static asset request by determining the asset name.
     * Checks the current sandbox for a matching asset.   If not found, checks the
     * production sandbox.
     *
     * The view portion will be handled by a component with the name "blStaticAssetView" This is
     * intended to be the specific class StaticAssetView.
     *
     * @see StaticAssetView
     * @see #handleRequest
     */
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fullUrl = removeAssetPrefix(request.getRequestURI());

        // 通常情况下静态资产的访问不需要通过Spring Security管道，但是对site的访问需要
        WebRequestContext context = WebRequestContext.getWebRequestContext();
        context.setSite( siteResolver.resolveSite( new ServletWebRequest(request, response)) );
        try {
        	String[] cached = staticAssetCacheService.getCachedStaticAsset( fullUrl, convertParameterMap( request.getParameterMap() ) );
            Map<String, String> model = buildModel(cached[0], cached[1]);
            return new ModelAndView(viewResolverName, model);
        } catch (AssetNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        } catch (Exception e) {
            LOG.error("Unable to retrieve static asset", e);
            throw new RuntimeException(e);
        } finally {
            ThreadLocalManager.remove();
        }
    }
    
    private String removeAssetPrefix(String requestURI) {
        String fileName = requestURI;
        if (assetServerUrlPrefix != null) {
            int pos = fileName.indexOf(assetServerUrlPrefix);
            fileName = fileName.substring(pos+assetServerUrlPrefix.length());

            if (! fileName.startsWith("/")) {
                fileName = "/"+fileName;
            }
        }

        return fileName;
        
    }

    private Map<String, String> convertParameterMap(Map<String, String[]> parameterMap) {
        Map<String, String> convertedMap = new LinkedHashMap<String, String>(parameterMap.size());
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            convertedMap.put(entry.getKey(), StringUtils.join(entry.getValue(), ','));
        }

        return convertedMap;
    }
    
    private Map<String, String> buildModel(String returnFilePath, String mimeType) {
        Map<String, String> model = new HashMap<String, String>(2);
        model.put("cacheFilePath", returnFilePath);
        model.put("mimeType", mimeType);

        return model;
    }
}
