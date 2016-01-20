package cn.globalph.b2c.payment.domain.wrapper;

import cn.globalph.b2c.payment.domain.PaymentTransaction;
import cn.globalph.b2c.payment.domain.wrap.MapElementWrap;
import cn.globalph.b2c.payment.domain.wrap.PaymentTransactionWrap;
import cn.globalph.b2c.payment.service.OrderPaymentService;
import cn.globalph.common.domain.wrapper.APIUnwrapper;
import cn.globalph.common.domain.wrapper.APIWrapper;
import cn.globalph.common.money.Money;
import cn.globalph.common.payment.PaymentTransactionType;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap; 
import java.util.List;
import java.util.Map;

public class PaymentTransactionWrapper implements APIWrapper<PaymentTransaction, PaymentTransactionWrap>, APIUnwrapper<PaymentTransaction, PaymentTransactionWrap> {
	@Resource(name="blOrderPaymentService")
	OrderPaymentService orderPaymentService;
	
    @Override
    public PaymentTransactionWrap wrapDetails(PaymentTransaction model) {
    	PaymentTransactionWrap wrap = new PaymentTransactionWrap();
    	wrap.setId( model.getId() );

        if (model.getOrderPayment() != null) {
            wrap.setOrderPaymentId( model.getOrderPayment().getId() );
        }

        if (model.getParentTransaction() != null) {
            wrap.setParentTransactionId( model.getParentTransaction().getId() );
        }

        if (model.getType() != null) {
            wrap.setType( model.getType().getType() );
        }

        wrap.setCustomerIpAddress( model.getCustomerIpAddress() );
        wrap.setRawResponse( model.getRawResponse() );
        wrap.setSuccess( model.getSuccess() );

        if (model.getAmount() != null) {
            wrap.setAmount( model.getAmount().getAmount() );
        }

        if (model.getAdditionalFields() != null && !model.getAdditionalFields().isEmpty()) {
            List<MapElementWrap> mapElementWraps = new ArrayList<MapElementWrap>();
            for (String key : model.getAdditionalFields().keySet()) {
                MapElementWrap mapElementWrap = new MapElementWrap();
                mapElementWrap.setKey(key);
                mapElementWrap.setValue(model.getAdditionalFields().get(key));
                mapElementWraps.add(mapElementWrap);
            }
            wrap.setAdditionalFields( mapElementWraps );
        }
        return wrap;
    }

    @Override
    public PaymentTransactionWrap wrapSummary(PaymentTransaction model) {
        return wrapDetails(model);
    }

    @Override
    public PaymentTransaction unwrap(PaymentTransactionWrap wrap) {
        
        PaymentTransaction transaction = orderPaymentService.createTransaction();

        if (wrap.getParentTransactionId() != null) {
            PaymentTransaction parentTransaction = orderPaymentService.readTransactionById(wrap.getParentTransactionId());
            transaction.setParentTransaction(parentTransaction);
        }

        transaction.setType(PaymentTransactionType.getInstance(wrap.getType()));

        if (wrap.getAdditionalFields() != null && !wrap.getAdditionalFields().isEmpty()) {
            Map<String, String> fields = new HashMap<String, String>();
            for (MapElementWrap mapElementWrapper : wrap.getAdditionalFields()) {
                fields.put(mapElementWrapper.getKey(), mapElementWrapper.getValue());
            	}

            transaction.setAdditionalFields(fields);
           }

        if (wrap.getAmount() != null) {
            transaction.setAmount(new Money(wrap.getAmount()));
           }

        transaction.setCustomerIpAddress(wrap.getCustomerIpAddress());
        transaction.setRawResponse(wrap.getRawResponse());
        transaction.setSuccess(wrap.getSuccess());

        return transaction;
    }
}
