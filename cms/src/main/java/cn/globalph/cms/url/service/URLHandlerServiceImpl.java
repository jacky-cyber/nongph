package cn.globalph.cms.url.service;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.cms.url.dao.URLHandlerDao;
import cn.globalph.cms.url.domain.URLHandler;
import cn.globalph.cms.url.domain.URLHandlerDTO;
import cn.globalph.common.cache.CacheStatType;
import cn.globalph.common.cache.StatisticsService;
import cn.globalph.common.sandbox.domain.SandBox;

import org.codehaus.jackson.map.util.LRUMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

@Service("blURLHandlerService")
public class URLHandlerServiceImpl implements URLHandlerService {

    private static final Log LOG = LogFactory.getLog(URLHandlerServiceImpl.class);

    @Resource(name="blURLHandlerDao")
    protected URLHandlerDao urlHandlerDao;

    @Resource(name="blStatisticsService")
    protected StatisticsService statisticsService;
    
    protected Cache urlHandlerCache;

    protected LRUMap<String, Pattern> urlPatternMap = new LRUMap<String, Pattern>(10, 2000);

    /**
     * Checks the passed in URL to determine if there is a matching URLHandler.
     * Returns null if no handler was found.
     * 
     * @param uri
     * @return
     */
    @Override
    public URLHandler findURLHandlerByURI(String uri) {
        URLHandler urlHandler = lookupHandlerFromCache(uri);
        return urlHandler;
    }

    @Override
    public URLHandler findURLHandlerById(Long id) {
        return urlHandlerDao.findURLHandlerById(id);
    }

    @Override
    public void removeURLHandlerFromCache(SandBox sandBox, URLHandler urlhandler) {
        getUrlHandlerCache().remove(buildKey(sandBox, urlhandler));
    }

    @Override
    public List<URLHandler> findAllURLHandlers() {
        return urlHandlerDao.findAllURLHandlers();
    }

    @Override
    @Transactional("blTransactionManager")
    public URLHandler saveURLHandler(URLHandler handler) {
        return urlHandlerDao.saveURLHandler(handler);
    }

    @Override
    public Cache getUrlHandlerCache() {
        if (urlHandlerCache == null) {
            urlHandlerCache = CacheManager.getInstance().getCache("cmsUrlHandlerCache");
        }
        return urlHandlerCache;
    }

    protected String buildKey(SandBox sandBox, String requestUri) {
        String key = requestUri;
        if (sandBox != null) {
            key = sandBox.getId() + "_" + key;
        }       
        return key;
    }
    
    protected String buildKey(SandBox sandBox, URLHandler urlHandler) {
        String key = urlHandler.getIncomingURL();
        if (sandBox != null) {
            key = sandBox.getId() + "_" + key;
        }       
        return key;
    }
    
    protected URLHandler checkForMatches(String requestURI) {
        URLHandler currentHandler = null;
        try {
            List<URLHandler> urlHandlers = findAllURLHandlers();
            for (URLHandler urlHandler : urlHandlers) {
                currentHandler = urlHandler;
                String incomingUrl = currentHandler.getIncomingURL();
                if (!incomingUrl.startsWith("^")) {
                    if (incomingUrl.startsWith("/")) {
                        incomingUrl = "^" + incomingUrl + "$";
                    } else {
                        incomingUrl = "^/" + incomingUrl + "$";
                    }
                }

                Pattern p = urlPatternMap.get(incomingUrl);
                if (p == null) {
                    p = Pattern.compile(incomingUrl);
                    urlPatternMap.put(incomingUrl, p);
                }
                Matcher m = p.matcher(requestURI);
                if (m.find()) {
                    String newUrl = m.replaceFirst(urlHandler.getNewURL());
                    if (newUrl.equals(urlHandler.getNewURL())) {
                        return urlHandler;
                    } else {
                        return new URLHandlerDTO(newUrl, urlHandler.getUrlRedirectType());
                    }
                }

            }
        } catch (RuntimeException re) {
            if (currentHandler != null) {
                // We don't want an invalid regex to cause tons of logging                
                if (LOG.isWarnEnabled()) {
                    LOG.warn("Error parsing URL Handler (incoming =" + currentHandler.getIncomingURL() + "), outgoing = ( "
                            + currentHandler.getNewURL() + "), " + requestURI);
                }
            }
        }


        return null;
    }

    protected URLHandler lookupHandlerFromCache(String requestURI)  {
        return checkForMatches(requestURI);
    }
    
    protected URLHandler getUrlHandlerFromCache(String key) {
        Element cacheElement = getUrlHandlerCache().get(key);
        if (cacheElement != null) {
            statisticsService.addCacheStat(CacheStatType.URL_HANDLER_CACHE_HIT_RATE.toString(), true);
            return (URLHandler) cacheElement.getValue();
        }
        statisticsService.addCacheStat(CacheStatType.URL_HANDLER_CACHE_HIT_RATE.toString(), false);
        return null;
    }

    protected URLHandler findURLHandlerByURIInternal(String uri) {
        URLHandler urlHandler = urlHandlerDao.findURLHandlerByURI(uri);
        if (urlHandler != null) {
            return urlHandler;
        }
        return null;
    }

}
