package cn.globalph.b2c.offer.service.discount;

import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.b2c.offer.domain.OfferItemCriteria;


/**
 * Wraps the promotion qualifier.   Serves as a useful starting point for extension.
 * 
 * @author bpolster
 */
public class PromotionQualifierWrapper extends PromotionQualifier {

    private static final long serialVersionUID = 1L;
    
    private PromotionQualifier wrappedQualifier;
    
    public PromotionQualifierWrapper(PromotionQualifier pq) {
        wrappedQualifier = pq;
    }

    public Offer getPromotion() {
        return wrappedQualifier.getPromotion();
    }

    public void setPromotion(Offer promotion) {
        wrappedQualifier.setPromotion(promotion);
    }

    public void setItemCriteria(OfferItemCriteria itemCriteria) {
        wrappedQualifier.setItemCriteria(itemCriteria);
    }

    public int getQuantity() {
        return wrappedQualifier.getQuantity();
    }

    public void setQuantity(int quantity) {
        wrappedQualifier.setQuantity(quantity);
    }

    public void setFinalizedQuantity(int finalizedQuantity) {
        wrappedQualifier.setFinalizedQuantity(finalizedQuantity);
    }

    public PromotionQualifier copy() {
        return wrappedQualifier.copy();
    }

    public boolean equals(Object arg0) {
        return wrappedQualifier.equals(arg0);
    }

    public OfferItemCriteria getItemCriteria() {
        return wrappedQualifier.getItemCriteria();
    }

    public int getFinalizedQuantity() {
        return wrappedQualifier.getFinalizedQuantity();
    }

    public int hashCode() {
        return wrappedQualifier.hashCode();
    }

    public void incrementQuantity(int quantity) {
        wrappedQualifier.incrementQuantity(quantity);
    }

    public void resetQty(int qty) {
        wrappedQualifier.resetQty(qty);
    }

    public PromotionQualifier split(int splitItemQty) {
        return wrappedQualifier.split(splitItemQty);
    }

    public boolean isFinalized() {
        return wrappedQualifier.isFinalized();
    }

    public String toString() {
        return wrappedQualifier.toString();
    }
}
