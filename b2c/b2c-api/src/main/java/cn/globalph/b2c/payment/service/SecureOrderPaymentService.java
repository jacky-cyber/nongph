package cn.globalph.b2c.payment.service;

import cn.globalph.b2c.payment.domain.secure.Referenced;
import cn.globalph.b2c.workflow.WorkflowException;
import cn.globalph.common.payment.PaymentType;

public interface SecureOrderPaymentService {

    public Referenced findSecurePaymentInfo(String referenceNumber, PaymentType paymentType) throws WorkflowException;

    public Referenced save(Referenced securePayment);

    public Referenced create(PaymentType paymentType);

    public void remove(Referenced securePayment);

    public void findAndRemoveSecurePaymentInfo(String referenceNumber, PaymentType paymentType) throws WorkflowException;

}
