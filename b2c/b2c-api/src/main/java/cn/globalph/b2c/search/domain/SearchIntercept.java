package cn.globalph.b2c.search.domain;

import cn.globalph.b2c.search.redirect.domain.SearchRedirect;

/**
 * @deprecated Replaced in functionality by {@link SearchRedirect}
 */
@Deprecated
public interface SearchIntercept {

    public abstract String getTerm();

    public abstract void setTerm(String term);

    public abstract String getRedirect();

    public abstract void setRedirect(String redirect);

}
