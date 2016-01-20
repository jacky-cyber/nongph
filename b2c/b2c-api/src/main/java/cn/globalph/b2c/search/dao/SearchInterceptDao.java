package cn.globalph.b2c.search.dao;

import cn.globalph.b2c.search.domain.SearchIntercept;
import cn.globalph.b2c.search.redirect.dao.SearchRedirectDao;

import java.util.List;

/**
 * @deprecated Replaced in functionality by {@link SearchRedirectDao}
 */
@Deprecated
public interface SearchInterceptDao {
    public SearchIntercept findInterceptByTerm(String term);
    public List<SearchIntercept> findAllIntercepts();
    public void createIntercept(SearchIntercept intercept);
    public void updateIntercept(SearchIntercept intercept);
    public void deleteIntercept(SearchIntercept intercept);
}
