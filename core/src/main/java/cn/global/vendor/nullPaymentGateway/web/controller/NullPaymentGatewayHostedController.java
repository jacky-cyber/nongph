package cn.global.vendor.nullPaymentGateway.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.payment.dto.PaymentResponseDTO;
import cn.globalph.common.payment.service.PaymentGatewayConfiguration;
import cn.globalph.common.payment.service.PaymentGatewayWebResponseService;
import cn.globalph.common.vendor.service.exception.PaymentException;
import cn.globalph.common.web.payment.controller.PaymentGatewayAbstractController;
import cn.globalph.vendor.nullPaymentGateway.service.payment.NullPaymentGatewayConstants;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Elbert Bautista (elbertbautista)
 */
@Controller("blNullPaymentGatewayHostedController")
@RequestMapping("/" + NullPaymentGatewayHostedController.GATEWAY_CONTEXT_KEY)
public class NullPaymentGatewayHostedController extends PaymentGatewayAbstractController {

    protected static final Log LOG = LogFactory.getLog(NullPaymentGatewayHostedController.class);
    protected static final String GATEWAY_CONTEXT_KEY = "null-checkout";

    @Resource(name = "blNullPaymentGatewayHostedWebResponseService")
    protected PaymentGatewayWebResponseService paymentGatewayWebResponseService;

    @Resource(name = "blNullPaymentGatewayHostedConfiguration")
    protected PaymentGatewayConfiguration paymentGatewayConfiguration;

    @Override
    public void handleProcessingException(Exception e, RedirectAttributes redirectAttributes) throws PaymentException {
        if (LOG.isTraceEnabled()) {
            LOG.trace("A Processing Exception Occurred for " + GATEWAY_CONTEXT_KEY +
                    ". Adding Error to Redirect Attributes.");
        }

        redirectAttributes.addAttribute(PAYMENT_PROCESSING_ERROR, getProcessingErrorMessage());
    }

    @Override
    public void handleUnsuccessfulTransaction(Model model, RedirectAttributes redirectAttributes,
                                              PaymentResponseDTO responseDTO) throws PaymentException {
        if (LOG.isTraceEnabled()) {
            LOG.trace("The Transaction was unsuccessful for " + GATEWAY_CONTEXT_KEY +
                    ". Adding Errors to Redirect Attributes.");
        }
        redirectAttributes.addAttribute(PAYMENT_PROCESSING_ERROR,
                responseDTO.getResponseMap().get(NullPaymentGatewayConstants.RESULT_MESSAGE));
    }

    @Override
    public String getGatewayContextKey() {
        return GATEWAY_CONTEXT_KEY;
    }

    @Override
    public PaymentGatewayWebResponseService getWebResponseService() {
        return paymentGatewayWebResponseService;
    }

    @Override
    public PaymentGatewayConfiguration getConfiguration() {
        return paymentGatewayConfiguration;
    }

    @Override
    @RequestMapping(value = "/hosted/return", method = RequestMethod.GET)
    public String returnEndpoint(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes,
                                 Map<String, String> pathVars) throws PaymentException {
        return super.process(model, request, redirectAttributes);
    }

    @Override
    @RequestMapping(value = "/hosted/error", method = RequestMethod.GET)
    public String errorEndpoint(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes,
                                Map<String, String> pathVars) throws PaymentException {
        redirectAttributes.addAttribute(PAYMENT_PROCESSING_ERROR,
                request.getParameter(PAYMENT_PROCESSING_ERROR));
        return getOrderReviewRedirect();
    }
}
