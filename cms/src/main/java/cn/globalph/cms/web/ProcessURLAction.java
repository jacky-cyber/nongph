package cn.globalph.cms.web;

/**
 * @deprecated in favor of Spring MVC mechanisms (@see PageHandlerMapping)
 * Enum that indicates the action to take when processing a URL.
 * <ul>
 *     <li>PAGE - indicates that the URL will be handled as a CMS managed page</li>
 *     <li>PRODUCT - indicates that the URL is an SEO manged product page</li>
 *     <li>CATEGORY - indicate that the URL is an SEO managed category URL</li>
 *     <li>PROCEED - indicates that the URL should be passed through and is not handled by BLC custom filters</li>
 *     <li>REDIRECT - indicates that the URL should be redirected to another URL</li>
 *     <li>UNKNOWN - indicates that it has not yet been determined how to process the URL</li>
 * </ul>
 *
 * Created by bpolster.
 */
public enum ProcessURLAction {
    PAGE,
    PRODUCT,
    CATEGORY,
    PROCEED,
    REDIRECT,
    UNKNOWN
}
