package cn.globalph.b2c.payment.domain.wrap;

import cn.globalph.common.util.xml.BigDecimalRoundingAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.math.BigDecimal;
import java.util.List;

@XmlRootElement(name = "transaction")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PaymentTransactionWrap{

    @XmlElement
    protected Long id;

    @XmlElement
    protected Long orderPaymentId;

    @XmlElement
    protected Long parentTransactionId;

    @XmlElement
    protected String type;

    @XmlElement
    protected String customerIpAddress;

    @XmlElement
    protected String rawResponse;

    @XmlElement
    protected Boolean success;

    @XmlElement
    @XmlJavaTypeAdapter(value = BigDecimalRoundingAdapter.class)
    protected BigDecimal amount;

    @XmlElement(name = "element")
    @XmlElementWrapper(name = "additionalFields")
    protected List<MapElementWrap> additionalFields;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderPaymentId() {
		return orderPaymentId;
	}

	public void setOrderPaymentId(Long orderPaymentId) {
		this.orderPaymentId = orderPaymentId;
	}

	public Long getParentTransactionId() {
		return parentTransactionId;
	}

	public void setParentTransactionId(Long parentTransactionId) {
		this.parentTransactionId = parentTransactionId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCustomerIpAddress() {
		return customerIpAddress;
	}

	public void setCustomerIpAddress(String customerIpAddress) {
		this.customerIpAddress = customerIpAddress;
	}

	public String getRawResponse() {
		return rawResponse;
	}

	public void setRawResponse(String rawResponse) {
		this.rawResponse = rawResponse;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public List<MapElementWrap> getAdditionalFields() {
		return additionalFields;
	}

	public void setAdditionalFields(List<MapElementWrap> additionalFields) {
		this.additionalFields = additionalFields;
	}
}
