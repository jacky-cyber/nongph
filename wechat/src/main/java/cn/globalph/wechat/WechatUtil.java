package cn.globalph.wechat;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author steven
 * @since 8/25/15
 */
public class WechatUtil {
    private static final Log LOG = LogFactory.getLog(WechatUtil.class);

    private String wechatServer;
    private String createIp;
    private String appId;
    private String appSecret;
    private String mchId;
    private String key;

    public Map<String, String> sendConfirmMessage(String openid, String orderNo, BigDecimal amount, String method, String content) {
        LOG.debug("Try to send wechat message to " + openid);
        Map<String, String> map = new HashMap<>();

        try {
            MultivaluedMapImpl formData = new MultivaluedMapImpl();
            formData.add("openid", openid);
            formData.add("orderNo", orderNo);
            formData.add("amount", amount);
            formData.add("method", method);
            formData.add("content", content);
            JSONObject jsonObj = requestWechat("POST", "http://" + wechatServer + "/api/sendTmplMsg", formData);
            int errCode = jsonObj.getInt("errcode");
            String errMsg = jsonObj.getString("errmsg");
            if (errCode == 0) {
                map.put("success", "付款成功通知发送成功");
                LOG.debug("Sent wechat message to " + openid + " successfully");
            } else {
                map.put("error", "付款成功通知发送失败");
                LOG.error(errMsg);
                LOG.debug("send wechat message to " + openid + " unsuccessfully");
            }
        } catch (JSONException ex) {
            LOG.error(ex.getMessage(), ex);
            map.put("error", "微信发送失败");
        }
        return map;
    }

    public Map<String, String> sendConfirmMessageToAdmin(String orderNo, BigDecimal amount, String method, String content) {
        LOG.debug("Try to send wechat message to admins");
        Map<String, String> map = new HashMap<>();
        try {
            MultivaluedMapImpl formData = new MultivaluedMapImpl();
            formData.add("orderNo", orderNo);
            formData.add("amount", amount);
            formData.add("method", method);
            formData.add("content", content);
            JSONObject jsonObj = requestWechat("POST", "http://" + wechatServer + "/api/sendAdminTmplMsg", formData);
            int errCode = jsonObj.getInt("errcode");
            String errMsg = jsonObj.getString("errmsg");
            if (errCode == 0) {
                map.put("success", "付款成功通知发送成功");
                LOG.debug("Sent wechat message to admins successfully");
            } else {
                map.put("error", "付款成功通知发送失败");
                LOG.error(errMsg);
                LOG.debug("send wechat message to admins unsuccessfully");
            }
        } catch (JSONException ex) {
            LOG.error(ex.getMessage(), ex);
            map.put("error", "微信发送失败");
        }
        return map;
    }

    public JssdkSign requestJssdkSign(String url) {
        LOG.debug("Try to request jssdk sign");

        try {
            MultivaluedMapImpl formData = new MultivaluedMapImpl();
            formData.add("url", url);
            JSONObject jsonObj = requestWechat("POST", "http://" + wechatServer + "/api/requestJssdkSign", formData);
            if (jsonObj.has("appid")) {
                JssdkSign jssdkSign = new JssdkSign();
                jssdkSign.setAppid(jsonObj.getString("appid"));
                jssdkSign.setNonceStr(jsonObj.getString("nonceStr"));
                jssdkSign.setTimestamp(jsonObj.getString("timestamp"));
                jssdkSign.setSign(jsonObj.getString("sign"));
                LOG.debug("Got jssdk sign, "+ jssdkSign.getAppid() +", "+ jssdkSign.getNonceStr()
                        + ", "+jssdkSign.getTimestamp() + ", " + jssdkSign.getSign());
                return jssdkSign;
            } else {
                String errMsg = jsonObj.getString("errmsg");
                LOG.error(errMsg);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    public JSONObject requestWechat(String method, String url, MultivaluedMapImpl formData) throws JSONException {
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter(appId, appSecret));
        WebResource webResource = client.resource(url);
        WebResource.Builder builder = webResource.type(MediaType.APPLICATION_FORM_URLENCODED);
        ClientResponse response = builder.method(method, ClientResponse.class, formData);

        String textEntity = response.getEntity(String.class);
        assert response.getStatus() == 200;
        return new JSONObject(textEntity);
    }

    public String getWechatServer() {
        return wechatServer;
    }

    public void setWechatServer(String wechatServer) {
        this.wechatServer = wechatServer;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
