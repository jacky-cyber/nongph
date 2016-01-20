
package cn.globalph.common.web.payment.expression;

import cn.globalph.common.extension.ExtensionHandler;
import cn.globalph.common.extension.ExtensionResultStatusType;

import java.util.Map;

/**
 * @author Elbert Bautista (elbertbautista)
 */
public interface PaymentGatewayFieldExtensionHandler extends ExtensionHandler {

    public ExtensionResultStatusType mapFieldName(String fieldNameKey, Map<String, String> fieldNameMap);

}
