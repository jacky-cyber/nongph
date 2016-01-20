package cn.globalph.b2c.offer.service.discount.domain;

import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.common.money.Money;

public interface PromotableItemFactory {

    PromotableOrder createPromotableOrder(Order order, boolean includeOrderAndItemAdjustments);

    PromotableCandidateOrderOffer createPromotableCandidateOrderOffer(PromotableOrder promotableOrder, Offer offer);

    PromotableCandidateOrderOffer createPromotableCandidateOrderOffer(PromotableOrder promotableOrder,
            Offer offer, Money potentialSavings);

    PromotableOrderAdjustment createPromotableOrderAdjustment(
            PromotableCandidateOrderOffer promotableCandidateOrderOffer,
            PromotableOrder order);

    PromotableOrderAdjustment createPromotableOrderAdjustment(
            PromotableCandidateOrderOffer promotableCandidateOrderOffer,
            PromotableOrder order, Money value);

    PromotableOrderItem createPromotableOrderItem(OrderItem orderItem, PromotableOrder order,
            boolean includeAdjustments);

    PromotableOrderItemPriceDetail createPromotableOrderItemPriceDetail(PromotableOrderItem promotableOrderItem,
            int quantity);

    PromotableCandidateItemOffer createPromotableCandidateItemOffer(PromotableOrder promotableOrder, Offer offer);

    PromotableOrderItemPriceDetailAdjustment createPromotableOrderItemPriceDetailAdjustment(
            PromotableCandidateItemOffer promotableCandidateItemOffer,
            PromotableOrderItemPriceDetail promotableOrderItemPriceDetail);

    PromotableFulfillmentGroup createPromotableFulfillmentGroup(FulfillmentGroup fulfillmentGroup, PromotableOrder order);

    PromotableCandidateFulfillmentGroupOffer createPromotableCandidateFulfillmentGroupOffer(
            PromotableFulfillmentGroup fulfillmentGroup,
            Offer offer);
    
    PromotableFulfillmentGroupAdjustment createPromotableFulfillmentGroupAdjustment(
            PromotableCandidateFulfillmentGroupOffer promotableCandidateFulfillmentGroupOffer,
            PromotableFulfillmentGroup fulfillmentGroup);
}
