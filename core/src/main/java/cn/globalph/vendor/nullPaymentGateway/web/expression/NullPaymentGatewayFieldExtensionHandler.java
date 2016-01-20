package cn.globalph.vendor.nullPaymentGateway.web.expression;

import cn.globalph.common.web.payment.expression.AbstractPaymentGatewayFieldExtensionHandler;
import cn.globalph.common.web.payment.expression.PaymentGatewayFieldExtensionManager;
import cn.globalph.vendor.nullPaymentGateway.service.payment.NullPaymentGatewayConstants;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * This sample handler will add itself to the {@link PaymentGatewayFieldExtensionManager}
 * and will output the input NAMEs that are required by the
 * {@link cn.global.vendor.nullPaymentGateway.web.controller.NullPaymentGatewayProcessorController}
 *
 * Note, we don't want this loaded into the extension manager
 * if a real payment gateway is used, so make sure to not scan this class when
 * using a real implementation.
 *
 * In order to use this sample extension handler, you will need to component scan
 * the package "cn.globalph".
 *
 * This should NOT be used in production, and is meant solely for demonstration
 * purposes only.
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Service("blNullPaymentGatewayFieldExtensionHandler")
public class NullPaymentGatewayFieldExtensionHandler extends AbstractPaymentGatewayFieldExtensionHandler {

    @Resource(name = "blPaymentGatewayFieldExtensionManager")
    protected PaymentGatewayFieldExtensionManager extensionManager;

    @PostConstruct
    public void init() {
        if (isEnabled()) {
            extensionManager.registerHandler(this);
        }
    }

    @Override
    public String getCreditCardHolderName() {
        return NullPaymentGatewayConstants.CREDIT_CARD_NAME;
    }

    @Override
    public String getCreditCardType() {
        return null;
    }

    @Override
    public String getCreditCardNum() {
        return NullPaymentGatewayConstants.CREDIT_CARD_NUMBER;
    }

    @Override
    public String getCreditCardExpDate() {
        return NullPaymentGatewayConstants.CREDIT_CARD_EXP_DATE;
    }

    @Override
    public String getCreditCardExpMonth() {
        return null;
    }

    @Override
    public String getCreditCardExpYear() {
        return null;
    }

    @Override
    public String getCreditCardCvv() {
        return NullPaymentGatewayConstants.CREDIT_CARD_CVV;
    }

    @Override
    public String getBillToAddressFirstName() {
        return null;
    }

    @Override
    public String getBillToAddressLastName() {
        return null;
    }

    @Override
    public String getBillToAddressCompanyName() {
        return null;
    }

    @Override
    public String getBillToAddressLine1() {
        return null;
    }

    @Override
    public String getBillToAddressLine2() {
        return null;
    }

    @Override
    public String getBillToAddressCityLocality() {
        return null;
    }

    @Override
    public String getBillToAddressStateRegion() {
        return null;
    }

    @Override
    public String getBillToAddressPostalCode() {
        return null;
    }

    @Override
    public String getBillToAddressCountryCode() {
        return null;
    }

    @Override
    public String getBillToAddressPhone() {
        return null;
    }

    @Override
    public String getBillToAddressEmail() {
        return null;
    }

    @Override
    public String getShipToAddressFirstName() {
        return null;
    }

    @Override
    public String getShipToAddressLastName() {
        return null;
    }

    @Override
    public String getShipToAddressCompanyName() {
        return null;
    }

    @Override
    public String getShipToAddressLine1() {
        return null;
    }

    @Override
    public String getShipToAddressLine2() {
        return null;
    }

    @Override
    public String getShipToAddressCityLocality() {
        return null;
    }

    @Override
    public String getShipToAddressStateRegion() {
        return null;
    }

    @Override
    public String getShipToAddressPostalCode() {
        return null;
    }

    @Override
    public String getShipToAddressCountryCode() {
        return null;
    }

    @Override
    public String getShipToAddressPhone() {
        return null;
    }

    @Override
    public String getShipToAddressEmail() {
        return null;
    }
}
