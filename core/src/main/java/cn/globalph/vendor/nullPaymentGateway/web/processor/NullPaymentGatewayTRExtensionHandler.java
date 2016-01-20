package cn.globalph.vendor.nullPaymentGateway.web.processor;

import cn.globalph.common.payment.dto.PaymentResponseDTO;
import cn.globalph.common.payment.service.PaymentGatewayConfiguration;
import cn.globalph.common.payment.service.PaymentGatewayTransparentRedirectService;
import cn.globalph.common.web.payment.processor.AbstractTRCreditCardExtensionHandler;
import cn.globalph.common.web.payment.processor.TRCreditCardExtensionManager;
import cn.globalph.vendor.nullPaymentGateway.service.payment.NullPaymentGatewayConstants;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * This sample handler will add itself to the {@link TRCreditCardExtensionManager}
 * and will add some default hidden parameters/form POST URL for our fake
 * {@link cn.global.vendor.nullPaymentGateway.web.controller.NullPaymentGatewayProcessorController}
 *
 * Note, we don't want this loaded into the extension manager
 * if a real payment gateway is used, so make sure to not scan this class when
 * using a real implementation. This is for demo purposes only.
 *
 * In order to use this sample extension handler, you will need to component scan
 * the package "cn.globalph".
 *
 * This should NOT be used in production, and is meant solely for demonstration
 * purposes only.
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Service("blNullPaymentGatewayTRExtensionHandler")
public class NullPaymentGatewayTRExtensionHandler extends AbstractTRCreditCardExtensionHandler {

    public static final String FORM_ACTION_URL = NullPaymentGatewayConstants.TRANSPARENT_REDIRECT_URL;
    public static final String FORM_HIDDEN_PARAMS = "FORM_HIDDEN_PARAMS";

    @Resource(name = "blTRCreditCardExtensionManager")
    protected TRCreditCardExtensionManager extensionManager;

    @Resource(name = "blNullPaymentGatewayTransparentRedirectService")
    protected PaymentGatewayTransparentRedirectService transparentRedirectService;

    @Resource(name = "blNullPaymentGatewayConfiguration")
    protected PaymentGatewayConfiguration configuration;

    @PostConstruct
    public void init() {
        if (isEnabled()) {
            extensionManager.registerHandler(this);
        }
    }

    @Override
    public String getFormActionURLKey() {
        return FORM_ACTION_URL;
    }

    @Override
    public String getHiddenParamsKey() {
        return FORM_HIDDEN_PARAMS;
    }

    @Override
    public PaymentGatewayConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public PaymentGatewayTransparentRedirectService getTransparentRedirectService() {
        return transparentRedirectService;
    }

    @Override
    public void populateFormParameters(Map<String, Map<String, String>> formParameters, PaymentResponseDTO responseDTO) {
        String actionUrl = (String) responseDTO.getResponseMap().get(NullPaymentGatewayConstants.TRANSPARENT_REDIRECT_URL);
        Map<String, String> actionValue = new HashMap<String, String>();
        actionValue.put(getFormActionURLKey(), actionUrl);
        formParameters.put(getFormActionURLKey(), actionValue);

        Map<String, String> hiddenFields = new HashMap<String, String>();
        hiddenFields.put(NullPaymentGatewayConstants.TRANSACTION_AMT,
                responseDTO.getResponseMap().get(NullPaymentGatewayConstants.TRANSACTION_AMT).toString());
        hiddenFields.put(NullPaymentGatewayConstants.ORDER_ID,
                responseDTO.getResponseMap().get(NullPaymentGatewayConstants.ORDER_ID).toString());

        if (responseDTO.getResponseMap().get(NullPaymentGatewayConstants.BILLING_FIRST_NAME) != null) {
            hiddenFields.put(NullPaymentGatewayConstants.BILLING_FIRST_NAME,
                responseDTO.getResponseMap().get(NullPaymentGatewayConstants.BILLING_FIRST_NAME).toString());
        }

        if (responseDTO.getResponseMap().get(NullPaymentGatewayConstants.BILLING_LAST_NAME) != null) {
            hiddenFields.put(NullPaymentGatewayConstants.BILLING_LAST_NAME,
                responseDTO.getResponseMap().get(NullPaymentGatewayConstants.BILLING_LAST_NAME).toString());
        }

        if (responseDTO.getResponseMap().get(NullPaymentGatewayConstants.BILLING_ADDRESS_LINE1) != null) {
            hiddenFields.put(NullPaymentGatewayConstants.BILLING_ADDRESS_LINE1,
                responseDTO.getResponseMap().get(NullPaymentGatewayConstants.BILLING_ADDRESS_LINE1).toString());
        }

        if (responseDTO.getResponseMap().get(NullPaymentGatewayConstants.BILLING_ADDRESS_LINE2) != null) {
            hiddenFields.put(NullPaymentGatewayConstants.BILLING_ADDRESS_LINE2,
                responseDTO.getResponseMap().get(NullPaymentGatewayConstants.BILLING_ADDRESS_LINE2).toString());
        }

        if (responseDTO.getResponseMap().get(NullPaymentGatewayConstants.BILLING_CITY) != null) {
            hiddenFields.put(NullPaymentGatewayConstants.BILLING_CITY,
                responseDTO.getResponseMap().get(NullPaymentGatewayConstants.BILLING_CITY).toString());
        }

        if (responseDTO.getResponseMap().get(NullPaymentGatewayConstants.BILLING_STATE) != null) {
            hiddenFields.put(NullPaymentGatewayConstants.BILLING_STATE,
                responseDTO.getResponseMap().get(NullPaymentGatewayConstants.BILLING_STATE).toString());
        }

        if (responseDTO.getResponseMap().get(NullPaymentGatewayConstants.BILLING_ZIP) != null) {
            hiddenFields.put(NullPaymentGatewayConstants.BILLING_ZIP,
                responseDTO.getResponseMap().get(NullPaymentGatewayConstants.BILLING_ZIP).toString());
        }

        if (responseDTO.getResponseMap().get(NullPaymentGatewayConstants.BILLING_COUNTRY) != null) {
            hiddenFields.put(NullPaymentGatewayConstants.BILLING_COUNTRY,
                    responseDTO.getResponseMap().get(NullPaymentGatewayConstants.BILLING_COUNTRY).toString());
        }

        if (responseDTO.getResponseMap().get(NullPaymentGatewayConstants.SHIPPING_FIRST_NAME) != null) {
            hiddenFields.put(NullPaymentGatewayConstants.SHIPPING_FIRST_NAME,
                responseDTO.getResponseMap().get(NullPaymentGatewayConstants.SHIPPING_FIRST_NAME).toString());
        }

        if (responseDTO.getResponseMap().get(NullPaymentGatewayConstants.SHIPPING_LAST_NAME) != null) {
            hiddenFields.put(NullPaymentGatewayConstants.SHIPPING_LAST_NAME,
                responseDTO.getResponseMap().get(NullPaymentGatewayConstants.SHIPPING_LAST_NAME).toString());
        }

        if (responseDTO.getResponseMap().get(NullPaymentGatewayConstants.SHIPPING_ADDRESS_LINE1) != null) {
            hiddenFields.put(NullPaymentGatewayConstants.SHIPPING_ADDRESS_LINE1,
                responseDTO.getResponseMap().get(NullPaymentGatewayConstants.SHIPPING_ADDRESS_LINE1).toString());
        }

        if (responseDTO.getResponseMap().get(NullPaymentGatewayConstants.SHIPPING_ADDRESS_LINE2) != null) {
            hiddenFields.put(NullPaymentGatewayConstants.SHIPPING_ADDRESS_LINE2,
                responseDTO.getResponseMap().get(NullPaymentGatewayConstants.SHIPPING_ADDRESS_LINE2).toString());
        }

        if (responseDTO.getResponseMap().get(NullPaymentGatewayConstants.SHIPPING_CITY) != null) {
            hiddenFields.put(NullPaymentGatewayConstants.SHIPPING_CITY,
                responseDTO.getResponseMap().get(NullPaymentGatewayConstants.SHIPPING_CITY).toString());
        }

        if (responseDTO.getResponseMap().get(NullPaymentGatewayConstants.SHIPPING_STATE) != null) {
            hiddenFields.put(NullPaymentGatewayConstants.SHIPPING_STATE,
                responseDTO.getResponseMap().get(NullPaymentGatewayConstants.SHIPPING_STATE).toString());
        }

        if (responseDTO.getResponseMap().get(NullPaymentGatewayConstants.SHIPPING_ZIP) != null) {
            hiddenFields.put(NullPaymentGatewayConstants.SHIPPING_ZIP,
                responseDTO.getResponseMap().get(NullPaymentGatewayConstants.SHIPPING_ZIP).toString());
        }

        if (responseDTO.getResponseMap().get(NullPaymentGatewayConstants.SHIPPING_COUNTRY) != null) {
            hiddenFields.put(NullPaymentGatewayConstants.SHIPPING_COUNTRY,
                    responseDTO.getResponseMap().get(NullPaymentGatewayConstants.SHIPPING_COUNTRY).toString());
        }

        formParameters.put(getHiddenParamsKey(), hiddenFields);
    }
}
