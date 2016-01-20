package cn.globalph.b2c.offer.service.discount.domain;

import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.common.money.Money;

import org.springframework.stereotype.Service;

@Service("blPromotableItemFactory")
public class PromotableItemFactoryImpl implements PromotableItemFactory {

    public PromotableOrder createPromotableOrder(Order order, boolean includeOrderAndItemAdjustments) {
        return new PromotableOrderImpl(order, this, includeOrderAndItemAdjustments);
    }

    @Override
    public PromotableCandidateOrderOffer createPromotableCandidateOrderOffer(PromotableOrder promotableOrder, Offer offer) {
        return new PromotableCandidateOrderOfferImpl(promotableOrder, offer);
    }
    
    @Override
    public PromotableCandidateOrderOffer createPromotableCandidateOrderOffer(PromotableOrder promotableOrder,
            Offer offer, Money potentialSavings) {
        return new PromotableCandidateOrderOfferImpl(promotableOrder, offer, potentialSavings);
    }

    @Override
    public PromotableOrderAdjustment createPromotableOrderAdjustment(
            PromotableCandidateOrderOffer promotableCandidateOrderOffer, PromotableOrder order) {
        return new PromotableOrderAdjustmentImpl(promotableCandidateOrderOffer, order);
    }
    
    @Override
    public PromotableOrderAdjustment createPromotableOrderAdjustment(
            PromotableCandidateOrderOffer promotableCandidateOrderOffer,
            PromotableOrder order, Money adjustmentValue) {
        return new PromotableOrderAdjustmentImpl(promotableCandidateOrderOffer, order, adjustmentValue);
    }

    @Override
    public PromotableOrderItem createPromotableOrderItem(OrderItem orderItem, PromotableOrder order,
            boolean includeAdjustments) {
        return new PromotableOrderItemImpl(orderItem, order, this, includeAdjustments);
    }
    
    @Override
    public PromotableOrderItemPriceDetail createPromotableOrderItemPriceDetail(PromotableOrderItem promotableOrderItem,
            int quantity) {
        return new PromotableOrderItemPriceDetailImpl(promotableOrderItem, quantity);
    }

    @Override
    public PromotableCandidateItemOffer createPromotableCandidateItemOffer(PromotableOrder promotableOrder, Offer offer) {
        return new PromotableCandidateItemOfferImpl(promotableOrder, offer);
    }
    
    @Override
    public PromotableOrderItemPriceDetailAdjustment createPromotableOrderItemPriceDetailAdjustment(
            PromotableCandidateItemOffer promotableCandidateItemOffer,
            PromotableOrderItemPriceDetail orderItemPriceDetail) {
        return new PromotableOrderItemPriceDetailAdjustmentImpl(promotableCandidateItemOffer, orderItemPriceDetail);
    }
    
    @Override
    public PromotableFulfillmentGroup createPromotableFulfillmentGroup(
            FulfillmentGroup fulfillmentGroup,
            PromotableOrder order) {
        return new PromotableFulfillmentGroupImpl(fulfillmentGroup, order, this);
    }
    
    @Override
    public PromotableCandidateFulfillmentGroupOffer createPromotableCandidateFulfillmentGroupOffer(
            PromotableFulfillmentGroup fulfillmentGroup, Offer offer) {
        return new PromotableCandidateFulfillmentGroupOfferImpl(fulfillmentGroup, offer);
    }
    
    @Override
    public PromotableFulfillmentGroupAdjustment createPromotableFulfillmentGroupAdjustment(
            PromotableCandidateFulfillmentGroupOffer promotableCandidateFulfillmentGroupOffer,
            PromotableFulfillmentGroup fulfillmentGroup) {
        return new PromotableFulfillmentGroupAdjustmentImpl(promotableCandidateFulfillmentGroupOffer, fulfillmentGroup);
    }
}
