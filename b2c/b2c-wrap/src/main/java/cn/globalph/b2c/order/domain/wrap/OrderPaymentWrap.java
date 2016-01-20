package cn.globalph.b2c.order.domain.wrap;

import cn.globalph.b2c.payment.domain.wrap.PaymentTransactionWrap;
import cn.globalph.common.util.xml.BigDecimalRoundingAdapter;
import cn.globalph.passort.domain.wrap.AddressWrap;





import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.math.BigDecimal;
import java.util.List;

@XmlRootElement(name = "payment")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class OrderPaymentWrap{

    @XmlElement
    protected Long id;

    @XmlElement
    protected Long orderId;

    @XmlElement
    protected String type;

    @XmlElement
    protected AddressWrap billingAddress;

    @XmlElement
    @XmlJavaTypeAdapter(value = BigDecimalRoundingAdapter.class)
    protected BigDecimal amount;

    @XmlElement
    protected String referenceNumber;

    @XmlElement
    protected String gatewayType;

    @XmlElement(name = "transaction")
    @XmlElementWrapper(name = "transactions")
    protected List<PaymentTransactionWrap> transactions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public AddressWrap getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(AddressWrap billingAddress) {
		this.billingAddress = billingAddress;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getGatewayType() {
		return gatewayType;
	}

	public void setGatewayType(String gatewayType) {
		this.gatewayType = gatewayType;
	}

	public List<PaymentTransactionWrap> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<PaymentTransactionWrap> transactions) {
		this.transactions = transactions;
	}
}
