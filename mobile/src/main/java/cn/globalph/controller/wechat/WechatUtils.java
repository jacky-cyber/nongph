package cn.globalph.controller.wechat;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.common.money.Money;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wecubics.payment.entity.wx.OrderQueryRequest;
import com.wecubics.payment.entity.wx.UnifiedOrder;
import com.wecubics.payment.utils.wx.WXPaymentUtil;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author steven
 * @since 6/18/15
 */
@Component("wechatUtils")
public class WechatUtils {
    private static final Log LOG = LogFactory.getLog(WechatUtils.class);

    @Value("${wechat.create.ip}")
    private String CREATE_IP;
    @Value("${wechat.appid}")
    private String APP_ID;
    @Value("${wechat.app.secret}")
    private String APP_SECRET;
    @Value("${wechat.mchid}")
    private String MCH_ID;
    @Value("${wechat.key}")
    private String KEY;

    public final static String TRATE_STATE = "trade_state";

    /**
     * Verify token with propriety system
     *
     * @param proprietyHost propiety system host
     * @param phone
     * @return proprietor
     */
    public List<Proprietor> getProprietors(String proprietyHost, String phone) {
        final String requestURL = "http://" + proprietyHost + "/api/household/contactno/" + phone;
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        GetMethod method = new GetMethod(requestURL);
        try {
            int statusCode = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                LOG.error("failed to get proprietors, status code: " + statusCode);
            }
            String response = method.getResponseBodyAsString();

            Gson gson = new Gson();
            Type type = new TypeToken<List<Proprietor>>() {
            }.getType();
            return gson.fromJson(response, type);
        } catch (Exception e) {
            LOG.error("failed to get proprietors for " + phone, e);
        } finally {
            try {
                client.getHttpConnectionManager().closeIdleConnections(1);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    /**
     * request openId by code
     *
     * @param code {@code String}
     * @return openid
     */
    public String requestOpenId(String code) {
        final String requestURL = "https://api.weixin.qq.com/sns/oauth2/access_token"
                + "?appid=" + APP_ID
                + "&secret=" + APP_SECRET
                + "&code=" + code
                + "&grant_type=authorization_code";
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        GetMethod method = new GetMethod(requestURL);
        try {
            int statusCode = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                LOG.debug("failed to validate token: status code: " + statusCode);
            }
            String response = method.getResponseBodyAsString();

            Gson gson = new Gson();
            Map map = gson.fromJson(response, Map.class);
            if (!map.containsKey("errcode")) {
                return (String) map.get("openid");
            }

        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            try {
                client.getHttpConnectionManager().closeIdleConnections(1);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public Map generateWechatPayPrepareInfo(String serverHost, String openId, Order order, Boolean isPrd) {
        String body = order.getOrderItems().get(0).getName() + "等";

        UnifiedOrder unifiedOrder = new UnifiedOrder();
        unifiedOrder.setAppid(APP_ID);
        unifiedOrder.setMch_id(MCH_ID);
        unifiedOrder.setOut_trade_no(order.getOrderNumber());
        Money money = order.getTotal();
        if (isPrd) {
            unifiedOrder.setTotal_fee(money.getAmount().multiply(new BigDecimal(100)).intValue());
            unifiedOrder.setBody(body.trim());
        } else {
            int fee = money.divide(100).getAmount().intValue();
            if (fee < 1) {
                fee = 1;
            }
            unifiedOrder.setTotal_fee(fee);
            unifiedOrder.setBody(openId + body.trim());
        }
        unifiedOrder.setSpbill_create_ip(CREATE_IP);
        unifiedOrder.setNotify_url("http://" + serverHost + "/wechatpay/notify");
        unifiedOrder.setTrade_type("JSAPI");
        unifiedOrder.setOpenid(openId);
        Map resultMap = WXPaymentUtil.getBrandWCPayRequest(unifiedOrder, KEY);
        if (resultMap.isEmpty()) {
            return null;
        }
        return resultMap;
    }

    public Map<String, Object> queryWechatPayStatus(String orderNumber) {
        OrderQueryRequest request = new OrderQueryRequest();
        request.setAppid(APP_ID);
        request.setMch_id(MCH_ID);
        request.setOut_trade_no(orderNumber);
        return WXPaymentUtil.getOrderStatus(request, KEY);
    }

    public String getCREATE_IP() {
        return CREATE_IP;
    }

    public String getAppId() {
        return APP_ID;
    }

    public String getAppSecret() {
        return APP_SECRET;
    }

    public String getMchId() {
        return MCH_ID;
    }

    public String getKey() {
        return KEY;
    }
}
