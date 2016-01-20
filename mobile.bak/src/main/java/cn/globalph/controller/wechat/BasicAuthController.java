package cn.globalph.controller.wechat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author steven
 * @since 2015/9/7
 */
public class BasicAuthController {
    private static final Log LOG = LogFactory.getLog(BasicAuthController.class);
    @Resource(name = "wechatUtils")
    private WechatUtils wechatUtils;

    public Boolean verifyBasicAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String sessionAuth = (String) request.getSession().getAttribute("auth");

        if (sessionAuth == null && !checkHeaderAuth(request)) {
            response.setStatus(401);
            response.setHeader("Cache-Control", "no-store");
            response.setDateHeader("Expires", 0);
            response.setHeader("WWW-authenticate", "Basic Realm=\"test\"");
            return false;
        }
//        response.setHeader("Content-type", "text/html;charset=utf-8");

        return true;
    }

    private boolean checkHeaderAuth(HttpServletRequest request) throws IOException {

        String auth = request.getHeader("Authorization");
        LOG.debug("auth encoded in base64 is " + getFromBASE64(auth));

        if ((auth != null) && (auth.length() > 6)) {
            auth = auth.substring(6, auth.length());

            String decodedAuth = getFromBASE64(auth);
            LOG.debug("auth decoded from base64 is " + decodedAuth);
            String[] combo = decodedAuth.split(":");
            if (wechatUtils.getAppId().equals(combo[0]) && wechatUtils.getAppSecret().equals(combo[1])) {
                request.getSession().setAttribute("auth", decodedAuth);
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }

    }

    private String getFromBASE64(String s) {
        if (s == null)
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(s);
            return new String(b);
        } catch (Exception e) {
            return null;
        }
    }
}
