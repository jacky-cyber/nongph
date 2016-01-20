package cn.globalph.passport.web.core.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Cookie used to protected against session fixation attacks
 * 
 * @see SessionFixationProtectionFilter
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class SessionFixationProtectionCookie {
    protected final Log logger = LogFactory.getLog(getClass());

    public static final String COOKIE_NAME = "ActiveID";

}
