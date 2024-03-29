package cn.globalph.common.util;

import cn.globalph.common.web.WebRequestContext;

import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;


/**
 * Convenience methods for interacting with the request
 * 
 * @author bpolster
 */
public class BLCRequestUtils {
    
    private static String OK_TO_USE_SESSION = "blOkToUseSession";
    
    /**
     * Broadleaf "Resolver" and "Filter" classes may need to know if they are allowed to utilize the session.
     * BLC uses a pattern where we will store an attribute in the request indicating whether or not the 
     * session can be used.   For example, when using the REST APIs, we typically do not want to utilize the
     * session.
     *
     */
    public static boolean isOKtoUseSession(WebRequest request) {
        Boolean useSessionForRequestProcessing = (Boolean) request.getAttribute(OK_TO_USE_SESSION, WebRequest.SCOPE_REQUEST);
        if (useSessionForRequestProcessing == null) {
            // by default we will use the session
            return true;
        } else {
            return useSessionForRequestProcessing.booleanValue();
        }
    }
    
    /**
     * Sets whether or not Broadleaf can utilize the session in request processing.   Used by the REST API
     * flow so that RESTful calls do not utilize the session.
     *
     */
    public static void setOKtoUseSession(WebRequest request, Boolean value) {
        request.setAttribute(OK_TO_USE_SESSION, value, WebRequest.SCOPE_REQUEST);
    }

    /**
     * Get header or url parameter.    Will obtain the parameter from a header variable or a URL parameter, preferring
     * header values if they are set.
     *
     */
    public static String getURLorHeaderParameter(WebRequest request, String varName) {
        String returnValue = request.getHeader(varName);
        if (returnValue == null) {
            returnValue = request.getParameter(varName);
        }
        return returnValue;
    }

    /**
     * Convenience method to obtain the server prefix of the current request.
     * Useful for many modules that configure Relative URL's and need to send absolute URL's
     * to Third Party Gateways.
     */
    public static String getRequestedServerPrefix() {
        HttpServletRequest request = WebRequestContext.getWebRequestContext().getRequest();
        String scheme = request.getScheme();
        StringBuilder serverPrefix = new StringBuilder(scheme);
        serverPrefix.append("://");
        serverPrefix.append(request.getServerName());
        if ((scheme.equalsIgnoreCase("http") && request.getServerPort() != 80) || (scheme.equalsIgnoreCase("https") && request.getServerPort() != 443)) {
            serverPrefix.append(":");
            serverPrefix.append(request.getServerPort());
        }
        return serverPrefix.toString();
    }
}
