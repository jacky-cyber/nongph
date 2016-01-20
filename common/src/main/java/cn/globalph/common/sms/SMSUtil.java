package cn.globalph.common.sms;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

public class SMSUtil {
    private static final Log LOG = LogFactory.getLog(SMSUtil.class);

    // 生成验证码
	public static String generateValidationCode() {
		StringBuilder vc = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			vc.append(Math.round(Math.floor(Math.random() * 10)));
		}
		return vc.toString();
	}
	
	// 发送手机短信验证码
    public static Map sendValidationCode(String mobile, String validationCode) {
        Map<String, String> map = new HashMap<>();
        try {
            sendMessage(mobile, "您的手机验证码为" + validationCode + ",有效时间为两分钟。");
            map.put("success", "验证码已发送到您手机");
        } catch (Exception e) {
            map.put("error", "验证码发送失败");
        }
        return map;
    }

    public static void sendMessage(String mobile, String msg) throws Exception {
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("api", "key-99523dc790bce4b53e26402c50f3c4a5"));
        WebResource webResource = client.resource("http://sms-api.luosimao.com/v1/send.json");
        WebResource.Builder builder = webResource.type(MediaType.APPLICATION_FORM_URLENCODED);
        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        formData.add("mobile", mobile);
        formData.add("message", msg + "【品荟生活】");
        ClientResponse response = builder.post(ClientResponse.class, formData);

        String textEntity = response.getEntity(String.class);
        assert response.getStatus() == 200;
        JSONObject jsonObj = new JSONObject(textEntity);
        int error_code = jsonObj.getInt("error");
        if (error_code == 0) {
            LOG.debug("Sent sms to " + mobile + " successfully, msg: " + msg);
        } else {
            String error_msg = jsonObj.getString("msg");
            LOG.error("send sms to " + mobile + " failed, error msg: " + error_msg);
            throw new Exception(error_msg);
        }

    }
}
