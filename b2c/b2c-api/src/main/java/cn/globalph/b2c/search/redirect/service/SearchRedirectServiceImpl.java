package cn.globalph.b2c.search.redirect.service;

import org.springframework.stereotype.Service;

import cn.globalph.b2c.search.redirect.dao.SearchRedirectDao;
import cn.globalph.b2c.search.redirect.domain.SearchRedirect;

import javax.annotation.Resource;

/**
 * Created by ppatel.
 */
@Service("blSearchRedirectService")
public class SearchRedirectServiceImpl implements SearchRedirectService {

  
    @Resource(name = "blSearchRedirectDao")
    protected SearchRedirectDao SearchRedirectDao;


    /**
     * Checks the passed in URL to determine if there is a matching
     * SearchRedirect. Returns null if no handler was found.
     * 
     * @param uri
     * @return
     */
    @Override
    public SearchRedirect findSearchRedirectBySearchTerm(String uri) {

        SearchRedirect SearchRedirect = SearchRedirectDao
                .findSearchRedirectBySearchTerm(uri);
        if (SearchRedirect != null) {
            return SearchRedirect;
        } else {
            return null;
        }

    }

}
