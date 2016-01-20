package cn.globalph.b2c.offer.service.discount.domain;

import cn.globalph.b2c.offer.domain.OfferItemCriteria;
import cn.globalph.b2c.offer.service.discount.PromotionDiscount;
import cn.globalph.b2c.offer.service.discount.PromotionQualifier;
import cn.globalph.common.money.Money;

import java.util.List;

/**
 * Modules that extend offer engine behavior can benefit from a wrapped PromotableOrderItemPriceDetail.
 * 
 * @author bpolster
 *
 */
public class PromotableOrderItemPriceDetailWrapper implements PromotableOrderItemPriceDetail {

    private PromotableOrderItemPriceDetail detail;

    public PromotableOrderItemPriceDetailWrapper(PromotableOrderItemPriceDetail wrappedDetail) {
        detail = wrappedDetail;
    }

    public void addCandidateItemPriceDetailAdjustment(PromotableOrderItemPriceDetailAdjustment itemAdjustment) {
        detail.addCandidateItemPriceDetailAdjustment(itemAdjustment);
    }

    public List<PromotableOrderItemPriceDetailAdjustment> getCandidateItemAdjustments() {
        return detail.getCandidateItemAdjustments();
    }

    public boolean hasNonCombinableAdjustments() {
        return detail.hasNonCombinableAdjustments();
    }

    public boolean isTotalitarianOfferApplied() {
        return detail.isTotalitarianOfferApplied();
    }

    public boolean isNonCombinableOfferApplied() {
        return detail.isNonCombinableOfferApplied();
    }

    public void chooseSaleOrRetailAdjustments() {
        detail.chooseSaleOrRetailAdjustments();
    }

    public void removeAllAdjustments() {
        detail.removeAllAdjustments();
    }

    public List<PromotionDiscount> getPromotionDiscounts() {
        return detail.getPromotionDiscounts();
    }

    public List<PromotionQualifier> getPromotionQualifiers() {
        return detail.getPromotionQualifiers();
    }

    public int getQuantity() {
        return detail.getQuantity();
    }

    public void setQuantity(int quantity) {
        detail.setQuantity(quantity);
    }

    public PromotableOrderItem getPromotableOrderItem() {
        return detail.getPromotableOrderItem();
    }

    public int getQuantityAvailableToBeUsedAsQualifier(PromotableCandidateItemOffer itemOffer) {
        return detail.getQuantityAvailableToBeUsedAsQualifier(itemOffer);
    }

    public int getQuantityAvailableToBeUsedAsTarget(PromotableCandidateItemOffer itemOffer) {
        return detail.getQuantityAvailableToBeUsedAsTarget(itemOffer);
    }

    public PromotionQualifier addPromotionQualifier(PromotableCandidateItemOffer itemOffer, OfferItemCriteria itemCriteria, int qtyToMarkAsQualifier) {
        return detail.addPromotionQualifier(itemOffer, itemCriteria, qtyToMarkAsQualifier);
    }

    public void addPromotionDiscount(PromotableCandidateItemOffer itemOffer, OfferItemCriteria itemCriteria, int qtyToMarkAsTarget) {
        detail.addPromotionDiscount(itemOffer, itemCriteria, qtyToMarkAsTarget);
    }

    public Money calculateItemUnitPriceWithAdjustments(boolean allowSalePrice) {
        return detail.calculateItemUnitPriceWithAdjustments(allowSalePrice);
    }

    public void finalizeQuantities() {
        detail.finalizeQuantities();
    }

    public void clearAllNonFinalizedQuantities() {
        detail.clearAllNonFinalizedQuantities();
    }

    public String buildDetailKey() {
        return detail.buildDetailKey();
    }

    public Money getFinalizedTotalWithAdjustments() {
        return detail.getFinalizedTotalWithAdjustments();
    }

    public Money calculateTotalAdjustmentValue() {
        return detail.calculateTotalAdjustmentValue();
    }

    public PromotableOrderItemPriceDetail splitIfNecessary() {
        return detail.splitIfNecessary();
    }

    public boolean useSaleAdjustments() {
        return detail.useSaleAdjustments();
    }

    public boolean isAdjustmentsFinalized() {
        return detail.isAdjustmentsFinalized();
    }

    public void setAdjustmentsFinalized(boolean adjustmentsFinalized) {
        detail.setAdjustmentsFinalized(adjustmentsFinalized);
    }

    @Override
    public PromotableOrderItemPriceDetail shallowCopy() {
        return detail.shallowCopy();
    }

    @Override
    public PromotableOrderItemPriceDetail copyWithFinalizedData() {
        return detail.copyWithFinalizedData();
    }

}
