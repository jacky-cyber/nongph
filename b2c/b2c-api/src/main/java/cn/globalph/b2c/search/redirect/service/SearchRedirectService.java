package cn.globalph.b2c.search.redirect.service;

import cn.globalph.b2c.search.redirect.domain.SearchRedirect;


/**
 * Created by bpolster.
 */
public interface SearchRedirectService {

    /**
     * Checks the passed in URL to determine if there is a matching SearchRedirect.
     * Returns null if no handler was found.
     * 
     * @param uri
     * @return
     */
    public SearchRedirect findSearchRedirectBySearchTerm(String uri);

}
