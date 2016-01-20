package cn.globalph.b2c.payment.dao;

import cn.globalph.b2c.payment.domain.secure.BankAccountPayment;
import cn.globalph.b2c.payment.domain.secure.CreditCardPayment;
import cn.globalph.b2c.payment.domain.secure.GiftCardPayment;
import cn.globalph.b2c.payment.domain.secure.Referenced;

public interface SecureOrderPaymentDao {

    public BankAccountPayment findBankAccountPayment(String referenceNumber);

    public CreditCardPayment findCreditCardPayment(String referenceNumber);

    public GiftCardPayment findGiftCardPayment(String referenceNumber);

    public Referenced save(Referenced securePaymentInfo);

    public BankAccountPayment createBankAccountPayment();

    public GiftCardPayment createGiftCardPayment();

    public CreditCardPayment createCreditCardPayment();

    public void delete(Referenced securePayment);

}
