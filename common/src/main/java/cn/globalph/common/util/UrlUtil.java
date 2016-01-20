package cn.globalph.common.util;

import org.apache.commons.lang.StringUtils;

public class UrlUtil {
    public static String generateUrlKey(String toConvert) {
        if (toConvert.matches(".*?\\W.*?")) {
            //remove all non-word characters
            String result = toConvert.replaceAll("\\W","");
            //uncapitalizes the first letter of the url key
            return StringUtils.uncapitalize(result);
        } else {
            return StringUtils.uncapitalize(toConvert);
        }
    }
    
        /**
         * If the url does not include "//" then the system will ensure that the
         * application context is added to the start of the URL.
         * 
         * @param url
         * @return
         */
        public static String fixRedirectUrl(String contextPath, String url) {
            if (url.indexOf("//") < 0) {

                if (contextPath != null && (!"".equals(contextPath))) {
                    if (!url.startsWith("/")) {
                        url = "/" + url;
                    }
                    if (!url.startsWith(contextPath)) {
                        url = contextPath + url;
                    }
                }
            }
            return url;

        }
        
}
