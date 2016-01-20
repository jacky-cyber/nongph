package cn.globalph.b2c.offer.service;

import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.b2c.offer.service.discount.domain.PromotableCandidateItemOffer;
import cn.globalph.b2c.offer.service.discount.domain.PromotableOrder;
import cn.globalph.b2c.offer.service.discount.domain.PromotableOrderItem;
import cn.globalph.b2c.order.domain.OrderItemPriceDetail;
import cn.globalph.common.extension.AbstractExtensionHandler;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;

import java.util.List;
import java.util.Map;


public class AbstractOfferServiceExtensionHandler extends AbstractExtensionHandler implements OfferServiceExtensionHandler {
    
    @Override
    public ExtensionResultStatusType applyAdditionalFilters(List<Offer> offers) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType calculatePotentialSavings(PromotableCandidateItemOffer itemOffer,
            PromotableOrderItem item, int quantity, Map<String, Object> contextMap) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType resetPriceDetails(PromotableOrderItem item) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType applyItemOffer(PromotableOrder order, PromotableCandidateItemOffer itemOffer,
            Map<String, Object> contextMap) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType synchronizeAdjustmentsAndPrices(PromotableOrder order) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType chooseSaleOrRetailAdjustments(PromotableOrder order) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType createOrderItemPriceDetailAdjustment(ExtensionResultHolder resultHolder,
            OrderItemPriceDetail itemDetail) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

}
