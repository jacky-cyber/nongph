package cn.globalph.b2c.order.domain;

import cn.globalph.b2c.coupon.domain.CouponCode;
import cn.globalph.b2c.offer.domain.CandidateOrderOffer;
import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.b2c.offer.domain.OfferInfo;
import cn.globalph.b2c.offer.domain.OrderAdjustment;
import cn.globalph.b2c.order.service.call.ActivityMessageDTO;
import cn.globalph.b2c.order.service.type.OrderStatus;
import cn.globalph.b2c.payment.domain.OrderPayment;
import cn.globalph.b2c.product.domain.GroupOn;
import cn.globalph.b2c.product.domain.Provider;
import cn.globalph.common.audit.Auditable;
import cn.globalph.common.money.Money;
import cn.globalph.coupon.domain.CustomerCoupon;
import cn.globalph.passport.domain.Customer;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * NullOrderImpl is a class that represents an unmodifiable, empty order. This class is used as the default order
 * for a customer. It is a shared class between customers, and serves as a placeholder order until an item 
 * is initially added to cart, at which point a real Order gets created. This prevents creating individual orders
 * for customers that are just browsing the site.
 */
public class NullOrderImpl implements Order {

    private static final long serialVersionUID = 1L;

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public void setId(Long id) {
        throw new UnsupportedOperationException("NullOrder does not support any modification operations.");
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {
        throw new UnsupportedOperationException("NullOrder does not support any modification operations.");
    }

    @Override
    public Auditable getAuditable() {
        return null;
    }

    @Override
    public void setAuditable(Auditable auditable) {
        throw new UnsupportedOperationException("NullOrder does not support any modification operations.");
    }

    @Override
    public Money getSubTotal() {
        return Money.ZERO;
    }

    @Override
    public void setSubTotal(Money subTotal) {
        throw new UnsupportedOperationException("NullOrder does not support any modification operations.");
    }

    @Override
    public void assignOrderItemsFinalPrice() {
        throw new UnsupportedOperationException("NullOrder does not support any modification operations.");
    }

    @Override
    public Money getTotal() {
        return null;
    }

    @Override
    public Money getTotalAfterAppliedPayments() {
        return null;
    }

    @Override
    public void setTotal(Money orderTotal) {
        throw new UnsupportedOperationException("NullOrder does not support any modification operations.");
    }

    @Override
    public Customer getCustomer() {
        return null;
    }

    @Override
    public void setCustomer(Customer customer) {
        throw new UnsupportedOperationException("NullOrder does not support any modification operations.");
    }

    @Override
    public OrderAddress getOrderAddress() {
        return null;
    }

    @Override
    public void setOrderAddress(OrderAddress orderAddress) {
        throw new UnsupportedOperationException("NullOrder does not support any modification operations.");
    }

    @Override
    public OrderStatus getStatus() {
        return null;
    }

    @Override
    public void setStatus(OrderStatus status) {
        throw new UnsupportedOperationException("NullOrder does not support any modification operations.");
    }

    @Override
    public List<OrderItem> getOrderItems() {
        return null;
    }

    @Override
    public void setOrderItems(List<OrderItem> orderItems) {
        throw new UnsupportedOperationException("NullOrder does not support any modification operations.");
    }

    @Override
    public void addOrderItem(OrderItem orderItem) {
        throw new UnsupportedOperationException("NullOrder does not support any modification operations.");
    }

    @Override
    public List<FulfillmentGroup> getFulfillmentGroups() {
        return null;
    }

    @Override
    public void setFulfillmentGroups(List<FulfillmentGroup> fulfillmentGroups) {
        throw new UnsupportedOperationException("NullOrder does not support any modification operations."); 
    }

    @Override
    public void setCandidateOrderOffers(List<CandidateOrderOffer> candidateOrderOffers) {
        throw new UnsupportedOperationException("NullOrder does not support any modification operations.");
    }

    @Override
    public List<CandidateOrderOffer> getCandidateOrderOffers() {
        return null;
    }

    @Override
    public Date getSubmitDate() {
        return null;
    }

    @Override
    public void setSubmitDate(Date submitDate) {
        throw new UnsupportedOperationException("NullOrder does not support any modification operations.");
    }

    @Override
    public Date getConfirmDate() {
        return null;
    }

    @Override
    public void setConfirmDate(Date confirmDate) {
        throw new UnsupportedOperationException("NullOrder does not support any modification operations.");
    }

    @Override
    public List<OrderPayment> getPayments() {
        return null;
    }

    @Override
    public void setPayments(List<OrderPayment> paymentInfos) {
        throw new UnsupportedOperationException("NullOrder does not support any modification operations.");
    }

    @Override
    public List<OrderAdjustment> getOrderAdjustments() {
        return null;
    }
    
    @Override
    public boolean containsSku(Long skuId) {
        return false;
    }

    @Override
    public String getFulfillmentStatus() {
        return null;
    }

    @Override
    public String getOrderNumber() {
        return null;
    }

    @Override
    public void setOrderNumber(String orderNumber) {
        throw new UnsupportedOperationException("NullOrder does not support any modification operations.");
    }

    @Override
    public Map<Offer, OfferInfo> getAdditionalOfferInformation() {
        return null;
    }

    @Override
    public void setAdditionalOfferInformation(Map<Offer, OfferInfo> additionalOfferInformation) {
        throw new UnsupportedOperationException("NullOrder does not support any modification operations.");
    }

    @Override
    public Money getItemAdjustmentsValue() {
        return null;
    }

    @Override
    public Money getOrderAdjustmentsValue() {
        return Money.ZERO;
    }

    @Override
    public Money getTotalAdjustmentsValue() {
        return null;
    }

    @Override
    public boolean updatePrices() {
        throw new UnsupportedOperationException("NullOrder does not support any modification operations.");
    }

    @Override
    public Money getFulfillmentGroupAdjustmentsValue() {
        return null;
    }
    
//    @Override
//    public Map<String, OrderAttribute> getOrderAttributes() {
//        return null;
//    }
//
//    @Override
//    public void setOrderAttributes(Map<String, OrderAttribute> orderAttributes) {
//        throw new UnsupportedOperationException("NullOrder does not support any modification operations.");
//    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public Money calculateSubTotal() {
        throw new UnsupportedOperationException("NullOrder does not support any modification operations.");
    }

    @Override
    public Money getTotalFulfillmentCharges() {
        return null;
    }

    @Override
    public void setTotalFulfillmentCharges(Money totalFulfillmentCharges) {
        throw new UnsupportedOperationException("NullOrder does not support any modification operations.");
    }

    @Override
    public boolean finalizeItemPrices() {
        throw new UnsupportedOperationException("NullOrder does not support any modification operations.");
    }

    @Override
    public boolean getHasOrderAdjustments() {
        return false;
    }

    @Override
    public List<ActivityMessageDTO> getOrderMessages() {
        return null;
    }

    @Override
    public void setOrderMessages(List<ActivityMessageDTO> orderMessages) {
        throw new UnsupportedOperationException("NullOrder does not support any modification operations.");
    }
    
    public int getUsedItemsNum(){
    	return 0;
    }

	@Override
	public CustomerCoupon getApplyCoupon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setApplyCoupon(CustomerCoupon customerCoupon) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<OrderItem> getUsedOrderItems() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Money getCouponDiscount() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void setCouponDiscount(Money couponDiscount){
		 
	 }

	@Override
	public boolean isReview() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setReview(boolean review) {
		// TODO Auto-generated method stub
		
	}

    @Override
    public Boolean isDeliveryInfoSent() {
        return null;
    }

    @Override
    public void setDeliveryInfoSent(Boolean isDeliveryInfoSent) {

    }

    @Override
    public Boolean isShipped() {
        return null;
    }

    @Override
    public void setShipped(Boolean isShipped) {

    }

    @Override
    public Order getOrder() {
        return null;
    }

    @Override
    public void setOrder(Order order) {

    }

    @Override
    public List<Order> getSuborderList() {
        return null;
    }

    @Override
    public void setSuborderList(List<Order> suborderList) {

    }

    @Override
    public List<OrderLog> getOrderLogs() {
        return null;
    }

    @Override
    public void setOrderLogs(List<OrderLog> orderLogs) {

    }

    @Override
    public String getDeliveryType() {
        return null;
    }

    @Override
    public void setDeliveryType(String deliveryType) {

    }

    @Override
    public Money getPickupOff() {
        return null;
    }

    @Override
    public void setPickupOff(Money money) {

    }

    @Override
    public Provider getProvider() {
        return null;
    }

    @Override
    public void setProvider(Provider provider) {

    }

    @Override
    public Boolean isRefundAvailable() {
		return null;
	}

	@Override
	public Boolean getHasRefundItem() {
		return null;
	}

	@Override
	public Boolean getIsGroupOn() {
		return null;
	}

	@Override
	public void setIsGroupOn(Boolean isGroupOn) {
		
	}

	@Override
	public GroupOn getGroupOn() {
		return null;
	}

	@Override
	public void setGroupOn(GroupOn groupOn) {
		
	}

	@Override
	public boolean isCouponAvailable() {
		return false;
	}

	@Override
	public boolean getNeedAddress() {
		return false;
	}

	@Override
	public CouponCode getCouponCode() {
		return null;
	}

	@Override
	public void setCouponCode(CouponCode couponCode) {
		
	}

    /*jenny add s*/
    @Override
    public void setRemark(String remark) {

    }

    @Override
    public String getRemark() {
        return null;
    }

    @Override
    public void setDeductionBonusPoint(Integer deductionBonusPoint) {

    }

    @Override
    public Integer getDeductionBonusPoint() {
        return null;
    }

    /*end*/
    @Override
	public Money getCouponCodeDiscount() {
		return null;
	}

	@Override
	public void setCouponCodeDiscount(Money couponCodeDiscount) {
		
	}

	@Override
	public boolean isCouponCodeAvailable() {
		return false;
	}

	@Override
	public Date getCompleteDate() {
		return null;
	}

	@Override
	public void setCompleteDate(Date completeDate) {
		
	}

	@Override
	public void setRefund(Refund refund) {
		
	}

	@Override
	public Refund getRefund() {
		return null;
	}
	
	@Override
	public Boolean isRefundOrderAvailable(){
		return false;
	}
	
}
