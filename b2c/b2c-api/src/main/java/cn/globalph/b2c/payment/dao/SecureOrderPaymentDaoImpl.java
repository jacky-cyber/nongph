package cn.globalph.b2c.payment.dao;

import cn.globalph.b2c.payment.domain.secure.BankAccountPayment;
import cn.globalph.b2c.payment.domain.secure.CreditCardPayment;
import cn.globalph.b2c.payment.domain.secure.GiftCardPayment;
import cn.globalph.b2c.payment.domain.secure.Referenced;
import cn.globalph.common.encryption.EncryptionModule;
import cn.globalph.common.persistence.EntityConfiguration;

import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

@Repository("blSecureOrderPaymentDao")
public class SecureOrderPaymentDaoImpl implements SecureOrderPaymentDao {

    @PersistenceContext(unitName = "blSecurePU")
    protected EntityManager em;

    @Resource(name = "blEncryptionModule")
    protected EncryptionModule encryptionModule;

    @Resource(name = "blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    public Referenced save(Referenced securePaymentInfo) {
        return em.merge(securePaymentInfo);
    }

    public BankAccountPayment createBankAccountPayment() {
        BankAccountPayment response = (BankAccountPayment) entityConfiguration.createEntityInstance(" cn.globalph.b2c.payment.domain.BankAccountPaymentInfo");
        response.setEncryptionModule(encryptionModule);
        return response;
    }

    public GiftCardPayment createGiftCardPayment() {
        GiftCardPayment response = (GiftCardPayment) entityConfiguration.createEntityInstance(" cn.globalph.b2c.payment.domain.GiftCardPaymentInfo");
        response.setEncryptionModule(encryptionModule);
        return response;
    }

    public CreditCardPayment createCreditCardPayment() {
        CreditCardPayment response = (CreditCardPayment) entityConfiguration.createEntityInstance(" cn.globalph.b2c.payment.domain.CreditCardPaymentInfo");
        response.setEncryptionModule(encryptionModule);
        return response;
    }

    @SuppressWarnings("unchecked")
    public BankAccountPayment findBankAccountPayment(String referenceNumber) {
        Query query = em.createNamedQuery("BC_READ_BANK_ACCOUNT_BY_REFERENCE_NUMBER");
        query.setParameter("referenceNumber", referenceNumber);
        List<BankAccountPayment> infos = query.getResultList();
        BankAccountPayment response = (infos == null || infos.size() == 0) ? null : infos.get(0);
        if (response != null) {
            response.setEncryptionModule(encryptionModule);
        }
        return response;
    }

    @SuppressWarnings("unchecked")
    public CreditCardPayment findCreditCardPayment(String referenceNumber) {
        Query query = em.createNamedQuery("BC_READ_CREDIT_CARD_BY_REFERENCE_NUMBER");
        query.setParameter("referenceNumber", referenceNumber);
        List<CreditCardPayment> infos = query.getResultList();
        CreditCardPayment response = (infos == null || infos.size() == 0) ? null : infos.get(0);
        if (response != null) {
            response.setEncryptionModule(encryptionModule);
        }
        return response;
    }

    @SuppressWarnings("unchecked")
    public GiftCardPayment findGiftCardPayment(String referenceNumber) {
        Query query = em.createNamedQuery("BC_READ_GIFT_CARD_BY_REFERENCE_NUMBER");
        query.setParameter("referenceNumber", referenceNumber);
        List<GiftCardPayment> infos = query.getResultList();
        GiftCardPayment response = (infos == null || infos.size() == 0) ? null : infos.get(0);
        if (response != null) {
            response.setEncryptionModule(encryptionModule);
        }
        return response;
    }

    public void delete(Referenced securePayment) {
        if (!em.contains(securePayment)) {
            securePayment = em.find(securePayment.getClass(), securePayment.getId());
        }
        em.remove(securePayment);
    }

}
