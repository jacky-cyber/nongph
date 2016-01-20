package cn.globalph.b2c.search.redirect.dao;

import cn.globalph.b2c.search.redirect.domain.SearchRedirect;


/**
 * Created by ppatel.
 */
public interface SearchRedirectDao {


    public SearchRedirect findSearchRedirectBySearchTerm(String uri);

}
