package cn.globalph.common.web;

/**
 * Responsible for providing the base url for the site / admin applications.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public interface BaseUrlResolver {

    /**
     * Returns the currently configured base url for the site. The default implementation of this interface
     * will return the value stored in the site.baseurl property for the current environment.
     * 
     * For example, in a development environment, this method might return: http://localhost:8080
     * 
     * @return the site baseurl, without a trailing slash
     */
    public String getSiteBaseUrl();

    /**
     * Returns the currently configured base url for the admin. The default implementation of this interface
     * will return the value stored in the admin.baseurl property for the current environment.
     * 
     * For example, in a development environment, this method might return: http://localhost:8080/admin
     * 
     * @return the admin baseurl, without a trailing slash
     */
    public String getAdminBaseUrl();

}
