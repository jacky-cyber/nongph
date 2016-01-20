package cn.globalph.b2c.payment.domain.wrapper;

import javax.annotation.Resource;

import cn.globalph.b2c.payment.domain.secure.BankAccountPayment;
import cn.globalph.b2c.payment.domain.secure.CreditCardPayment;
import cn.globalph.b2c.payment.domain.secure.GiftCardPayment;
import cn.globalph.b2c.payment.domain.secure.Referenced;
import cn.globalph.b2c.payment.domain.wrap.ReferencedWrap;
import cn.globalph.b2c.payment.service.SecureOrderPaymentService;
import cn.globalph.common.domain.wrapper.APIUnwrapper;
import cn.globalph.common.domain.wrapper.APIWrapper;
import cn.globalph.common.payment.PaymentType;

public class ReferencedWrapper implements APIWrapper<Referenced, ReferencedWrap>, APIUnwrapper<Referenced, ReferencedWrap>{
	
	@Resource(name="blSecureOrderPaymentService")
	private SecureOrderPaymentService securePaymentInfoService;

    @Override
    public ReferencedWrap wrapDetails(Referenced model) {
    	ReferencedWrap wrap = new ReferencedWrap();
    	wrap.setId( model.getId() );
    	wrap.setReferenceNumber( model.getReferenceNumber() );

        if (model instanceof CreditCardPayment) {
            CreditCardPayment referenced = (CreditCardPayment) model;
            wrap.setType( CreditCardPayment.class.getName() );

            wrap.setPan( referenced.getPan() );
            wrap.setCvvCode( referenced.getCvvCode() );
            wrap.setExpirationMonth( referenced.getExpirationMonth() );
            wrap.setExpirationYear( referenced.getExpirationYear() );
        }

        if (model instanceof BankAccountPayment) {
            BankAccountPayment referenced = (BankAccountPayment) model;
            wrap.setType( BankAccountPayment.class.getName() );
            wrap.setAccountNumber( referenced.getAccountNumber() );
            wrap.setRoutingNumber( referenced.getRoutingNumber() );
        }

        if (model instanceof GiftCardPayment) {
            GiftCardPayment referenced = (GiftCardPayment) model;
            wrap.setType( GiftCardPayment.class.getName() );
            wrap.setPan( referenced.getPan() );
            wrap.setPin( referenced.getPin() );
        }
        return wrap;
    }

    @Override
    public Referenced unwrap(ReferencedWrap wrap) {
        
        if (CreditCardPayment.class.getName().equals(wrap.getType())) {
            CreditCardPayment paymentInfo = (CreditCardPayment) securePaymentInfoService.create(PaymentType.CREDIT_CARD);
            paymentInfo.setId(wrap.getId());
            paymentInfo.setReferenceNumber(wrap.getReferenceNumber());
            paymentInfo.setPan(wrap.getPan());
            paymentInfo.setCvvCode(wrap.getCvvCode());
            paymentInfo.setExpirationMonth(wrap.getExpirationMonth());
            paymentInfo.setExpirationYear(wrap.getExpirationYear());

            return paymentInfo;
        }

        if (BankAccountPayment.class.getName().equals(wrap.getType())) {
            BankAccountPayment paymentInfo = (BankAccountPayment) securePaymentInfoService.create(PaymentType.BANK_ACCOUNT);
            paymentInfo.setId(wrap.getId());
            paymentInfo.setReferenceNumber(wrap.getReferenceNumber());
            paymentInfo.setAccountNumber(wrap.getAccountNumber());
            paymentInfo.setRoutingNumber(wrap.getRoutingNumber());

            return paymentInfo;
        }

        if (GiftCardPayment.class.getName().equals(wrap.getType())) {
            GiftCardPayment paymentInfo = (GiftCardPayment) securePaymentInfoService.create(PaymentType.GIFT_CARD);
            paymentInfo.setId(wrap.getId());
            paymentInfo.setReferenceNumber(wrap.getReferenceNumber());
            paymentInfo.setPan(wrap.getPan());
            paymentInfo.setPin(wrap.getPin());

            return paymentInfo;
        }

        return null;
    }

    @Override
    public ReferencedWrap wrapSummary(Referenced model) {
        return wrapDetails(model);
    }
}
