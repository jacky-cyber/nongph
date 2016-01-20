package cn.globalph.b2c.payment.service;

import cn.globalph.b2c.payment.dao.SecureOrderPaymentDao;
import cn.globalph.b2c.payment.domain.secure.BankAccountPayment;
import cn.globalph.b2c.payment.domain.secure.CreditCardPayment;
import cn.globalph.b2c.payment.domain.secure.GiftCardPayment;
import cn.globalph.b2c.payment.domain.secure.Referenced;
import cn.globalph.b2c.workflow.WorkflowException;
import cn.globalph.common.payment.PaymentType;
import cn.globalph.common.util.TransactionUtils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Acquisition of Primary Account Number (PAN) and other sensitive information
 * is retrieved through a service separate from the order. This conceptual
 * separation facilitates the physical separation of this sensitive data from
 * the order. As a result, implementors may host sensitive user account
 * information in a datastore separate from the datastore housing the order.
 * This measure goes towards achieving a PCI compliant architecture.
 * @felix.wu
 */
@Service("blSecureOrderPaymentService")
public class SecureOrderPaymentServiceImpl implements SecureOrderPaymentService {

    @Resource(name = "blSecureOrderPaymentDao")
    protected SecureOrderPaymentDao securePaymentInfoDao;

    @Override
    @Transactional(TransactionUtils.SECURE_TRANSACTION_MANAGER)
    public Referenced save(Referenced securePaymentInfo) {
        return securePaymentInfoDao.save(securePaymentInfo);
    }

    @Override
    public Referenced create(PaymentType paymentType) {
        if (paymentType.equals(PaymentType.CREDIT_CARD)) {
            CreditCardPayment ccinfo = securePaymentInfoDao.createCreditCardPayment();
            return ccinfo;
        } else if (paymentType.equals(PaymentType.BANK_ACCOUNT)) {
            BankAccountPayment bankinfo = securePaymentInfoDao.createBankAccountPayment();
            return bankinfo;
        } else if (paymentType.equals(PaymentType.GIFT_CARD)) {
            GiftCardPayment gcinfo = securePaymentInfoDao.createGiftCardPayment();
            return gcinfo;
        }

        return null;
    }

    @Override
    public Referenced findSecurePaymentInfo(String referenceNumber, PaymentType paymentType) throws WorkflowException {
        if (paymentType == PaymentType.CREDIT_CARD) {
            CreditCardPayment ccinfo = findCreditCardInfo(referenceNumber);
            if (ccinfo == null) {
                throw new WorkflowException("No credit card info associated with credit card payment type with reference number: " + referenceNumber);
            }
            return ccinfo;
        } else if (paymentType == PaymentType.BANK_ACCOUNT) {
            BankAccountPayment bankinfo = findBankAccountInfo(referenceNumber);
            if (bankinfo == null) {
                throw new WorkflowException("No bank account info associated with bank account payment type with reference number: " + referenceNumber);
            }
            return bankinfo;
        } else if (paymentType == PaymentType.GIFT_CARD) {
            GiftCardPayment gcinfo = findGiftCardInfo(referenceNumber);
            if (gcinfo == null) {
                throw new WorkflowException("No bank account info associated with gift card payment type with reference number: " + referenceNumber);
            }
            return gcinfo;
        }

        return null;
    }

    @Override
    @Transactional(TransactionUtils.SECURE_TRANSACTION_MANAGER)
    public void findAndRemoveSecurePaymentInfo(String referenceNumber, PaymentType paymentType) throws WorkflowException {
        Referenced referenced = findSecurePaymentInfo(referenceNumber, paymentType);
        if (referenced != null) {
            remove(referenced);
        }

    }

    @Override
    @Transactional(TransactionUtils.SECURE_TRANSACTION_MANAGER)
    public void remove(Referenced securePaymentInfo) {
        securePaymentInfoDao.delete(securePaymentInfo);
    }

    protected BankAccountPayment findBankAccountInfo(String referenceNumber) {
        return securePaymentInfoDao.findBankAccountPayment(referenceNumber);
    }

    protected CreditCardPayment findCreditCardInfo(String referenceNumber) {
        return securePaymentInfoDao.findCreditCardPayment(referenceNumber);
    }

    protected GiftCardPayment findGiftCardInfo(String referenceNumber) {
        return securePaymentInfoDao.findGiftCardPayment(referenceNumber);
    }
}
