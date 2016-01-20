package cn.globalph.cms.url.service;

import net.sf.ehcache.Cache;

import java.util.List;

import cn.globalph.cms.url.domain.URLHandler;
import cn.globalph.common.sandbox.domain.SandBox;


/**
 * Created by bpolster.
 */
public interface URLHandlerService {

    /**
     * Checks the passed in URL to determine if there is a matching URLHandler.
     * Returns null if no handler was found.
     * 
     * @param uri
     * @return
     */
    URLHandler findURLHandlerByURI(String uri);
    
    List<URLHandler> findAllURLHandlers();
    
    URLHandler saveURLHandler(URLHandler handler);

    URLHandler findURLHandlerById(Long id);

    void removeURLHandlerFromCache(SandBox sandBox, URLHandler urlhandler);

    Cache getUrlHandlerCache();

}
