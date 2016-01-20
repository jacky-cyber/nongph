package cn.globalph.b2c.offer.service.discount.domain;

import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.b2c.offer.domain.OrderItemPriceDetailAdjustment;
import cn.globalph.common.money.Money;

import java.math.BigDecimal;

public class PromotableOrderItemPriceDetailAdjustmentImpl extends AbstractPromotionRounding implements PromotableOrderItemPriceDetailAdjustment, OfferHolder {

    private static final long serialVersionUID = 1L;

    protected PromotableCandidateItemOffer promotableCandidateItemOffer;
    protected PromotableOrderItemPriceDetail promotableOrderItemPriceDetail;
    protected Money saleAdjustmentValue;
    protected Money retailAdjustmentValue;
    protected Money adjustmentValue;
    protected boolean appliedToSalePrice;
    protected Offer offer;

    public PromotableOrderItemPriceDetailAdjustmentImpl(PromotableCandidateItemOffer promotableCandidateItemOffer,
            PromotableOrderItemPriceDetail orderItemPriceDetail) {
        assert (promotableCandidateItemOffer != null);
        assert (orderItemPriceDetail != null);
        this.promotableCandidateItemOffer = promotableCandidateItemOffer;
        this.promotableOrderItemPriceDetail = orderItemPriceDetail;
        this.offer = promotableCandidateItemOffer.getOffer();
        computeAdjustmentValues();
    }

    public PromotableOrderItemPriceDetailAdjustmentImpl(OrderItemPriceDetailAdjustment itemAdjustment,
            PromotableOrderItemPriceDetail orderItemPriceDetail) {
        assert (orderItemPriceDetail != null);
        adjustmentValue = itemAdjustment.getValue();
        if (itemAdjustment.isAppliedToSalePrice()) {
            saleAdjustmentValue = itemAdjustment.getValue();
            // This will just set to a Money value of zero in the correct currency.
            retailAdjustmentValue = itemAdjustment.getRetailPriceValue();
        } else {
            retailAdjustmentValue = itemAdjustment.getValue();
            // This will just set to a Money value of zero in the correct currency.
            saleAdjustmentValue = itemAdjustment.getSalesPriceValue();
        }
        appliedToSalePrice = itemAdjustment.isAppliedToSalePrice();
        promotableOrderItemPriceDetail = orderItemPriceDetail;
        offer = itemAdjustment.getOffer();
    }

    /*
     * Calculates the value of the adjustment for both retail and sale prices.   
     * If either adjustment is greater than the item value, it will be set to the
     * currentItemValue (e.g. no adjustment can cause a negative value). 
     */
    protected void computeAdjustmentValues() {
        saleAdjustmentValue = Money.ZERO;
        retailAdjustmentValue = Money.ZERO;

        Money currentPriceDetailRetailValue = promotableOrderItemPriceDetail.calculateItemUnitPriceWithAdjustments(false);
        Money currentPriceDetailSalesValue = promotableOrderItemPriceDetail.calculateItemUnitPriceWithAdjustments(true);
        if (currentPriceDetailSalesValue == null) {
            currentPriceDetailSalesValue = currentPriceDetailRetailValue;
        }
        
        BigDecimal offerUnitValue = PromotableOfferUtility.determineOfferUnitValue(offer, promotableCandidateItemOffer);

        retailAdjustmentValue = PromotableOfferUtility.computeAdjustmentValue(currentPriceDetailRetailValue, offerUnitValue, this, this);
                
        if (offer.getApplyDiscountToSalePrice() == true) {
            saleAdjustmentValue = PromotableOfferUtility.computeAdjustmentValue(currentPriceDetailSalesValue, offerUnitValue, this, this);
        }
    }

    @Override
    public Money getRetailAdjustmentValue() {
        return retailAdjustmentValue;
    }

    @Override
    public Money getSaleAdjustmentValue() {
        return saleAdjustmentValue;
    }

    @Override
    public PromotableOrderItemPriceDetail getPromotableOrderItemPriceDetail() {
        return promotableOrderItemPriceDetail;
    }

    @Override
    public Offer getOffer() {
        return offer;
    }

    @Override
    public boolean isCombinable() {
        Boolean combinable = offer.isCombinableWithOtherOffers();
        return (combinable != null && combinable);
    }

    @Override
    public boolean isTotalitarian() {
        Boolean totalitarian = offer.isTotalitarianOffer();
        return (totalitarian != null && totalitarian.booleanValue());
    }

    @Override
    public Long getOfferId() {
        return offer.getId();
    }

    @Override
    public Money getAdjustmentValue() {
        return adjustmentValue;
    }

    @Override
    public boolean isAppliedToSalePrice() {
        return appliedToSalePrice;
    }

    @Override
    public void finalizeAdjustment(boolean useSalePrice) {
        appliedToSalePrice = useSalePrice;
        if (useSalePrice) {
            adjustmentValue = saleAdjustmentValue;
        } else {
            adjustmentValue = retailAdjustmentValue;
        }
    }

    @Override
    public PromotableOrderItemPriceDetailAdjustment copy() {
        PromotableOrderItemPriceDetailAdjustmentImpl newAdjustment = new PromotableOrderItemPriceDetailAdjustmentImpl(
                promotableCandidateItemOffer, promotableOrderItemPriceDetail);
        newAdjustment.adjustmentValue = adjustmentValue;
        newAdjustment.saleAdjustmentValue = saleAdjustmentValue;
        newAdjustment.retailAdjustmentValue = retailAdjustmentValue;
        newAdjustment.appliedToSalePrice = appliedToSalePrice;
        return newAdjustment;
    }
}
